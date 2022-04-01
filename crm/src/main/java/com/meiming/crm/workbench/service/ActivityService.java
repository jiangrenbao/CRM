package com.meiming.crm.workbench.service;

import com.meiming.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    //保存
    int saveCreateActivity(Activity activity);
    //多条件及分页查询
    List<Activity> queryActivityForPagesByCondition(Map<String, Object> map);
    //记录总数
    long queryCountOfActivityByCondition(Map<String, Object> map);
    //id查询
    Activity queryActivityById(String id);
    //更新
    int saveEditActivity(Activity activity);
    //删除
    int deleteActivityByIds(String[] ids);
    List<Activity> queryActivityForDetailByIds(String[] ids);
    //批量保存
    int saveCreateActivityByList(List<Activity> activityList);
    //id查询市场活动详情
    Activity queryActivityForDetailById(String id);
    //线索模块中有个根据市场活动名查询所有市场活动
    List<Activity> queryActivityForDetailByName(String name);
     //查询所有的市场活动
    List<Activity> queryAllActivityForDetail();

    List<Activity> queryActivityForDetailByClueId(String clueId);

    List<Activity> searchActivityNoBoundById(Map<String, Object> map);
}
