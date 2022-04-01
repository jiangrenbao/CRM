package com.meiming.crm.workbench.web.controller;

import com.meiming.crm.commons.contants.Contants;
import com.meiming.crm.commons.domain.ReturnObject;
import com.meiming.crm.commons.utils.DateUtils;
import com.meiming.crm.commons.utils.UUIDUtils;
import com.meiming.crm.settings.domain.DicValue;
import com.meiming.crm.settings.domain.User;
import com.meiming.crm.settings.service.DicValueService;
import com.meiming.crm.settings.service.UserService;
import com.meiming.crm.workbench.domain.Customer;
import com.meiming.crm.workbench.domain.Tran;
import com.meiming.crm.workbench.domain.TranHistory;
import com.meiming.crm.workbench.domain.TranRemark;
import com.meiming.crm.workbench.service.CustomerService;
import com.meiming.crm.workbench.service.TranHistoryService;
import com.meiming.crm.workbench.service.TranRemarkService;
import com.meiming.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class TranController {
    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private CustomerService customerService;


    @Autowired
    private TranService tranService;

    @Autowired
    private TranRemarkService tranRemarkService;


    @Autowired
    private TranHistoryService tranHistoryService;

    @RequestMapping("/workbench/transaction/typeahead.do")
    public @ResponseBody Object typeahead(String customerName){

        //模拟客户数据
        List<Customer> customerList=new ArrayList<>();

        Customer customer=new Customer();
        customer.setId("1001");
        customer.setName("字节跳动");
        customerList.add(customer);

        customer=new Customer();
        customer.setId("1002");
        customer.setName("动力字节");
        customerList.add(customer);


        return customerList;
    }

    @RequestMapping("/workbench/transaction/index.do")
    public String index(Model model) {
        //调用service层方法，查询动态数据
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList=dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
        //把数据保存到request中
        model.addAttribute("stageList",stageList);
        model.addAttribute("transactionTypeList",transactionTypeList);
        model.addAttribute("sourceList",sourceList);
        return  "workbench/transaction/index";
    }


    @RequestMapping("/workbench/transaction/createTran.do")
    public String createTran(Model model) {

        List<User> userList=userService.queryAllUsers();
        //调用service层方法，查询动态数据
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList=dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
        //把数据保存到request中
        model.addAttribute("stageList",stageList);
        model.addAttribute("transactionTypeList",transactionTypeList);
        model.addAttribute("sourceList",sourceList);
        model.addAttribute("userList",userList);
        return  "workbench/transaction/save";
    }

    @RequestMapping("/workbench/transaction/queryCustomerByName.do")
    public @ResponseBody Object queryCustomerByName(String customerName){
        List<Customer> customerList=customerService.queryCustomerByName(customerName);
        return customerList;
    }

    @RequestMapping("/workbench/transaction/getPossibilityByStageValue.do")
    public @ResponseBody Object getPossibilityByStageValue(String stageValue){
        ResourceBundle bundle=ResourceBundle.getBundle("possibility");//classpath resources 只给前缀名
        String possibility=bundle.getString(stageValue);
        return possibility;
    }
    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    public @ResponseBody Object saveCreateTran(Tran tran, String customerName, HttpSession session){

        User user=(User)session.getAttribute(Contants.SESSION_USER);
        tran.setId(UUIDUtils.getUUID());
        tran.setCreateBy(user.getId());
        tran.setCreateTime(DateUtils.formatDateTime(new Date()));

        Map<String,Object> map=new HashMap<>();

        map.put("tran",tran);
        map.put("customerName",customerName);
        map.put("sessionUser",user);

        ReturnObject returnObject=new ReturnObject();

        tranService.saveCreateTran(map);

        returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);


        return returnObject;
    }

    //通过交易Id去查询交易详情
    @RequestMapping("workbench/transaction/detailTran.do")
    public String detailTran(String id,Model model){

        //交易详情
        Tran tran=tranService.queryTranForDetailById(id);
        //交易的备注
        List<TranRemark> remarkList=tranRemarkService.queryTranRemarkForDetailByTranId(id);
        //交易的历史
        List<TranHistory> tranHistoryList=tranHistoryService.queryTranHistoryForDetailByTranId(id);

        ResourceBundle bundle=ResourceBundle.getBundle("possibility");
        String possibility=bundle.getString(tran.getStage()); //10
        tran.setPossibility(possibility);

        //查找所有的阶段
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");


        model.addAttribute("tran",tran);
        model.addAttribute("remarkList", remarkList);
        model.addAttribute("tranHistoryList",tranHistoryList);
        model.addAttribute("stageList",stageList);

        //获取失败之前最后一个成功解读orderNo
        TranHistory tranHistory=null;
        //int i=2;i>=0;i--
        for(int i=tranHistoryList.size()-1;i>=0;i--){
            tranHistory=tranHistoryList.get(i);
            break;
        }
        model.addAttribute("theOrderNo", tranHistory.getOrderNo());

        return "workbench/transaction/detail";
    }


}
