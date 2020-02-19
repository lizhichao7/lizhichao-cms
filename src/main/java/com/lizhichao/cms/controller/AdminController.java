package com.lizhichao.cms.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.lizhichao.cms.bean.Article;
import com.lizhichao.cms.bean.Complain;
import com.lizhichao.cms.common.CmsError;
import com.lizhichao.cms.common.CmsMessage;
import com.lizhichao.cms.mapper.ArticleRep;
import com.lizhichao.cms.service.ArticleService;

@RequestMapping("admin")
@Controller
public class AdminController {
	
	@Autowired
	ArticleService articleService;
	
	@RequestMapping("index")
	public String index() {
		return "admin/index";
	}
	
	@RequestMapping("setArticeHot")
	@ResponseBody
	public CmsMessage setArticeHot(int id,int status) {
		/**
		 * 数据合法性校验 
		 */
		if(status !=0 && status!=1) {
			
		}
		
		if(id<0) {
			
		}
		
		Article article = articleService.getInfoById(id);
		if(article==null) {
			
		}
		if(article.getStatus()==status) {
			
		}
		int result = articleService.setHot(id,status);
		if(result<1)
			return new CmsMessage(CmsError.FAILED_UPDATE_DB,"设置失败，请稍后再试",null);
		
		
		return new CmsMessage(CmsError.SUCCESS,"成功",null);
		
	}
	
	/**
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping("setArticeStatus")
	@ResponseBody
	public CmsMessage  setArticeStatus(int id,int status) {
		
		/**
		 * 数据合法性校验 
		 */
		if(status !=1 && status!=2) {
			return new CmsMessage(CmsError.NOT_VALIDATED_ARGURMENT,"status参数值不合法",null);
		}
		
		if(id<0) {
			return new CmsMessage(CmsError.NOT_VALIDATED_ARGURMENT,"id参数值不合法",null);
		}
		
		Article article = articleService.getInfoById(id);
		if(article==null) {
			return new CmsMessage(CmsError.NOT_EXIST,"数据不存在",null);
		}
		
		/**
		 * 
		 */
		if(article.getStatus()==status) {
			return new CmsMessage(CmsError.NEEDNT_UPDATE,"数据无需更改",null);
		}
		
		/**
		 *  修改数据
		 */
		int result = articleService.setCheckStatus(id,status);
		if(result<1)
			return new CmsMessage(CmsError.FAILED_UPDATE_DB,"设置失败，请稍后再试",null);
		
		
		return new CmsMessage(CmsError.SUCCESS,"成功",null);
		
	}
	
	
	@RequestMapping("article")
	public String article(HttpServletRequest request, @RequestParam(defaultValue= "-1") int status ,
			@RequestParam(defaultValue= "1") int page) {
		PageInfo<Article> articlePage =  articleService.list(status ,page);
		request.setAttribute("status", status);
		request.setAttribute("articlePage", articlePage);
		return "admin/article/list";
		
	}
	
	@RequestMapping("qlist")
	public String qlist(Complain complain,HttpServletRequest request) {
		List<Complain> list = articleService.qlist(complain);
		request.setAttribute("c", complain);
		request.setAttribute("list", list);
		
		return "admin/article/ts";
		
	}
	
	
}
