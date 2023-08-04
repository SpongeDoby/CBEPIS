package com.example.cbepis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cbepis.entity.News;
import com.example.cbepis.service.CovidNewsService;
import com.example.cbepis.vo.NewsVo;
import com.example.cbepis.vo.ViewData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.text.View;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private CovidNewsService covidNewsService;

    @RequestMapping("/toNews")
    public String toNews(){
        return "news/news";
    }

    @RequestMapping("/listNews")
    @ResponseBody
    public ViewData listNews(NewsVo newsVo){
        IPage<News> page=new Page<>(newsVo.getPage(),newsVo.getLimit());
        QueryWrapper<News> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(newsVo.getTitle()),"title",newsVo.getTitle());
        IPage<News> page1 = covidNewsService.page(page, queryWrapper);
        return new ViewData(page1.getTotal(),page1.getRecords());
    }

    @RequestMapping("/deleteById")
    @ResponseBody
    public ViewData deleteById(Integer id){
        ViewData viewData=new ViewData();
        if(covidNewsService.removeById(id)){
            viewData.setMsg("数据删除成功");
            viewData.setCode(200);
            return viewData;
        }else{
            viewData.setCode(100);
            viewData.setMsg("数据删除失败");
            return viewData;
        }
    }

    @RequestMapping("/addOrUpdateNews")
    @ResponseBody
    public ViewData addOrUpdateNews(News news,@RequestParam("createTime1") String createTime1) throws ParseException {
        ViewData viewData=new ViewData();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createTime=simpleDateFormat.parse(createTime1);
        news.setCreateTime(createTime);
        if(covidNewsService.saveOrUpdate(news)){
            viewData.setMsg("数据更新成功");
            viewData.setCode(200);
            return viewData;
        }else{
            viewData.setCode(100);
            viewData.setMsg("数据更新失败");
            return viewData;
        }
    }
}
