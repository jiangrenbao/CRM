package com.meiming.crm.commons.domain;
//统一返回结果对象
public class ReturnObject {

    private String code; //1成功, 0失败
    private String message; //描述信息
    private Object retData; //返回的数据 user,teacher ,userList

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}
