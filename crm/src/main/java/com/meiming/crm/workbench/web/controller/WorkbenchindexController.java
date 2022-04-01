package com.meiming.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class WorkbenchindexController {

    @RequestMapping("/workbench/index.do")
    public String index(){
        return "workbench/index";
    }
}
