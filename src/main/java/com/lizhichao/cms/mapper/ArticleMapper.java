package com.lizhichao.cms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.pagehelper.PageInfo;
import com.lizhichao.cms.bean.Article;
import com.lizhichao.cms.bean.Category;
import com.lizhichao.cms.bean.Channel;
import com.lizhichao.cms.bean.Comment;
import com.lizhichao.cms.bean.Complain;
import com.lizhichao.cms.bean.Favorite;

public interface ArticleMapper {
	/**
	 * 根据用户获取文章的列表
	 */
	
	List<Article> listByUser(Integer id);
	
	@Update("update cms_article set deleted=#{status} where id=#{id}")
	int updateStatus(@Param("id")int id,@Param("status")int status);
	
	/**
	 * 获取所有栏目的方法
	 */
	@Select("select id,name from cms_channel")
	List<Channel> getAllChannels();
	
	/**
	 * 根据栏目id 获取分类
	 * @cid : 栏目的id
	 */
	@Select("select id,name from cms_category where channel_id=#{value}")
	List<Category> getCategorisByCid(int cid);
	
	
	@Insert("INSERT INTO cms_article(title,content,picture,channel_id,category_id,user_id,hits,hot,status,deleted,created,updated,commentCnt,articleType)"
			+ " VALUES(#{title},#{content},#{picture},#{channelId},#{categoryId},#{userId},0,0,0,0,now(),now(),0,#{articleType})")
	int add(Article article);
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	Article findById(int id);
	
	@Update("UPDATE cms_article SET title=#{title},content=#{content},picture=#{picture},channel_id=#{channelId},"
			+ " category_id=#{categoryId},status=0,"
			+ "updated=now() WHERE id=#{id} ")
	int update(Article article);

	/**
	 * 文章列表
	 * @param status  文章状态
	 */
	List<Article> list(@Param("status")int status);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Select("SELECT id,title,channel_id channelId , category_id categoryId,status ,hot "
			+ " FROM cms_article WHERE id = #{value} ")
	Article getInfoById(int id);

	/**
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@Update("UPDATE cms_article SET hot=#{hot} WHERE id=#{myid}")
	int setHot(@Param("myid") int id, @Param("hot") int status);

	/**
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@Update("UPDATE cms_article SET status=#{myStatus} WHERE id=#{myid}")
	int CheckStatus(@Param("myid") int id, @Param("myStatus") int status);
	
	List<Article> hostList();

	List<Article> lastList(int pageSize);
	
	List<Article> comList(int pageSize);

	/**
	 * 根据分类和栏目获取文章
	 * @param channleId
	 * @param catId
	 * @return
	 */
	List<Article> getArticles(@Param("channelId")  int channleId, @Param("catId") int catId);

	/**
	 * 
	 * @param channleId
	 * @return
	 */
	@Select("SELECT id,name FROM cms_category where channel_id=#{value}")
	@ResultType(Category.class)
	List<Category> getCategoriesByChannelId(int channleId);
	
	@Insert("INSERT INTO cms_comment(articleId,userId,content,created)"
			+ " VALUES(#{articleId},#{userId},#{content},NOW())")
	int addComment(Comment comment);
	
	/**
	 * 增加文章的评论数量
	 * @param id
	 * @return
	 */
	@Update("UPDATE cms_article SET commentCnt=commentCnt+1 WHERE id=#{value}")
	int increaseCommentCnt(int id);

	/**
	 * 
	 * @param articleId
	 * @return
	 */
	@Select("SELECT c.id,c.articleId,c.userId,u.username as userName,c.content,c.created FROM cms_comment as c "
			+ " LEFT JOIN cms_user as u ON u.id=c.userId "
			+ " WHERE articleId=#{value} ORDER BY c.created DESC")
	List<Comment> getComments(int articleId);
	
	/**
	 * 
	 * @param complain
	 * @return
	 */
	@Insert("INSERT INTO cms_complain(article_id,user_id,complain_type,"
			+ "compain_option,src_url,picture,content,email,mobile,created)"
			+ "   VALUES(#{articleId},#{userId},"
			+ "#{complainType},#{compainOption},#{srcUrl},#{picture},#{content},#{email},#{mobile},now())")
	int addCoplain(Complain complain);

	/**
	 * 
	 * @param articleId
	 */
	@Update("UPDATE cms_article SET complainCnt=complainCnt+1,status=if(complainCnt>10,2,status)  "
			+ " WHERE id=#{value}")
	void increaseComplainCnt(Integer articleId);

	/**
	 * 
	 * @param articleId
	 * @return
	 */
	List<Complain> getComplains(int articleId);
	
	@Select("select * from cms_complain ORDER BY created desc")
	List<Complain> qlist(Complain complain);
	
	@Select("select * from cms_article where status=#{i}")
	List<Article>  findAllArticlesWithStatus(int i);
	
	@Update("UPDATE cms_article SET hits=hits+1 WHERE id=#{id}")
	int updHits(String substring);

	@Update("UPDATE cms_article SET hits=hits+1 WHERE id=#{id}")
	int updaHits(Article article);
	
	/**
	 *  查询收藏夹
	 * @param favorite
	 * @return
	 */
	@Select("select * from cms_favorite ORDER BY created desc")
	List<Favorite> listfavorite(Favorite favorite);
	
	/**
	 *  添加收藏夹
	 * @param favorite
	 * @return
	 */
	@Insert("insert into cms_favorite values(null,#{text},#{url},0,now())")
	void addfavorite(Favorite favorite);
	
	/**
	 *  删除收藏夹
	 * @param favorite
	 * @return
	 */
	@Delete("delete from cms_favorite where id=#{id}")
	void delfavorite(@Param("id")int id);
	
}
