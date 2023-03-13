package com.javapandeng.service.impl;

import com.javapandeng.base.BaseMapper;
import com.javapandeng.base.BaseServiceImpl;
import com.javapandeng.mapper.UserMapper;
import com.javapandeng.po.User;
import com.javapandeng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseMapper<User> getBaseMapper() {
        return userMapper;
    }
}
