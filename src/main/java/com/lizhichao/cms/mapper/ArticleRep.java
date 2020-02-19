package com.lizhichao.cms.mapper;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.lizhichao.cms.bean.Article;

public interface ArticleRep extends ElasticsearchRepository<Article, Integer>{
	//根据标题查询
	List<Article> findByTitle(String key);
}
