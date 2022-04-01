package com.meiming.crm.workbench.web.controller;

import com.meiming.crm.commons.contants.Contants;
import com.meiming.crm.commons.domain.ReturnObject;
import com.meiming.crm.commons.utils.DateUtils;
import com.meiming.crm.commons.utils.UUIDUtils;
import com.meiming.crm.settings.domain.DicValue;
import com.meiming.crm.settings.domain.User;
import com.meiming.crm.settings.mapper.DicValueMapper;
import com.meiming.crm.settings.service.DicValueService;
import com.meiming.crm.settings.service.UserService;
import com.meiming.crm.workbench.domain.Activity;
import com.meiming.crm.workbench.domain.Clue;
import com.meiming.crm.workbench.domain.ClueActivityRelation;
import com.meiming.crm.workbench.service.ActivityService;
import com.meiming.crm.workbench.service.ClueActivityRelationService;
import com.meiming.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;


    //跳转线索页面
    @RequestMapping("/workbench/clue/index.do")
    public String index(Model model){
        List<User> userList=userService.queryAllUsers();
        List<DicValue> appellationList=dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> clueStateList=dicValueService.queryDicValueByTypeCode("clueState");

        model.addAttribute("userList",userList);
        model.addAttribute("appellationList",appellationList);
        model.addAttribute("sourceList",sourceList);
        model.addAttribute("clueStateList",clueStateList);
        return "workbench/clue/index";

    }

    //保存线索          workbench/clue/saveCreateClue.do
    @RequestMapping("/workbench/clue/saveCreateClue.do")
    public @ResponseBody Object saveCreateClue(Clue clue, HttpSession session){

        User user=(User)session.getAttribute(Contants.SESSION_USER);

        clue.setId(UUIDUtils.getUUID());
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DateUtils.formatDateTime(new Date()));

        ReturnObject returnObject=new ReturnObject();

        int ret=clueService.saveCreateClue(clue);

        if(ret>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }
        return returnObject;
    }

    //根据id去查看详情

    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id,Model model){

        Clue clue=clueService.queryClueForDetailById(id);

        List<Activity> activityList=activityService.queryActivityForDetailByClueId(id);

        model.addAttribute("clue",clue);
        model.addAttribute("activityList",activityList);

        return "workbench/clue/detail";
    }

    //查询与该线索不关联的市场
    @RequestMapping("/workbench/clue/searchActivityNoBoundById.do")
    public @ResponseBody Object searchActivityNoBoundById(String activityName,String clueId){
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);

        List<Activity> activityList=activityService.searchActivityNoBoundById(map);


        return activityList;
    }

    //关联市场活动
    @RequestMapping("/workbench/clue/saveBoundActivity.do")
    public @ResponseBody Object saveBoundActivity(String[] activityId,String clueId) {
        ClueActivityRelation relation=null;

        List<ClueActivityRelation> relationList=new ArrayList<>();

        //clueId 1 activity 3,
        for(String ai:activityId){
            relation=new ClueActivityRelation();
            relation.setId(UUIDUtils.getUUID());
            relation.setClueId(clueId);
            relation.setActivityId(ai);

            relationList.add(relation);
        }



        ReturnObject returnObject=new ReturnObject();

        int ret=clueActivityRelationService.saveCeateClueActivityRelationByList(relationList);

        if(ret>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            //将添加的市场活动追加显示到市场活动页中
            List<Activity> activityList=activityService.queryActivityForDetailByIds(activityId);
            returnObject.setRetData(activityList);
        }
        return returnObject;
    }



    //解除关联市场活动
    @RequestMapping("/workbench/clue/saveUnboundActivity.do")
    public @ResponseBody Object saveUnboundActivity(ClueActivityRelation relation) {
        ReturnObject returnObject=new ReturnObject();
        int ret=clueActivityRelationService.deleteClueActivityRelationByCLueIdActivityId(relation);

        if(ret>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

        }
        return returnObject;
    }

    //跳到转换线索页面
    @RequestMapping("/workbench/clue/convertClue.do")
    public String converClue(String id,Model model){
        Clue clue=clueService.queryClueForDetailById(id);

        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");

        model.addAttribute("clue",clue);
        model.addAttribute("stageList",stageList);

        return "workbench/clue/convert";

    }


    @RequestMapping("/workbench/clue/saveConvertClue.do")
    public @ResponseBody Object saveConvertClue(String clueId,String isCreateTran,String amountOfMoney,String tradeName,String expectedClosingDate,String stage,String activityId,HttpSession session){
         //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("clueId", clueId);
        map.put("isCreateTran", isCreateTran);
        map.put("amountOfMoney", amountOfMoney);
        map.put("tradeName", tradeName);
        map.put("expectedClosingDate", expectedClosingDate);
        map.put("stage", stage);
        map.put("activityId", activityId);
        map.put("sessionUser", session.getAttribute(Contants.SESSION_USER));
        ReturnObject returnObject=new ReturnObject();

        clueService.saveConvert(map);
        returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

        return returnObject;
    }



}
