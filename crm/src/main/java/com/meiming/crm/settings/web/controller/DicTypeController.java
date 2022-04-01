package com.meiming.crm.settings.web.controller;

import com.meiming.crm.commons.contants.Contants;
import com.meiming.crm.commons.domain.ReturnObject;
import com.meiming.crm.settings.domain.DicType;
import com.meiming.crm.settings.service.DicTypeServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DicTypeController {

    @Autowired
    private DicTypeServcie dicTypeServcie;

    @RequestMapping("/settings/dictionary/type/index.do")
    public String index(Model model){ //modelandview model view request
        List<DicType> dicTypeList=dicTypeServcie.queryAllDicTypes();
        model.addAttribute("dicTypeList", dicTypeList);

        return "settings/dictionary/type/index";
    }

    //跳转到save.jsp
    @RequestMapping("/settings/dictionary/type/toSave.do")
    public String toSave(){
        return "settings/dictionary/type/save";
    }

    @RequestMapping("/settings/dictionary/type/checkCode.do")
    public @ResponseBody Object checkCode(String code){
        DicType dicType=dicTypeServcie.queryDicTypeByCode(code);
        //返回对象
        ReturnObject returnObject=new ReturnObject();
        if(dicType==null){
            //不重复
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            //code已经有了
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("编码已经存在");
        }

        return returnObject;
    }


    @RequestMapping("/settings/dictionary/type/saveCreateDicType.do")
    public @ResponseBody Object saveCreateDicType(DicType dicType){

        ReturnObject returnObject=new ReturnObject();

        int ret=dicTypeServcie.saveCreateDicType(dicType);
        if(ret>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存失败");
        }

        return returnObject;
    }

    //跳转到编辑页面
    @RequestMapping("/settings/dictionary/type/editDicType.do")
    public String editDicType(String code,Model model){
        DicType dicType=dicTypeServcie.queryDicTypeByCode(code);

        model.addAttribute("dicType", dicType);
        return "settings/dictionary/type/edit";
    }


    @RequestMapping("/settings/dictionary/type/saveEditDicType.do")
    public @ResponseBody Object saveEditDicType(DicType dicType){

        ReturnObject returnObject=new ReturnObject();

        int ret=dicTypeServcie.saveEditDicType(dicType);
        if(ret>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("更新失败");
        }

        return returnObject;
    }


    @RequestMapping("/settings/dictionary/type/deleteDicTypeByCodes.do")
    public @ResponseBody Object deleteDicTypeByCodes(String[] code){

        ReturnObject returnObject=new ReturnObject();

        int ret=dicTypeServcie.deleteDicTypeByCodes(code);
        if(ret>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败");
        }

        return returnObject;
    }
}
