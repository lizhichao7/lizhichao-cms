package com.lizhichao.cms.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.lizhichao.cms.bean.Article;
import com.lizhichao.cms.bean.Category;
import com.lizhichao.cms.bean.Channel;
import com.lizhichao.cms.bean.Slide;
import com.lizhichao.cms.service.ArticleService;

@Controller
public class IndexController {
	@Autowired
	ArticleService articleService;
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws InterruptedException 
	 */
	@RequestMapping(value= {"index","/"})
	public String index(HttpServletRequest request,@RequestParam(defaultValue="1") int page) throws InterruptedException {
		
		Thread  t1 =  new Thread() {
			public void run() {
		// 获取所有的栏目
		List<Channel> channels = articleService.getChannels();
		request.setAttribute("channels", channels);
			};
		};
		
		Thread  t2 =  new Thread() {
			public void run() {
		// 获取热门文章
		PageInfo<Article> articlePage= articleService.hotList(page);
		request.setAttribute("articlePage", articlePage);
			};
		};
		
		Thread  t9 =  new Thread() {
			public void run() {
		// 获取投诉文章
		List<Article> comArticles = articleService.comList();
		request.setAttribute("comArticles", comArticles);
			};
		};
		
		Thread  t3 =  new Thread() {
			public void run() {
		// 获取最新文章
		List<Article> lastArticles = articleService.lastList();
		request.setAttribute("lastArticles", lastArticles);
			};
		};
		
		Thread  t4 =  new Thread() {
			public void run() {
		// 轮播图
		List<Slide> slides = articleService.getSlides();
		request.setAttribute("slides", slides);
		
			};
		};
		
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t9.start();
		
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		t9.join();
		
		return "index";
		
	}
	
	/**
	 * 
	 * @param request  请求
	 * @param channleId  栏目的id
	 * @param catId 分类的id
	 * @param page 页码
	 * @return
	 * @throws InterruptedException 
	 */
	@RequestMapping("channel")
	public String channel(HttpServletRequest request,
			int channelId,
			@RequestParam(defaultValue="0") int catId,
			@RequestParam(defaultValue="1")  int page) throws InterruptedException {
		
		Thread  t1 =  new Thread() {
			public void run() {
		// 获取所有的栏目
		List<Channel> channels = articleService.getChannels();
		request.setAttribute("channels", channels);
			};
		};
		
		Thread  t7 =  new Thread() {
			public void run() {
		// 获取热门文章
		PageInfo<Article> articlePage= articleService.hotList(page);
		request.setAttribute("articlePage", articlePage);
			};
		};
		
		
		Thread  t2 =  new Thread() {
			public void run() {
		// 当前栏目下  当前分类下的文章
		PageInfo<Article> articlePage= articleService.getArticles(channelId,catId, page);
		request.setAttribute("articlePage", articlePage);
			};
		};
		
		Thread  t3 =  new Thread() {
			public void run() {
		// 获取最新文章
		List<Article> lastArticles = articleService.lastList();
		request.setAttribute("lastArticles", lastArticles);
			};
		};
		
		Thread  t9 =  new Thread() {
			public void run() {
		// 获取投诉文章
		List<Article> comArticles = articleService.comList();
		request.setAttribute("comArticles", comArticles);
			};
		};
		
		Thread  t4 =  new Thread() {
			public void run() {
		// 轮播图
		List<Slide> slides = articleService.getSlides();
		request.setAttribute("slides", slides);
		
			};
		};
		
		// 获取当前栏目下的所有的分类 catId
		Thread  t5 =  new Thread() {
			public void run() {
		// 
		List<Category> categoris= articleService.getCategoriesByChannelId(channelId);
		request.setAttribute("categoris", categoris);
		System.err.println("categoris is " + categoris);
			};
		};
		
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t9.start();
		t7.start();
		
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		t5.join();
		t9.join();
		t7.join();
		
		// 参数回传
		request.setAttribute("catId", catId);
		request.setAttribute("channelId", channelId);
		
		return "channel";
	}
}
