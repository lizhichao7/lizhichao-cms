package com.lizhichao.cms;

import org.junit.Assert;

import com.lizhichao.StringUtil;

public class TestStringUtil {
	//验证对的地址
	@org.junit.Test
	public void isHttpUrltrue() {
		String url = "http://www.baidu.com";
		Assert.assertTrue("true", StringUtil.isHttpUrl(url));
	}
	
	//验证错的地址
	@org.junit.Test
	public void isHttpUrlfalse() {
		String url = "htt://www.baidu.com";
		Assert.assertTrue("true", StringUtil.isHttpUrl(url));
	}
}
