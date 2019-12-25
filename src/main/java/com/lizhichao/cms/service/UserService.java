package com.lizhichao.cms.service;

import javax.validation.Valid;

import com.lizhichao.cms.bean.User;

public interface UserService {
	User getUserByUsername(String username);
	
	int register(@Valid User user);
	
	User login(User user);
}
