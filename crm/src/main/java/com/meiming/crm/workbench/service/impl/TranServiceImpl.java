package com.meiming.crm.workbench.service.impl;

import com.meiming.crm.commons.utils.DateUtils;
import com.meiming.crm.commons.utils.UUIDUtils;
import com.meiming.crm.settings.domain.User;
import com.meiming.crm.workbench.domain.Customer;
import com.meiming.crm.workbench.domain.FunnelVO;
import com.meiming.crm.workbench.domain.Tran;
import com.meiming.crm.workbench.domain.TranHistory;
import com.meiming.crm.workbench.mapper.CustomerMapper;
import com.meiming.crm.workbench.mapper.TranHistoryMapper;
import com.meiming.crm.workbench.mapper.TranMapper;
import com.meiming.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TranServiceImpl implements TranService {


    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private   TranMapper tranMapper;


    @Autowired
    private   TranHistoryMapper tranHistoryMapper;


    @Override
    public int saveCreateTran(Map<String, Object> map) {

        Tran tran=(Tran)map.get("tran");
        String customerId=(String)tran.getCustomerId();
        String customerName=(String)map.get("customerName");
        User user=(User)map.get("sessionUser");

        //是否需要创建新客户
        if(customerId==null||customerId.trim().length()==0){
            //创建客户,在客户表插入客户记录
            Customer customer=new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setOwner(user.getId());
            customer.setName(customerName);

            customerMapper.insertCustomer(customer);

            //将新建customer对象的customerId赋予给tran的customerId
            tran.setCustomerId(customer.getId());
        }
        //保存交易
        tranMapper.insertTran(tran);

        //每次创建交易时要创建它的交易历史记录
        TranHistory tranHistory=new TranHistory();
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateTime(DateUtils.formatDateTime(new Date()));
        tranHistory.setStage(tran.getStage());
        tranHistory.setCreateBy(user.getId());
        tranHistory.setTranId(tran.getId());

        tranHistoryMapper.insertTranHistory(tranHistory);

        return 0;
    }

    @Override
    public Tran queryTranForDetailById(String id) {
        return tranMapper.selectTranForDetailById(id);
    }

    @Override
    public List<FunnelVO> queryCountOfTranGroupByStage() {
        return tranMapper.selectCountOfTranGroupByStage();
    }


}
