package com.example.cbepis.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cbepis.entity.ConfirmedData;
import com.example.cbepis.entity.DomesticTotal;

import com.example.cbepis.entity.LineTrend;
import com.example.cbepis.entity.News;
import com.example.cbepis.service.CovidNewsService;
import com.example.cbepis.service.DomesticTotalService;
import com.example.cbepis.service.IndexService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class IndexController {
    @Autowired
    private IndexService indexService;
    @Autowired
    private DomesticTotalService domesticTotalService;
    @Autowired
    private CovidNewsService covidNewsService;

    /**
     * 确诊数据动态查询-中国地图
     * 有Redis缓存
     */
    @RequestMapping("/query")
    @ResponseBody
    public List<ConfirmedData> getConfirmedDaya(){
        //Redis
        Jedis jedis=new Jedis("127.0.0.1");
        List<ConfirmedData> list=new ArrayList<>();
        if(jedis!=null){
            List<String> listRedis=jedis.lrange("ConfirmedData",0,33);
            if(listRedis.size()>0){
                for(String i: listRedis){
                    JSONObject jsonObject = JSONObject.parseObject(i);
                    Object name = jsonObject.get("name");
                    Object value = jsonObject.get("value");
                    ConfirmedData confirmedData = new ConfirmedData();
                    confirmedData.setName(String.valueOf(name));
                    confirmedData.setValue(Integer.parseInt(value.toString()));
                    list.add(confirmedData);
                }
            }else{
                IPage<ConfirmedData> page=new Page<>(1,34);
                QueryWrapper<ConfirmedData> queryWrapper=new QueryWrapper<>();
                queryWrapper.orderByDesc("id");
                IPage<ConfirmedData> page1 = indexService.page(page, queryWrapper);
                list=page1.getRecords();
                for(ConfirmedData i:list){
                    jedis.lpush("ConfirmedData", JSONObject.toJSONString(i));
                }
            }
        }
        return list;
    }

    //跳转饼状图
    @RequestMapping("/toPie")
    public String toPie(){
        return "pie";
    }
    //处理饼状图ajax
    @RequestMapping("/queryPie")
    @ResponseBody
    public List<ConfirmedData> getConfirmedDataPie(){
        IPage<ConfirmedData> page=new Page<>(1,34);
        QueryWrapper<ConfirmedData> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        IPage<ConfirmedData> page1 = indexService.page(page, queryWrapper);
        return page1.getRecords();
    }

    //跳转柱状图
    @RequestMapping("/toBar")
    public String toBar(){
        return "bar";
    }
    //柱状图ajax
    @RequestMapping("/queryBar")
    @ResponseBody
    public Map<String,List<Object>> getConfirmedDataBar(){
        IPage<ConfirmedData> page=new Page<>(1,34);
        QueryWrapper<ConfirmedData> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        IPage<ConfirmedData> page1 = indexService.page(page, queryWrapper);
        List<ConfirmedData> list=page1.getRecords();
        //省名
        List<String> citylist=new ArrayList<>();
        for(ConfirmedData i:list){
            citylist.add(i.getName());
        }
        //数据
        List<Integer> datalist=new ArrayList<>();
        for(ConfirmedData i:list){
            datalist.add(i.getValue());
        }
        Map map=new HashMap();
        map.put("citylist",citylist);
        map.put("datalist",datalist);
        return map;
    }

    //跳转折线图
    @RequestMapping("/toLine")
    public String toLine(){
        return "line";
    }
    //处理折线图ajax
    @RequestMapping("/queryLine")
    @ResponseBody
    public Map<String,List<Object>> getLineData(){
        List<LineTrend> lineTrends=indexService.getSevenDayData();
        List<Integer> confirmList=new ArrayList<>();
        for(LineTrend i:lineTrends){
            confirmList.add(i.getConfirm());
        }
        List<Integer> isolationList=new ArrayList<>();
        for(LineTrend i:lineTrends){
            isolationList.add(i.getIsolation());
        }
        List<Integer> cureList=new ArrayList<>();
        for(LineTrend i:lineTrends){
            cureList.add(i.getCure());
        }
        List<Integer> similarList=new ArrayList<>();
        for(LineTrend i:lineTrends){
            similarList.add(i.getSimilar());
        }
        List<Integer> deadList=new ArrayList<>();
        for(LineTrend i:lineTrends){
            deadList.add(i.getDead());
        }
        Map map=new HashMap();
        map.put("confirmList",confirmList);
        map.put("isolationList",isolationList);
        map.put("deadList",deadList);
        map.put("cureList",confirmList);
        map.put("similarList",similarList);
        return map;
    }

    //前台首页的统计数据、Redis缓存
    @RequestMapping("/toChina")
    public String toChina(Model model) throws ParseException {
        /**
         * Redis,有数据直接返回；无数据，查mysql，更新缓存；
         */
        Jedis jedis=new Jedis("127.0.0.1");
        if(jedis!=null){
            String confirm = jedis.get("confirm");
            String input = jedis.get("input");
            String heal = jedis.get("heal");
            String dead = jedis.get("dead");
            String updateTime = jedis.get("updateTime");
            if(StringUtils.isNotBlank(confirm)
                    && StringUtils.isNotBlank(input)
                    && StringUtils.isNotBlank(heal)
                    && StringUtils.isNotBlank(dead)
                    && StringUtils.isNotBlank(updateTime)){
                DomesticTotal domesticTotalRedis=new DomesticTotal();
                domesticTotalRedis.setConfirm(Integer.parseInt(confirm));
                domesticTotalRedis.setInput(Integer.parseInt(input));
                domesticTotalRedis.setHeal(Integer.parseInt(heal));
                domesticTotalRedis.setDead(Integer.parseInt(dead));
                DateFormat format1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date date = format1.parse(updateTime);
                DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String newDateString = format2.format(date);
                Date newDate = format2.parse(newDateString);
                domesticTotalRedis.setUpdateTime(newDate);
                model.addAttribute("lastTimeData",domesticTotalRedis);
                List<News> newsList=covidNewsService.list5News();
                model.addAttribute("newsList",newsList);
            }else{
                //按id查询最新数据
                QueryWrapper<DomesticTotal> queryWrapper=new QueryWrapper<>();
                queryWrapper.orderByDesc("id");
                List<DomesticTotal> list=domesticTotalService.list(queryWrapper);
                DomesticTotal domesticTotal=list.get(0);
                model.addAttribute("lastTimeData",domesticTotal);
                //更新缓存
                jedis.set("confirm",String.valueOf(domesticTotal.getConfirm()));
                jedis.set("input",String.valueOf(domesticTotal.getInput()));
                jedis.set("heal",String.valueOf(domesticTotal.getHeal()));
                jedis.set("dead",String.valueOf(domesticTotal.getDead()));
                jedis.set("updateTime",domesticTotal.getUpdateTime().toString());
                //播报新闻
                List<News> newsList=covidNewsService.list5News();
                model.addAttribute("newsList",newsList);
            }
        }

        return "china";
    }

    //主控页
    @RequestMapping("/")
    public String index(Model model){
        //按id查询最新数据
        QueryWrapper<DomesticTotal> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<DomesticTotal> list=domesticTotalService.list(queryWrapper);
        DomesticTotal domesticTotal=list.get(0);
        model.addAttribute("lastTimeData",domesticTotal);
        return "index";
    }

}
