package com.crm.service;

import com.meiming.crm.settings.domain.TblUser;
import com.meiming.crm.settings.domain.User;
import com.meiming.crm.settings.service.UserService;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class UserServiceTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Test
    public void testSelectUserById() {
        TblUser tblUser = userService.selectUserById("06f5fc056eac41558a964f96daa7f27c");
        System.out.println(tblUser.getName());
    }

    @Test
    public void testqueryUserByLoginAndPwd() {
        HashMap map = new HashMap();
        map.put("loginAct", "ls");
        map.put("loginPwd", "44ba5ca65651b4f36f1927576dd35436");

        User user = userService.queryUserByLoginAndPwd(map);
        System.out.println(user.getName());
    }
}
