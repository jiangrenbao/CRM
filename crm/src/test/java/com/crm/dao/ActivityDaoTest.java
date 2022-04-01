package com.crm.dao;

import com.meiming.crm.workbench.domain.Activity;
import com.meiming.crm.workbench.mapper.ActivityMapper;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class ActivityDaoTest extends BaseTest {

    @Autowired
    private ActivityMapper activityMapper;

    @Test
    public void estSelectActivityForPageByCondition(){
        HashMap map=new HashMap();
        map.put("name","1");
        map.put("beginNo",0);
        map.put("pageSize",2);

        List<Activity> activityList=activityMapper.selectActivityForPageByCondition(map);
        System.out.println(activityList.size());
    }


    @Test
    public void selectCountOfActivityByCondition(){
        HashMap map=new HashMap();
        long count=activityMapper.selectCountOfActivityByCondition(map);
        System.out.println(count);
    }


    @Test
    public void searchActivityNoBoundById(){
        HashMap map=new HashMap();
        map.put("activityName","2");
        map.put("clueId","01b41229673949f7a8196215332aaa70");

        List<Activity> activityList=activityMapper.searchActivityNoBoundById(map);
        System.out.println(activityList.size());
    }
}
