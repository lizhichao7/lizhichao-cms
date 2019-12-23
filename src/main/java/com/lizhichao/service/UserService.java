package com.lizhichao.service;

import javax.validation.Valid;

import com.lizhichao.bean.User;

public interface UserService {
	User getUserByUsername(String username);
	
	int register(@Valid User user);
	
	User login(User user);
}
