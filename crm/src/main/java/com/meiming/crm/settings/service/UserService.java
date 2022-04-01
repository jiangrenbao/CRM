package com.meiming.crm.settings.service;

import com.meiming.crm.settings.domain.TblUser;
import com.meiming.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    //根据id获取user
    public TblUser selectUserById(String id);

    //查询用户名和密码
    User queryUserByLoginAndPwd(Map<String, Object> map);

    //查询所有的用户
    List<User> queryAllUsers();
}
