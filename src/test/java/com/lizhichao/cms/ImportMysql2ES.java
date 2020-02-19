package com.lizhichao.cms;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lizhichao.cms.bean.Article;
import com.lizhichao.cms.mapper.ArticleMapper;
import com.lizhichao.cms.mapper.ArticleRep;
import com.lizhichao.cms.service.ArticleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans.xml")
public class ImportMysql2ES {
	
	@Autowired
	ArticleMapper articleMapper;
	
	@Autowired
	ArticleRep articleRep;
	
	@Test
	public void importMysql2es() {
		//从mysql查询已经审核通过的所有文章
		List<Article> findAllArticlesWithStatus = articleMapper.findAllArticlesWithStatus(1);
		//查询出来的文章保存到es的索引库
		articleRep.saveAll(findAllArticlesWithStatus);
	}
	
	@Test
	public void selectContent() {
		String key ="文章";
		List<Article> findByContent = articleRep.findByTitle(key);
		for (Article article : findByContent) {
			System.out.println(article);
		}
	}
	
	
}
