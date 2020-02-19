package com.lizhichao.cms.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.listener.MessageListener;

import com.alibaba.fastjson.JSON;
import com.lizhichao.cms.bean.Article;
import com.lizhichao.cms.mapper.ArticleRep;
import com.lizhichao.cms.service.ArticleService;

public class ArticleListener implements MessageListener<String, String>{
	
	@Autowired
	ArticleService articleService;
	
	@Autowired
	RedisTemplate redisTemplate;
	
	@Autowired
	ArticleRep articleRep;
	
	
	@Override
	public void onMessage(ConsumerRecord<String, String> data) {
		// TODO Auto-generated method stub
		//接收消息
		String value = data.value();
		System.err.println("收到了消息！"+value);
		//爬json类型的串  转成article对象
		if (value.startsWith("add")) {
			
			int indexOf = value.indexOf("=");
			Article parseObject = JSON.parseObject(value.substring(indexOf+1), Article.class);

			// 把审核通过的文章存到redis数据库
			redisTemplate.opsForList().leftPush("articles", parseObject);
			System.err.println("收到消息================");
			// 把审核通过的Id删除es仓库数据
			articleRep.save(parseObject);
		}else if(value.startsWith("del")){
			int indexOf = value.indexOf("=");
			articleRep.deleteById(Integer.parseInt(value.substring(indexOf+1)));
		}else{
			Article parseObject = JSON.parseObject(value, Article.class);
			
			articleService.add(parseObject);
		}
		
		
	}
	
}
