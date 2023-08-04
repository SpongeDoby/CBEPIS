package com.example.cbepis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cbepis.entity.BanJi;
import com.example.cbepis.entity.HeSuan;
import com.example.cbepis.entity.User;
import com.example.cbepis.entity.XueYuan;
import com.example.cbepis.service.BanJiService;
import com.example.cbepis.service.HeSuanService;
import com.example.cbepis.service.RoleService;
import com.example.cbepis.service.XueYuanService;
import com.example.cbepis.vo.HeSuanVo;
import com.example.cbepis.vo.ViewData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/hesuan")
public class HeSuanController {
    @Autowired
    private HeSuanService heSuanService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BanJiService banJiService;
    @Autowired
    private XueYuanService xueYuanService;

    @RequestMapping("/toHeSuan")
    public String toHeSuan(HttpSession session){
        User user = (User)session.getAttribute("user");
        Integer id = user.getId();
        List<Integer> roleByUid = roleService.getRoleByUid(id);
        Integer integer = roleByUid.get(0);
        if(integer!=1){
            return "hesuan/hesuan_user";
        }
        return "hesuan/hesuan";
    }

    @RequestMapping("/loadAllHeSuan")
    @ResponseBody
    public ViewData loadAllHeSuan(HeSuanVo heSuanVo,HttpSession session){
        IPage<HeSuan> page=new Page<>(heSuanVo.getPage(),heSuanVo.getLimit());
        QueryWrapper<HeSuan> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(heSuanVo.getName()),"name",heSuanVo.getName());
        User user = (User)session.getAttribute("user");
        Integer id = user.getId();
        List<Integer> roleByUid = roleService.getRoleByUid(id);
        Integer integer = roleByUid.get(0);
        //根据不同角色展示数据
        if(integer==3){
            queryWrapper.eq("uid",user.getUid());
        }else if(integer==2){
            queryWrapper.eq("grade_id",user.getGradeId());
        }else if(integer==4){
            queryWrapper.eq("xue_yuan_id",user.getXueYuanId());
        }
        IPage<HeSuan> page1 = heSuanService.page(page, queryWrapper);
        for(HeSuan heSuan: page1.getRecords()){
            XueYuan byId = xueYuanService.getById(heSuan.getXueYuanId());
            BanJi banJi = banJiService.getById(heSuan.getBanJiId());
            heSuan.setXueYuanName(byId.getName());
            heSuan.setBanJiName(banJi.getName());
        }
        return new ViewData(page1.getTotal(),page1.getRecords());
    }

    @RequestMapping("/addHeSuan")
    @ResponseBody
    public ViewData addHeSuan(HeSuan heSuan, HttpSession session, @RequestParam("createTime1") String createTime1, @RequestParam("updateTime1") String updateTime1) throws ParseException {
        ViewData viewData=new ViewData();
        User user = (User)session.getAttribute("user");
        Integer id = user.getId();
        List<Integer> roleByUid = roleService.getRoleByUid(id);
        Integer integer = roleByUid.get(0);
        heSuan.setUid(user.getUid());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1=simpleDateFormat.parse(createTime1);
        Date date2=simpleDateFormat.parse(updateTime1);
        heSuan.setCreateTime(date1);
        heSuan.setUpdateTime(date2);
        if(heSuanService.save(heSuan)){
            viewData.setMsg("数据新增成功");
            viewData.setCode(200);
            return viewData;
        }else{
            viewData.setCode(100);
            viewData.setMsg("数据新增失败");
            return viewData;
        }
    }

    @RequestMapping("/updateHeSuan")
    @ResponseBody
    public ViewData updateHeSuan(HeSuan heSuan,@RequestParam("createTime1") String createTime1,@RequestParam("updateTime1") String updateTime1) throws ParseException {
        ViewData viewData=new ViewData();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1=simpleDateFormat.parse(createTime1);
        Date date2=simpleDateFormat.parse(updateTime1);
        heSuan.setCreateTime(date1);
        heSuan.setUpdateTime(date2);
        if(heSuanService.updateById(heSuan)){
            viewData.setMsg("数据更新成功");
            viewData.setCode(200);
            return viewData;
        }else{
            viewData.setCode(100);
            viewData.setMsg("数据更新失败");
            return viewData;
        }
    }

    @RequestMapping("/deleteHeSuan")
    @ResponseBody
    public ViewData deleteHeSuan(Integer id){
        ViewData viewData=new ViewData();
        if(heSuanService.removeById(id)){
            viewData.setMsg("数据删除成功");
            viewData.setCode(200);
            return viewData;
        }else{
            viewData.setCode(100);
            viewData.setMsg("数据删除失败");
            return viewData;
        }
    }
}
