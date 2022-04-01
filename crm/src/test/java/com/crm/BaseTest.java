package com.crm;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.transform.Source;

//spring与junit整合
@RunWith(SpringJUnit4ClassRunner.class)
//要spring,spring配置文件
@ContextConfiguration({"classpath:applicationContext.xml"})
public class BaseTest {


}
