package com.lizhichao.cms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.lizhichao.cms.bean.Slide;

public interface SlideMapper {
	@Select("SELECT id,title,picture,url FROM cms_slide ORDER BY id ")
	List<Slide> list();
}
