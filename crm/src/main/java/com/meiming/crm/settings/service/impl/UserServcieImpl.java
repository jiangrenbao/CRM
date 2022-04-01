package com.meiming.crm.settings.service.impl;

import com.meiming.crm.settings.domain.TblUser;
import com.meiming.crm.settings.domain.User;
import com.meiming.crm.settings.mapper.TblUserMapper;
import com.meiming.crm.settings.mapper.UserMapper;
import com.meiming.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServcieImpl implements UserService {

    @Autowired
    private TblUserMapper tblUserMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public TblUser selectUserById(String id) {
        return tblUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public User queryUserByLoginAndPwd(Map<String, Object> map) {
        return userMapper.selectUserByLoginActAndPwd(map);
    }

    @Override
    public List<User> queryAllUsers() {
        return userMapper.selectAllUsers();
    }
}
