package com.example.cbepis.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cbepis.apiutils.CovidRiskZoneApi;
import com.example.cbepis.entity.CommunityDetail;
import com.example.cbepis.entity.Communitys;
import com.example.cbepis.service.CommunityDetailService;
import com.example.cbepis.service.CommunitysService;
import com.example.cbepis.vo.CommunitysVo;
import com.example.cbepis.vo.ViewData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class CovidRiskZoneController {
    @Autowired
    private CommunitysService communitysService;
    @Autowired
    private CommunityDetailService communityDetailService;

    @RequestMapping("/zone/toZone")
    public String toZone(){
        return "zone/zone";
    }

    @RequestMapping("/zone/searchRiskZone")
    @ResponseBody
    public ViewData searchCovidRiskZone(CommunitysVo communitysVo){
        IPage<Communitys> page=new Page<>(communitysVo.getPage(),communitysVo.getLimit());
        QueryWrapper<Communitys> queryWrapper=new QueryWrapper<>();
        if(StringUtils.isNotBlank(communitysVo.getCounty())){
            queryWrapper.like("area_name",communitysVo.getCounty());
        }else if(StringUtils.isNotBlank(communitysVo.getCity())){
            queryWrapper.like("area_name",communitysVo.getCity());
        }else{
            queryWrapper.like(StringUtils.isNotBlank(communitysVo.getProvince()),"area_name",communitysVo.getProvince());
        }
        IPage<Communitys> page1 = communitysService.page(page, queryWrapper);
        return new ViewData(page1.getTotal(),page1.getRecords());
    }

    @RequestMapping("/zone/initDetailByAreaName")
    @ResponseBody
    public ViewData getDetailByAreaName(String areaName){
        System.out.println("---------------------"+areaName);
        QueryWrapper<CommunityDetail> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("area_name",areaName);
        List<CommunityDetail> list = communityDetailService.list(queryWrapper);
        ViewData viewData=new ViewData(list);
        return viewData;
    }

/*
接口失效前代码,可替换为有效接口
 */
//    @RequestMapping("/zone/searchRiskZone")
//    @ResponseBody
//    public ViewData searchCovidRiskZoneData(CommunitysVo communitysVo,String province, String city, String county) throws IOException {
//        if(StringUtils.isEmpty(province)){
//            province="广东省";
//        }
//        //1、解析接口数据
//        String riskZoneData= CovidRiskZoneApi.getCovidRiskZoneData(province,city,county);
//        //2、拿到社区的数据封装list集合
//        List<Communitys> list=new ArrayList<>();
//        //fastjson解析实体
//        if(StringUtils.isNotEmpty(riskZoneData)){
//            JSONObject jsonObject = JSONObject.parseObject(riskZoneData);
//            JSONObject data = jsonObject.getJSONObject("data");
//            Integer high_count = data.getInteger("high_count");
//            String end_update_time = (String) data.get("end_update_time");
//            JSONArray high_list = data.getJSONArray("high_list");
//            for (int i = 0; i < high_list.size(); i++) {
//                JSONObject high_listJSONObject = high_list.getJSONObject(i);
//                String area_name=(String) high_list.getJSONObject(i).get("area_name");
//                JSONArray communitys = high_listJSONObject.getJSONArray("communitys");
//                for (int j = 0; j < communitys.size(); j++) {
//                    String s=communitys.get(j).toString();
//                    Communitys community = new Communitys();
//                    community.setCommunity(s);
//                    community.setEnd_update_time(end_update_time);
//                    community.setCount(high_count);
//                    community.setArea_name(area_name);
//                    list.add(community);
//                }
//            }
//            //接口没数据了，造一个展示一下
//            Communitys community=new Communitys();
//            community.setCommunity("天河区");
//            community.setEnd_update_time("2022-12-07 06:00:00");
//            community.setCount(51);
//            community.setArea_name("广东省 广州市");
//            list.add(community);
////            IPage<Communitys> iPage=new Page<Communitys>(communitysVo.getPage(),communitysVo.getLimit());
////            IPage<Communitys> page = communitysService.page(iPage);
////            page.setRecords(list);
////            page.setTotal(list.size());
//            return new ViewData(list);
//        }
//        return null;
//    }
}
