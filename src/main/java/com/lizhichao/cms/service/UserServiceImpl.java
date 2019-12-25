package com.lizhichao.cms.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lizhichao.cms.bean.User;
import com.lizhichao.cms.common.CmsUtils;
import com.lizhichao.cms.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;
	
	
	@Override
	public User getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return userMapper.findUserByName(username);
	}

	@Override
	public int register(@Valid User user) {
		// TODO Auto-generated method stub
		String encryPwd = CmsUtils.encry(user.getPassword(), user.getUsername());
		
		user.setPassword(encryPwd);
		return userMapper.add(user);
	}

	@Override
	public User login(User user) {
		// TODO Auto-generated method stub
		user.setPassword(CmsUtils.encry(user.getPassword(), user.getUsername()));
		User loginUser = userMapper.findByPwd(user);
		return loginUser;
	}

}
