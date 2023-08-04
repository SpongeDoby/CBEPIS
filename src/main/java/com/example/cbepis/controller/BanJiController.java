package com.example.cbepis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cbepis.entity.BanJi;
import com.example.cbepis.entity.News;
import com.example.cbepis.entity.XueYuan;
import com.example.cbepis.service.BanJiService;
import com.example.cbepis.service.XueYuanService;
import com.example.cbepis.vo.BanJiVo;
import com.example.cbepis.vo.ViewData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/banji")
public class BanJiController {
    @Autowired
    private BanJiService banJiService;
    @Autowired
    private XueYuanService xueYuanService;

    @RequestMapping("/toBanJi")
    public String listBanJi(){
        return "banji/banji";
    }

    @RequestMapping("/listBanJi")
    @ResponseBody
    public ViewData listNews(BanJiVo banJiVo){
        IPage<BanJi> page=new Page<>(banJiVo.getPage(),banJiVo.getLimit());
        QueryWrapper<BanJi> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(banJiVo.getName()),"title",banJiVo.getName());
        IPage<BanJi> page1 = banJiService.page(page, queryWrapper);
        for(BanJi banJi: page1.getRecords()){
            XueYuan xueYuan = xueYuanService.getById(banJi.getXueYuanId());
            banJi.setXueYuanName(xueYuan.getName());
        }
        return new ViewData(page1.getTotal(),page1.getRecords());
    }

    @RequestMapping("/deleteById")
    @ResponseBody
    public ViewData deleteById(Integer id){
        ViewData viewData=new ViewData();
        if(banJiService.removeById(id)){
            viewData.setMsg("数据删除成功");
            viewData.setCode(200);
            return viewData;
        }else{
            viewData.setCode(100);
            viewData.setMsg("数据删除失败");
            return viewData;
        }
    }

    @RequestMapping("/addBanJi")
    @ResponseBody
    public ViewData addBanJi(BanJi banJi){
        ViewData viewData=new ViewData();
        if(banJiService.save(banJi)){
            viewData.setMsg("数据更新成功");
            viewData.setCode(200);
            return viewData;
        }else{
            viewData.setCode(100);
            viewData.setMsg("数据更新失败");
            return viewData;
        }
    }

    @RequestMapping("/updateBanJi")
    @ResponseBody
    public ViewData UpdateBanJi(BanJi banJi){
        ViewData viewData=new ViewData();
        if(banJiService.updateById(banJi)){
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
