package com.meiming.crm.workbench.service.impl;

import com.meiming.crm.commons.contants.Contants;
import com.meiming.crm.commons.utils.DateUtils;
import com.meiming.crm.commons.utils.UUIDUtils;
import com.meiming.crm.settings.domain.User;
import com.meiming.crm.workbench.domain.*;
import com.meiming.crm.workbench.mapper.*;
import com.meiming.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CluseServiceImpl implements ClueService {

    @Autowired
    private ClueMapper clueMapper;


    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private   ClueRemarkMapper clueRemarkMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private   ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private   ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private   ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private   TranMapper tranMapper;

    @Autowired
    private   TranRemarkMapper tranRemarkMapper;






    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

 /*（1）获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
      （2）通过线索对象提取客户信息， 保存客户
（3）通过线索对象提取联系人信息，保存联系人
（4） 线索备注转换到客户备注以及联系人备注
（5）“线索和市场活动”的关系转换到“联系人和市场活动”的关系
（6）如果有创建交易需求，创建一条交易
(7)如果创建交易,要将线索备注也要转一份到交易备注
（8）删除线索备注
（9）删除线索和市场活动的关系
（10）删除线索*/

    @Override
    public void saveConvert(Map<String, Object> map) {

       //获取参数
        String clueId=(String)map.get("clueId");
        User user=(User)map.get("sessionUser");
        String isCreateTran=(String)map.get("isCreateTran");

        //（1）获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
       Clue clue= clueMapper.selectClueById(clueId);
       //(2)通过线索对象提取客户信息， 保存客户
        Customer customer=new Customer();
        customer.setId(UUIDUtils.getUUID());
        customer.setOwner(user.getId());
        //客户与公司
        customer.setName(clue.getCompany());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formatDateTime(new Date()));
        customer.setContactSummary(clue.getContactSummary());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setDescription(clue.getDescription());
        customer.setAddress(clue.getAddress());

        customerMapper.insertCustomer(customer);
        //BeanUtils
        //（3）通过线索对象提取联系人信息，保存联系人
        Contacts contacts=new Contacts();
        contacts.setId(UUIDUtils.getUUID());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullName(clue.getFullName());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formatDateTime(new Date()));
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setDescription(clue.getDescription());
        contacts.setAddress(clue.getAddress());

        contactsMapper.insertContacts(contacts);

        //(4)线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarkList=clueRemarkMapper.selectClueRemarkByClueId(clueId);
        if(clueRemarkList!=null&&clueRemarkList.size()>0){
            CustomerRemark cur=null; //客户备注
            ContactsRemark cor=null; //联系人备注

            List<CustomerRemark> curList=new ArrayList<>();
            List<ContactsRemark> corList=new ArrayList<>();

            //循环去遍历线索备注
            for(ClueRemark cr:clueRemarkList){
                //为客户备注去填写内容
                cur=new CustomerRemark();
                cur.setId(UUIDUtils.getUUID());
                cur.setNoteContent(cr.getNoteContent());
                cur.setCreateTime(cr.getCreateTime());
                cur.setCreateBy(cr.getCreateBy());
                cur.setEditBy(cr.getEditBy());
                cur.setEditTime(cr.getEditTime());
                curList.add(cur);
                //为联系人备注去填写内容
                cor=new ContactsRemark();
                cor.setId(UUIDUtils.getUUID());
                cor.setNoteContent(cr.getNoteContent());
                cor.setCreateTime(cr.getCreateTime());
                cor.setCreateBy(cr.getCreateBy());
                cor.setEditBy(cr.getEditBy());
                cor.setEditTime(cr.getEditTime());
                corList.add(cor);
            }

            //批量插入客户备注和联系人备注数据库
            customerRemarkMapper.insertCustomerRemarkByList(curList);
            contactsRemarkMapper.insertContactsRemarkByList(corList);
        }

        //（5）“线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> carList=clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);

        if(carList!=null&&carList.size()>0){
            ContactsActivityRelation coar=null;
            List<ContactsActivityRelation> coarList=new ArrayList<>();
            for(ClueActivityRelation car:carList){
                coar=new ContactsActivityRelation();
                coar.setId(UUIDUtils.getUUID());
                coar.setContactsId(contacts.getId());
                coar.setActivityId(car.getActivityId());

                coarList.add(coar);
            }

            contactsActivityRelationMapper.insertContactsActivityRelationByList(coarList);
        }

        //6.如果有创建交易需求，创建一条交易
        if("true".equals(isCreateTran)){
            Tran tran=new Tran();
            tran.setId(UUIDUtils.getUUID());
            tran.setOwner(user.getId());
            tran.setMoney((String)map.get("amountOfMoney"));
            tran.setName((String)map.get("tradeName"));
            tran.setExpectedDate((String)map.get("expectedClosingDate"));
            tran.setCustomerId(customer.getId());
            tran.setStage((String)map.get("stage"));
            tran.setActivityId((String)map.get("activityId"));

            tranMapper.insertTran(tran);

            //如果创建交易,要将线索备注也要转一份到交易备注
            if(clueRemarkList!=null&&clueRemarkList.size()>0){
                TranRemark tr=null;
                List<TranRemark> trList=new ArrayList<>();
                for(ClueRemark cr:clueRemarkList){
                    tr=new TranRemark();
                    tr.setId(UUIDUtils.getUUID());
                    tr.setNoteContent(cr.getNoteContent());
                    tr.setCreateBy(cr.getCreateBy());
                    tr.setCreateTime(cr.getCreateTime());

                    trList.add(tr);
                }

                tranRemarkMapper.insertTranRemarkByList(trList);

            }


        }

   /*     （8）删除线索备注
（9）删除线索和市场活动的关系
（10）删除线索*/

   clueRemarkMapper.deleteClueRemarkByClueId(clueId);
   clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
   clueMapper.deleteClueById(clueId);

    }

}
