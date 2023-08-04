package com.example.cbepis.controller;

import com.example.cbepis.entity.GzConfirmedData;
import com.example.cbepis.service.GuangzhouService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class VisualDataController {
    @Autowired
    private GuangzhouService guangzhouService;

    @RequestMapping("/toGuangzhou")
    public String toGuangzhou(){
        return "guangzhou";
    }

    @RequestMapping("/toVisualData")
    public String toVisualData(){
        return "visualDataSearch";
    }

    @RequestMapping("/queryGuangzhou")
    @ResponseBody
    public List<GzConfirmedData> queryGuangzhou(){
        return guangzhouService.list();
    }
}
