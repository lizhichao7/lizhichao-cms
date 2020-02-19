package com.lizhichao.cms.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.lizhichao.cms.bean.Article;
import com.lizhichao.cms.bean.Category;
import com.lizhichao.cms.bean.Channel;
import com.lizhichao.cms.bean.Slide;
import com.lizhichao.cms.mapper.ArticleRep;
import com.lizhichao.cms.service.ArticleService;
import com.lizhichao.cms.utils.HLUtils;

@Controller
public class IndexController {
	@Autowired
	ArticleService articleService;
	
	//注入es仓库
	@Autowired
	ArticleRep articleRep;
	
	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;
	
	//自动注入redisRemplate
	@Autowired
	RedisTemplate redisTemplate;
	
	// es
	@GetMapping("index")
	public String search(String key,Model model,HttpServletRequest request,@RequestParam(defaultValue="1")int page) {
		Thread  t1 =  new Thread() {
			public void run() {
		// 获取所有的栏目
		List<Channel> channels = articleService.getChannels();
		request.setAttribute("channels", channels);
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
		
		t1.start();
		t3.start();
		t9.start();
			
		//利用es的仓库来查询（无高亮）
//		List<Article> list = articleRep.findByTitle(key);
//		PageInfo<Article> pageInfo = new PageInfo<>(list);
//		model.addAttribute("articlePage", pageInfo);
			
		//利用es实现高亮HightLight
		long start = System.currentTimeMillis();
		PageInfo<Article> pageInfo = (PageInfo<Article>) HLUtils.findByHighLight(elasticsearchTemplate, Article.class, page, 5, new String[] {"title"}, "id", key);
		long end = System.currentTimeMillis();
		model.addAttribute("articlePage", pageInfo);
		model.addAttribute("key", key);
		System.err.println("es搜索所耗"+(end-start)+"ms");
		
		
		return "index";
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws InterruptedException 
	 */
	@RequestMapping(value= {"index","/"})
	public String index(Model m,HttpServletRequest request,@RequestParam(defaultValue="1") int page) throws InterruptedException {
		
		Thread  t1 =  new Thread() {
			public void run() {
		// 获取所有的栏目
		List<Channel> channels = articleService.getChannels();
		request.setAttribute("channels", channels);
			};
		};
		
//		Thread  t2 =  new Thread() {
//		public void run() {
//		// 获取热门文章
//		PageInfo<Article> articlePage= articleService.hotList(page);
//		request.setAttribute("articlePage", articlePage);
//			};
//	};
		
		Thread t2 = new Thread() {
			//获取热门文章
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				
				
				/*//先获取redis里的数据
				 List<Article> range = redisTemplate.opsForList().range("host_articles", 0, -1);
				//如果数据库里面没有数据
				if(range==null || range.size()==0) {
					 PageInfo<Article> hotList = articleService.hotList(page);
					System.err.println("从mysql中查询了热门文章...");
					//从数据库里面获取数据并装入redis缓存
					redisTemplate.opsForList().leftPushAll("host_articles",hotList.getList().toArray());
					//设置过期时间为5分钟
					redisTemplate.expire("host_articles", 5, TimeUnit.MINUTES);
					//获取热门文章
					m.addAttribute("articlePage", hotList);
				}else {//否则
//					List range2 = redisTemplate.opsForList().range("host_articles", 0, -1);
					PageInfo<Article> pageInfo = new PageInfo<>(range);
					System.err.println("从redis中查询了热门文章...");
					m.addAttribute("articlePage", pageInfo);
				}*/
				
				List<Article> range = redisTemplate.opsForList().range("host_articles", 0, -1);
				if(range==null || range.size()==0) {
					PageInfo<Article> hotList = articleService.hotList(page);
					System.err.println("从mysql中查询了热门文章...");
					redisTemplate.opsForList().leftPushAll("host_articles", hotList.getList().toArray());
					redisTemplate.expire("host_articles", 5, TimeUnit.MINUTES);
					m.addAttribute("articlePage", hotList);
				}else {
					redisTemplate.opsForList().range("host_articles", 0, -1);
					PageInfo<Article> pageInfo = new PageInfo<>(range);
					System.err.println("从redis中查询了热门文章...");
					m.addAttribute("articlePage", pageInfo);
				}
			}
		};
		
		
		Thread  t9 =  new Thread() {
			public void run() {
		// 获取投诉文章
		List<Article> comArticles = articleService.comList();
		request.setAttribute("comArticles", comArticles);
				
			};
		};
		
		Thread t3 = new Thread() {
			//获取最新文章
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				
				
				//先获取redis里的数据
				List range = redisTemplate.opsForList().range("new_articles", 0, -1);
				//如果数据库里面没有数据
				if(range==null || range.size()==0) {
					List<Article> lastArticles= articleService.lastList();
					System.err.println("从mysql中查询了最新文章...");
					//从数据库里面获取数据并装入redis缓存
					redisTemplate.opsForList().leftPushAll("new_articles",lastArticles.toArray());
					//设置过期时间为5分钟
					redisTemplate.expire("new_articles", 5, TimeUnit.MINUTES);
					//获取最新文章
					m.addAttribute("lastArticles", lastArticles);
				}else {//否则
//					List range2 = redisTemplate.opsForList().range("new_articles", 0, -1);
					System.err.println("从redis中查询了最新文章...");
					m.addAttribute("lastArticles", range);
				}
			}
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
