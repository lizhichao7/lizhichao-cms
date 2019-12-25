package com.lizhichao.cms;

import org.junit.Assert;

import com.lizhichao.StringUtils;

public class Test {
	
	@org.junit.Test
	public void isurltrue() {
		String url ="http://www.baidu.com";
		Assert.assertTrue("true",StringUtils.isUrl(url));
	}
	
	@org.junit.Test
	public void isurlfalse() {
		String url ="htt:/www.baidu.com";
		Assert.assertTrue("true",StringUtils.isUrl(url));
	}
}
