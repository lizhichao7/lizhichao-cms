package com.lizhichao.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lizhichao.cms.bean.Article;
import com.lizhichao.cms.bean.Category;
import com.lizhichao.cms.bean.Channel;
import com.lizhichao.cms.bean.Comment;
import com.lizhichao.cms.bean.Complain;
import com.lizhichao.cms.bean.Favorite;
import com.lizhichao.cms.bean.Slide;
import com.lizhichao.cms.common.CmsContant;
import com.lizhichao.cms.mapper.ArticleMapper;
import com.lizhichao.cms.mapper.ArticleRep;
import com.lizhichao.cms.mapper.SlideMapper;
@Service
public class ArticleServiceImpl implements ArticleService{
	
	@Autowired
	private ArticleMapper articleMapper;
	
	@Autowired
	SlideMapper slideMapper;
	
	@Autowired
	ArticleRep articleRep;
	
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
	
	@Override
	public PageInfo<Article> listByUser(Integer id, int page) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, CmsContant.PAGE_SIZE);
		PageInfo<Article> articlePage=  new PageInfo<Article>(articleMapper.listByUser(id));
		
		return articlePage;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return articleMapper.updateStatus(id,CmsContant.ARTICLE_STATUS_DEL);
	}

	@Override
	public List<Channel> getChannels() {
		// TODO Auto-generated method stub
		return articleMapper.getAllChannels();
	}

	@Override
	public List<Category> getCategorisByCid(int cid) {
		// TODO Auto-generated method stub
		return articleMapper.getCategorisByCid(cid);
	}
	
	@Override
	public int add(Article article) {
		// TODO Auto-generated method stub
		return articleMapper.add(article);
		
	}

	@Override
	public Article getById(int id) {
		// TODO Auto-generated method stub
		return articleMapper.findById(id);
	}

	@Override
	public int update(Article article, Integer userId) {
		// TODO Auto-generated method stub
		Article articleSrc = this.getById(article.getId());
		if(articleSrc.getUserId() != userId) {
			// todo 如果  不是自己的文章 需要。。。。。
		}
		return articleMapper.update(article);
		
	}

	@Override
	public PageInfo<Article> list(int status, int page) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, CmsContant.PAGE_SIZE);
		return new PageInfo<Article>(articleMapper.list(status));
	}
	
	@Override
	public Article getInfoById(int id) {
		// TODO Auto-generated method stub
		return articleMapper.getInfoById(id);
	}

	/**
	 * 
	 */
	@Override
	public int setHot(int id, int status) {
		// TODO Auto-generated method stub
		return articleMapper.setHot(id,status);
	}

	@Override
	public int setCheckStatus(int id, int status) {
		// TODO Auto-generated method stub
		if(status==1) {
			Article article = articleMapper.findById(id);
			String jsonString = JSON.toJSONString(article);
			jsonString="add="+jsonString;
			kafkaTemplate.send("articles",jsonString);
			System.err.println("发送消息*****************");
		}
		 return articleMapper.CheckStatus(id,status);
	}
	
	@Override
	public PageInfo<Article> hotList(int page) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page,CmsContant.PAGE_SIZE);
		return new PageInfo<>(articleMapper.hostList());
	}

	@Override
	public List<Article> lastList() {
		// TODO Auto-generated method stub
		return articleMapper.lastList(CmsContant.PAGE_SIZE);
	}

	@Override
	public List<Slide> getSlides() {
		// TODO Auto-generated method stub
		return slideMapper.list();
	}

	@Override
	public PageInfo<Article> getArticles(int channleId, int catId, int page) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page,CmsContant.PAGE_SIZE);
		
		return new PageInfo<Article>(articleMapper.getArticles(channleId, catId));
	}

	@Override
	public List<Category> getCategoriesByChannelId(int channleId) {
		// TODO Auto-generated method stub
		return articleMapper.getCategoriesByChannelId(channleId) ;
	}
	
	@Override
	public int addComment(Comment comment) {
		// TODO Auto-generated method stub
		int result =  articleMapper.addComment(comment);
		 //文章评论数目自增
		if(result>0)
			articleMapper.increaseCommentCnt(comment.getArticleId());
		
		return result;
	}

	@Override
	public PageInfo<Comment> getComments(int articleId, int page) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, CmsContant.PAGE_SIZE);
		return new PageInfo<Comment>(articleMapper.getComments(articleId));
		
		
	}
	
	@Override
	public int addComplian(Complain complain) {
		// TODO Auto-generated method stub
		
		//添加投诉到数据库
		int result = articleMapper.addCoplain(complain);
		// 增加投诉的数量
		if(result>0)
			articleMapper.increaseComplainCnt(complain.getArticleId());
		
		return 0;
	}

	@Override
	public PageInfo<Complain> getComplains(int articleId, int page) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, CmsContant.PAGE_SIZE);
		return new PageInfo<Complain>(articleMapper.getComplains(articleId));
	}

	@Override
	public List<Complain> qlist(Complain complain) {
		// TODO Auto-generated method stub
		return articleMapper.qlist(complain);
	}

	@Override
	public List<Article> comList() {
		// TODO Auto-generated method stub
		return articleMapper.comList(CmsContant.PAGE_SIZE);
	}

	//访问量  增加 1
	@Override
	public int updHits(String substring) {
		// TODO Auto-generated method stub
		return articleMapper.updHits(substring);
	}

	//访问量  增加 1
	@Override
	public int updaHits(Article article) {
		// TODO Auto-generated method stub
		return articleMapper.updaHits(article);
	}

	@Override
	public List<Favorite> listfavorite(Favorite favorite) {
		// TODO Auto-generated method stub
		return articleMapper.listfavorite(favorite);
	}

	@Override
	public void addfavorite(Favorite favorite) {
		// TODO Auto-generated method stub
		articleMapper.addfavorite(favorite);
	}

	@Override
	public void delfavorite(int id) {
		// TODO Auto-generated method stub
		articleMapper.delfavorite(id);
	}

}
