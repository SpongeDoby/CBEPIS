package com.example.cbepis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 处理页面跳转
 */
@Controller
public class ApiController {
    //进入疫情数据管理页面
    @RequestMapping("/toCDM")
    public String toConfirmedDataManage(){
        return "/DataManage/ConfirmedDataManage";
    }
}
