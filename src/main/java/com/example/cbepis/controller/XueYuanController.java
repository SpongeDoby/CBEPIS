package com.example.cbepis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cbepis.entity.News;
import com.example.cbepis.entity.XueYuan;
import com.example.cbepis.service.XueYuanService;
import com.example.cbepis.vo.NewsVo;
import com.example.cbepis.vo.ViewData;
import com.example.cbepis.vo.XueYuanVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/xueyuan")
public class XueYuanController {
    @Autowired
    private XueYuanService xueYuanService;

    @RequestMapping("/toXueYuan")
    public String toXueYuan(){
        return "xueyuan/xueyuan";
    }

    @RequestMapping("/listXueYuan")
    @ResponseBody
    public ViewData listNews(XueYuanVo xueYuanVo){
        IPage<XueYuan> page=new Page<>(xueYuanVo.getPage(),xueYuanVo.getLimit());
        QueryWrapper<XueYuan> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(xueYuanVo.getName()),"title",xueYuanVo.getName());
        IPage<XueYuan> page1 = xueYuanService.page(page, queryWrapper);
        return new ViewData(page1.getTotal(),page1.getRecords());
    }

    @RequestMapping("/deleteById")
    @ResponseBody
    public ViewData deleteById(Integer id){
        ViewData viewData=new ViewData();
        if(xueYuanService.removeById(id)){
            viewData.setMsg("数据删除成功");
            viewData.setCode(200);
            return viewData;
        }else{
            viewData.setCode(100);
            viewData.setMsg("数据删除失败");
            return viewData;
        }
    }

    @RequestMapping("/addOrUpdateXueYuan")
    @ResponseBody
    public ViewData addOrUpdateNews(XueYuan xueYuan){
        ViewData viewData=new ViewData();
        if(xueYuanService.saveOrUpdate(xueYuan)){
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
