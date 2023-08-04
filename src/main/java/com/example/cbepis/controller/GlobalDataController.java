package com.example.cbepis.controller;

import com.example.cbepis.entity.GlobalData;
import com.example.cbepis.service.GlobalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class GlobalDataController {
    @Autowired
    private GlobalDataService globalDataService;

    //跳转世界图
    @RequestMapping("/toGlobal")
    public String toGlobal(){
        return "global";
    }
    //世界地图ajax
    @ResponseBody
    @RequestMapping("/queryGlobal")
    public List<GlobalData> getGlobalData(){
        return globalDataService.list();
    }

    //国内疫情趋势图
    @RequestMapping("/dataTrend")
    public String toDataTrend(){
        return "data_trend";
    }
}
