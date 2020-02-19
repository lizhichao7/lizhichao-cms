package com.lizhichao.cms.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.lizhichao.StringUtils;
import com.lizhichao.cms.bean.Article;
import com.lizhichao.cms.bean.Channel;
import com.lizhichao.cms.bean.Comment;
import com.lizhichao.cms.bean.Complain;
import com.lizhichao.cms.bean.Favorite;
import com.lizhichao.cms.bean.User;
import com.lizhichao.cms.common.CmsContant;
import com.lizhichao.cms.common.CmsError;
import com.lizhichao.cms.common.CmsMessage;
import com.lizhichao.cms.mapper.ArticleRep;
import com.lizhichao.cms.service.ArticleService;
import com.lizhichao.cms.utils.HLUtils;

@Controller
@RequestMapping("article")
public class ArticleController extends BaseController {
	@Autowired
	ArticleService articleService;
	
	@Autowired
	RedisTemplate redisTemplate;
	
	//注入spring的线程池
	@Autowired
	ThreadPoolTaskExecutor executor;
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("getDetail")
	@ResponseBody
	public CmsMessage getDetail(int id,Model m) {
		if(id<=0) {
			
		}
		// 获取文章详情
		Article article = articleService.getById(id);
		
		// 不存在
		if(article==null)
			return new CmsMessage(CmsError.NOT_EXIST, "文章不存在",null);
		
		// 返回数据
		return new CmsMessage(CmsError.SUCCESS,"",article); 
		
	}
	
	@RequestMapping("detail")
	public String detail(HttpServletRequest request,int id) {
		
		Article article = articleService.getById(id);
		request.setAttribute("article", article);
		
		/**
		 * 现在请你利用Redis提高性能，当用户浏览文章时，
		 * 将“Hits_${文章ID}_${用户IP地址}”为key，查询Redis里有没有该key，如果有key，则不做任何操作。
		 * 如果没有，则使用Spring线程池异步执行数据库加1操作，
		 * 并往Redis保存key为Hits_${文章ID}_${用户IP地址}，value为空值的记录，而且有效时长为5分钟。
		 */
		//获取用户ip地址  的方法
		String user_ip = request.getRemoteAddr();
		//准备redis'的key
		String key = "Hits"+id+user_ip;
		//查询redis中的该key
		String redisKey = (String) redisTemplate.opsForValue().get(key);
		if(redisKey==null) {
			executor.execute(new Runnable() {

				@Override
				public void run() {
					//在这里就可以写具体的逻辑了
					//数据库+1操作(根据id从mysql中查询文章对象)
					//设置浏览量+1
					article.setHits(article.getHits()+1);
					//更新到数据库
					articleService.updaHits(article);
					//并往Redis保存key为Hits_${文章ID}_${用户IP地址}，value为空值的记录，而且有效时长为5分钟。
					redisTemplate.opsForValue().set(key, "",5, TimeUnit.MINUTES);
				}
			});
		}
		
		return "detail";
		
	}
	
	@RequestMapping("postcomment")
	@ResponseBody
	public CmsMessage postcomment(HttpServletRequest request,int articleId,String content) {
		
		User loginUser  = (User)request.getSession().getAttribute(CmsContant.USER_KEY);
		
		if(loginUser==null) {
			return new CmsMessage(CmsError.NOT_LOGIN, "您尚未登录！", null);
		}
		
		Comment comment = new Comment();
		comment.setUserId(loginUser.getId());
		comment.setContent(content);
		comment.setArticleId(articleId);
		int result = articleService.addComment(comment);
		if(result > 0)
			return new CmsMessage(CmsError.SUCCESS, "成功", null);
		
		return new CmsMessage(CmsError.FAILED_UPDATE_DB, "异常原因失败，请与管理员联系", null);
		
	}
	// {articleId:'${article.id}',content:$("#co
	
		//comments?id
		@RequestMapping("comments")
		public String comments(HttpServletRequest request,int id,int page) {
			PageInfo<Comment> commentPage =  articleService.getComments(id,page);
			request.setAttribute("commentPage", commentPage);
			return "comments";
		}
		
		/**
		 * 跳转到投诉的页面
		 * @param request
		 * @param articleId
		 * @return
		 */
		@RequestMapping(value="complain",method=RequestMethod.GET)
		public String complain(HttpServletRequest request,int articleId) {
			Article article= articleService.getById(articleId);
			request.setAttribute("article", article);
			request.setAttribute("complain", new Complain());
			return "article/complain";
					
		}
		
		
		/**
		 * 接受投诉页面提交的数据
		 * @param request
		 * @param articleId
		 * @return
		 * @throws IOException 
		 * @throws IllegalStateException 
		 */
		@RequestMapping(value="complain",method=RequestMethod.POST)
		public String complain(HttpServletRequest request,
				@ModelAttribute("complain") @Valid Complain complain,
				MultipartFile file,
				BindingResult result) throws IllegalStateException, IOException {
			
			if(!StringUtils.isUrl(complain.getSrcUrl())) {
				result.rejectValue("srcUrl", "", "不是合法的url地址");
			}
			if(result.hasErrors()) {
				return "article/complain";
			}
			
			User loginUser  =  (User)request.getSession().getAttribute(CmsContant.USER_KEY);
			
			String picUrl = this.processFile(file);
			complain.setPicture(picUrl);
			
			
			//加上投诉人
			if(loginUser!=null)
				complain.setUserId(loginUser.getId());
			else
				complain.setUserId(0);
			
			articleService.addComplian(complain);
			
			return "redirect:/article/detail?id="+complain.getArticleId();
					
		}
		
		//complains?articleId
			@RequestMapping("complains")
			public String 	complains(HttpServletRequest request,int articleId,
					@RequestParam(defaultValue="1") int page) {
				PageInfo<Complain> complianPage=   articleService.getComplains(articleId, page);
				request.setAttribute("complianPage", complianPage);
				return "article/complainslist";
			}
			
			
		
}
