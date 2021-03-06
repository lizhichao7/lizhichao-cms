package com.lizhichao.cms.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.PageInfo;
import com.lizhichao.cms.bean.Article;
import com.lizhichao.cms.bean.Category;
import com.lizhichao.cms.bean.Channel;
import com.lizhichao.cms.bean.Comment;
import com.lizhichao.cms.bean.Complain;
import com.lizhichao.cms.bean.Favorite;
import com.lizhichao.cms.bean.Slide;

public interface ArticleService {
	/**
	 * 根据用户列出文章
	 * @param id
	 * @param page
	 * @return
	 */
	PageInfo<Article> listByUser(Integer id, int page);

	/**
	 * 删除文章
	 * @param id
	 * @return
	 */
	int delete(int id);

	/**
	 * 获取所有的栏目
	 * @return
	 */
	List<Channel> getChannels();
	
	List<Category> getCategorisByCid(int cid);
	
	/**
	 * 发布文章
	 * @param article
	 */
	int add(Article article);

	/**
	 * 根据文章id获取文章对象
	 * @param id
	 * @return
	 */
	Article getById(int id);
	
	/**
	 * 获取文章的简要信息  常常用于判断文章的存在性
	 * @param id
	 * @return
	 */
	Article getInfoById(int id);

	/**
	 * 
	 * @param article
	 * @param id
	 * @return
	 */
	int update(Article article, Integer id);

	/**
	 * 获取文章列表
	 * @param status
	 * @param page
	 * @return
	 */
	PageInfo<Article> list(int status, int page);
	
	int setHot(int id, int status);

	int setCheckStatus(int id, int status);
	
	/**
	 * 获取热门文章
	 * @param page
	 * @return
	 */
	PageInfo<Article> hotList(int page);
	

	/**
	 * 获取最新文章NAG
	 * @return
	 */
	List<Article> lastList();
	
	List<Article> comList();

	/**
	 * 获取轮播图
	 * @return
	 */
	List<Slide> getSlides();

	/**
	 * 获取栏目下的文章
	 * @param channleId
	 * @param catId
	 * @param page
	 * @return
	 */
	PageInfo<Article> getArticles(int channleId, int catId, int page);

	/**
	 * 获取栏目下的分类
	 * @param channleId
	 * @return
	 */
	List<Category> getCategoriesByChannelId(int channleId);
	
	/**
	 * 发表评论
	 * @param comment
	 * @return
	 */
	int addComment(Comment comment);

	/**
	 * 根据文章id获取评论
	 * @param id
	 * @param page
	 * @return
	 */
	PageInfo<Comment> getComments(int articleId, int page);
	
	int addComplian( Complain complain);

	/**
	 * 获取投书
	 * @param articleId
	 * @param page
	 * @return
	 */
	PageInfo<Complain> getComplains(int articleId, int page);
	
	List<Complain> qlist(Complain complain);
	
	//访问量+1
	int updHits(String substring);


	//访问量+1
	int updaHits(Article article);

	List<Favorite> listfavorite(Favorite favorite);
	
	void addfavorite(Favorite favorite);
	
	void delfavorite(@Param("id")int id);
}
