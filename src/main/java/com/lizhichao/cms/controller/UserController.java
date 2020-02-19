package com.lizhichao.cms.controller;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lizhichao.FileUtils;
import com.lizhichao.HtmlUtils;
import com.lizhichao.StringUtil;
import com.lizhichao.StringUtils;
import com.lizhichao.cms.bean.Article;
import com.lizhichao.cms.bean.Category;
import com.lizhichao.cms.bean.Channel;
import com.lizhichao.cms.bean.Favorite;
import com.lizhichao.cms.bean.User;
import com.lizhichao.cms.common.CmsContant;
import com.lizhichao.cms.service.ArticleService;
import com.lizhichao.cms.service.UserService;

/**
 * 
 * @author 李志超
 *
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {
	
	
	//public native void test();  .dll
		@Autowired
		UserService userService;
		
		@Autowired
		ArticleService   articleService;
		
		@RequestMapping("home")
		public String home() {
			return "user/home";
		}
		
		@RequestMapping("logout")
		public String home(HttpServletRequest request,HttpServletResponse response) {
			request.getSession().removeAttribute(CmsContant.USER_KEY);
			
			
			Cookie cookieUserName = new Cookie("username", "");
			cookieUserName.setPath("/");
			cookieUserName.setMaxAge(0);// 立即过期
			response.addCookie(cookieUserName);
			Cookie cookieUserPwd = new Cookie("userpwd", "");
			cookieUserPwd.setPath("/");
			cookieUserPwd.setMaxAge(0);// 立即过期
			response.addCookie(cookieUserPwd);
			
			return "redirect:/";
		}
		
		/**
		 * 跳转到注册界面
		 * @param request
		 * @return
		 */
		@RequestMapping(value="register",method=RequestMethod.GET)
		public String register(HttpServletRequest request) {
			User user  = new User();
			request.setAttribute("user", user);
			return "user/register";
		}
		
		/**
		 * 从注册页面发过来的请求
		 * @param request
		 * @return
		 */
		@RequestMapping(value="register",method=RequestMethod.POST)
		public String register(HttpServletRequest request,
				@Valid @ModelAttribute("user") User user,
				BindingResult result
				) {
			
			// 有错误返回到注册页面
			if(result.hasErrors()) {
				return "user/register";
			}
			
			//进行唯一性验证
			User existUser = userService.getUserByUsername(user.getUsername());
			if(existUser!=null) {
				result.rejectValue("username", "", "用户名已经存在");
				return "user/register";
			}
					
			//加一个手动的校验
			if(StringUtils.isNumber(user.getPassword())) {
				result.rejectValue("password", "", "密码不能全是数字");
				return "user/register";
			}
			
			// 去注册
			int reRegister = userService.register(user);
			
			//注册失败
			if(reRegister<1) {
				request.setAttribute("eror", "注册失败，请稍后再试！");
				return "user/register";
			}
			
			//跳转到登录页面
			return "redirect:login";
		}
		
		/**
		 * 跳转登录册界面
		 * @param request
		 * @return
		 */
		@RequestMapping(value="login",method=RequestMethod.GET)
		public String login(HttpServletRequest request) {
			return "user/login";
		}
		
		/**
		 * 接受登录界面的请求
		 * @param request
		 * @return
		 */
		@RequestMapping(value="login",method=RequestMethod.POST)
		public String login(HttpServletRequest request,HttpServletResponse response, User user) {
			String pwd =  new String(user.getPassword());
			User loginUser = userService.login(user);
			
			//登录失败
			if(loginUser==null) {
				request.setAttribute("error", "用户名密码错误");
				return "/user/login";	
			}
			
			// 登录成功，用户信息存放看到session当中
			
			request.getSession().setAttribute(CmsContant.USER_KEY, loginUser);
			
			//保存用户的用户名和密码
			Cookie cookieUserName = new Cookie("username", user.getUsername());
			cookieUserName.setPath("/");
			cookieUserName.setMaxAge(10*24*3600);// 10天
			response.addCookie(cookieUserName);
			Cookie cookieUserPwd = new Cookie("userpwd", pwd);
			cookieUserPwd.setPath("/");
			cookieUserPwd.setMaxAge(10*24*3600);// 10天
			response.addCookie(cookieUserPwd);
			
			
			
			// 进入管理界面
			if(loginUser.getRole()==CmsContant.USER_ROLE_ADMIN)
				 return "redirect:/admin/index";	
			
			
			// 进入个人中心
			return "redirect:/user/home";
			
			
			
			
			
		}
		
		
		
		/**
		 * 
		 * @param username
		 * @return
		 */
		@RequestMapping("checkname")
		@ResponseBody
		public boolean checkUserName(String username) {
			User existUser = userService.getUserByUsername(username);
			return existUser==null;
		}
		
		/**
		 * 
		 * @param username
		 * @return
		 */
		@RequestMapping("deletearticle")
		@ResponseBody
		public boolean deleteArticle(int id) {
			int result  = articleService.delete(id);
			return result > 0;
		}
		
		
		@RequestMapping("articles")
		public String articles(HttpServletRequest request,@RequestParam(defaultValue="1") int page) {
			
			User loginUser = (User)request.getSession().getAttribute(CmsContant.USER_KEY);
			
			PageInfo<Article> articlePage = articleService.listByUser(loginUser.getId(),page);
			
			request.setAttribute("articlePage", articlePage);
			
			return "user/article/list";
		}
		
		@RequestMapping("comments")
		public String comments() {
			return "user/comment/list";
		}
		
		/**
		 * 跳转到发布文章的页面
		 * @return
		 */
		@RequestMapping("postArticle")
		public String postArticle(HttpServletRequest request) {	
			List<Channel> channels= articleService.getChannels();
			request.setAttribute("channels", channels);
			return "user/article/post";
		}
		
		/**
		 * 跳转到修改文章的页面
		 * @return
		 */
		@RequestMapping(value="updateArticle",method=RequestMethod.GET)
		public String updateArticle(HttpServletRequest request,int id) {	
			
			//获取栏目
			List<Channel> channels= articleService.getChannels();
			request.setAttribute("channels", channels);
			
			//获取文章
			Article article = articleService.getById(id);
			User loginUser = (User)request.getSession().getAttribute(CmsContant.USER_KEY);
			if(loginUser.getId() != article.getUserId()) {
				// todo 准备做异常处理的！！
			}
			request.setAttribute("article", article);
			request.setAttribute("content1",  HtmlUtils.htmlspecialchars(article.getContent()));
			
			
			return "user/article/update";
		}
		
		/**
		 *  获取分类
		 * @param request
		 * @param cid
		 * @return
		 */
		@RequestMapping("getCategoris")
		@ResponseBody
		public List<Category>  getCategoris(int cid) {	
			List<Category> categoris = articleService.getCategorisByCid(cid);
			return categoris;
		}
		
		/**
		 * 
		 * @param request
		 * @param article
		 * @param file
		 * @return
		 */
		@RequestMapping(value = "postArticle",method=RequestMethod.POST)
		@ResponseBody
		public boolean postArticle(HttpServletRequest request, Article article, 
				MultipartFile file
				) {
			
			
			String picUrl;
			try {
				// 处理上传文件
				picUrl = processFile(file);
				article.setPicture(picUrl);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//当前用户是文章的作者
			User loginUser = (User)request.getSession().getAttribute(CmsContant.USER_KEY);
			article.setUserId(loginUser.getId());
			
			
			return articleService.add(article)>0;
		}
		
		/**
		 * 接受修改文章的页面提交的数据
		 * @return
		 */
		@RequestMapping(value="updateArticle",method=RequestMethod.POST)
		@ResponseBody
		public  boolean  updateArticle(HttpServletRequest request,Article article,MultipartFile file) {
			
			System.out.println("aarticle is  "  + article);
			
			String picUrl;
			try {
				// 处理上传文件
				picUrl = processFile(file);
				article.setPicture(picUrl);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//当前用户是文章的作者
			User loginUser = (User)request.getSession().getAttribute(CmsContant.USER_KEY);
			//article.setUserId(loginUser.getId());
			int updateREsult  = articleService.update(article,loginUser.getId());
			
			
			return updateREsult>0;
			
		}
		
		//查询收藏夹
		@RequestMapping("listfavorite")
		public String listfavorite(Favorite favorite,HttpServletRequest request,@RequestParam(defaultValue="1")int pageNum) {
			PageHelper.startPage(pageNum, 10);
			List<Favorite> list = articleService.listfavorite(favorite);
			PageInfo<Favorite> page = new PageInfo<>(list);
			request.setAttribute("list", list);
			request.setAttribute("f", favorite);
			request.setAttribute("page", page);
			
			return "user/article/flist";
			
		}
		
		//去添加页面
		@RequestMapping("toaddfavorite")
		public String toaddfavorite() {
			
			return "user/article/addfavorite";
			
		}
		
		//添加收藏夹
		@RequestMapping(value="addfavorite",method=RequestMethod.POST)
		public String addfavorite(@ModelAttribute("favorite") @Valid Favorite favorite,MultipartFile file,BindingResult result,HttpServletRequest request) throws IllegalStateException, IOException {
			
			/*if(!StringUtil.isHttpUrl(favorite.getUrl())) {
				result.rejectValue("Url", "", "不是合法的url地址");
			}*/
			/*if(result.hasErrors()) {
				return "user/article/addfavorite";
			}*/
			
			articleService.addfavorite(favorite);
			
			return "user/home";
			
		}
		
		//删除收藏夹
		@RequestMapping("delfavorite")
		public String delfavorite(HttpServletRequest request) {
			int id = Integer.parseInt(request.getParameter("id"));
			articleService.delfavorite(id);
			
			return "user/home";
			
		}
}
