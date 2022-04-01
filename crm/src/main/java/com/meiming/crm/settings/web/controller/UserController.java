package com.meiming.crm.settings.web.controller;

import com.meiming.crm.commons.contants.Contants;
import com.meiming.crm.commons.domain.ReturnObject;
import com.meiming.crm.commons.utils.DateUtils;
import com.meiming.crm.commons.utils.MD5Util;
import com.meiming.crm.settings.domain.User;
import com.meiming.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {


    @Autowired
    private UserService userService;

    // http://ip:port/
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(HttpServletRequest request) {
        // 获取cookie
        Cookie[] cookies = request.getCookies();
        String loginAct = null;
        String loginPwd = null;

        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if ("loginAct".equals(name)) {
                loginAct = cookie.getValue();
                //继续让密码
                continue;
            }
            if ("loginPwd".equals(name)) {
                loginPwd = cookie.getValue();
            }
        }

        if (loginAct != null && loginPwd != null) {
            // 去数据库再做验证
            Map<String, Object> map = new HashMap<>();
            map.put("loginAct", loginAct);
            map.put("loginPwd", MD5Util.getMD5(loginPwd));
            // 调用业务层
            User user = userService.queryUserByLoginAndPwd(map);
            request.getSession().setAttribute(Contants.SESSION_USER, user);
            return "redirect:/workbench/index.do";// 跳工作台
        } else {
            return "settings/qx/user/login";
        }
    }

    @RequestMapping("/settings/qx/user/login.do")
    //user对象->user json格式 {username:'tom', password:'123'}
    //控制层调用业务层返回对象,返回给前端json对象,转换, springmvc @ResponseBody
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpSession session, HttpServletResponse response) {
        //返回对象
        ReturnObject returnObject = new ReturnObject();
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", MD5Util.getMD5(loginPwd));

        //调用业务层
        User user = userService.queryUserByLoginAndPwd(map);

        if (user == null) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户或密码错误");
        } else if (DateUtils.formatDateTime(new Date()).compareTo(user.getExpireTime()) > 0) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("账户已经过期");
        } else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            //保存用户request,session
            session.setAttribute(Contants.SESSION_USER, user);
            //是否需要免登录功能
            if ("true".equals(isRemPwd)) {
                //实现免登录功能
                Cookie c1 = new Cookie("loginAct", loginAct);
                //设定cookie在本地保存多久
                c1.setMaxAge(10 * 24 * 60 * 60);
                //服务器将cookie保存到浏览器
                //response响应对象
                response.addCookie(c1);

                Cookie c2 = new Cookie("loginPwd", loginPwd);
                //设定cookie在本地保存多久
                c2.setMaxAge(10 * 24 * 60 * 60);
                //服务器将cookie保存到浏览器
                //response响应对象
                response.addCookie(c2);
            } else {
                //不实现免登录,将cookie清空
                Cookie c1 = new Cookie("loginAct", null);
                //设定cookie在本地保存多久
                c1.setMaxAge(0);
                //服务器将cookie保存到浏览器
                //response响应对象
                response.addCookie(c1);

                //不实现免登录,将cookie清空
                Cookie c2 = new Cookie("loginPwd", null);
                //设定cookie在本地保存多久
                c2.setMaxAge(0);
                //服务器将cookie保存到浏览器
                //response响应对象
                response.addCookie(c2);
            }


        }

        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session) {
        //清空cookie
        //不实现免登录,将cookie清空
        Cookie c1 = new Cookie("loginAct", null);
        //设定cookie在本地保存多久
        c1.setMaxAge(0);
        //服务器将cookie保存到浏览器
        //response响应对象
        response.addCookie(c1);

        //不实现免登录,将cookie清空
        Cookie c2 = new Cookie("loginPwd", null);
        //设定cookie在本地保存多久
        c2.setMaxAge(0);
        //服务器将cookie保存到浏览器
        //response响应对象
        response.addCookie(c2);

        //销毁session
        session.invalidate();

        return "redirect:/";
    }

}
