package com.lizhichao.cms;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.lizhichao.FileUtillo;
import com.lizhichao.cms.bean.Article;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:producer.xml")
public class ImportArtices {
	
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
	
	@Test
	public void test() throws Exception {
		File file = new File("D:\\测试爬虫");
		File[] listFiles = file.listFiles();
		for (File file2 : listFiles) {
//			System.out.println(file2.getName());
			String title = file2.getName().replace(".txt", "");
//			System.out.println(title);
			
			//读取文章内容
			String content = FileUtillo.readFile(file2, "utf8");
//			System.out.println(content);
			
			//声明文章对象
			Article article = new Article();
			article.setTitle(title);
			article.setContent(content);
			article.setPicture("D:\\pic");
			article.setUserId(78);
			article.setChannelId(2);
			article.setCategoryId(24);
			article.setArticleType(0);
			
			String jsonString = JSON.toJSONString(article);
			/*System.out.println(article);*/
			
			
			
			kafkaTemplate.send("articles", jsonString);
		}
	}
}
