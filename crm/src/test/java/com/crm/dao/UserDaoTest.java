package com.crm.dao;

import com.meiming.crm.settings.domain.TblUser;
import com.meiming.crm.settings.domain.User;
import com.meiming.crm.settings.mapper.TblUserMapper;
import com.meiming.crm.settings.mapper.UserMapper;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class UserDaoTest extends BaseTest {

    //注入mapper接口
    @Autowired
    private TblUserMapper tblUserMapper;

    @Autowired
    private UserMapper userMapper;


    //按id主键查询user
    @Test
    public void testSelectUserById(){
        TblUser tblUser=tblUserMapper.selectByPrimaryKey("06f5fc056eac41558a964f96daa7f27c");
        System.out.println(tblUser.getName());
    };

    //按id主键查询user
    @Test
    public void testSelectUserByLoginActAndPwd(){
        HashMap map=new HashMap();
        map.put("loginAct","ls");
        map.put("loginPwd","44ba5ca65651b4f36f1927576dd35436");

        User user=userMapper.selectUserByLoginActAndPwd(map);
        System.out.println(user.getName());
    };



    //查询所有的用户
    @Test
    public void testSelectAllUsers(){
        List<User> userList=userMapper.selectAllUsers();
        System.out.println(userList.size());
    };
}
