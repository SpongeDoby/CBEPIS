package com.example.cbepis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cbepis.entity.BanJi;
import com.example.cbepis.entity.HealthClockIn;
import com.example.cbepis.entity.User;
import com.example.cbepis.entity.XueYuan;
import com.example.cbepis.service.BanJiService;
import com.example.cbepis.service.HealthClockInService;
import com.example.cbepis.service.RoleService;
import com.example.cbepis.service.XueYuanService;
import com.example.cbepis.vo.HealthClockInVo;
import com.example.cbepis.vo.ViewData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class HealthClockInController {
    @Autowired
    private HealthClockInService healthClockInService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BanJiService banJiService;
    @Autowired
    private XueYuanService xueYuanService;

    @RequestMapping("/toHealathClockIn")
    public String toHealthClockIn(HttpSession session){
        User user = (User)session.getAttribute("user");
        Integer id = user.getId();
        List<Integer> roleByUid = roleService.getRoleByUid(id);
        Integer integer = roleByUid.get(0);
        if(integer!=1){
            return "/DataManage/healthClockIn_user";
        }else{
            return "/DataManage/healthClockIn";
        }
    }

    @RequestMapping("/listHealthClockIn")
    @ResponseBody
    public ViewData listHealthClockIn(HealthClockInVo healthClockInVo,HttpSession session){
        IPage<HealthClockIn> page=new Page<>(healthClockInVo.getPage(),healthClockInVo.getLimit());
        QueryWrapper<HealthClockIn> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(healthClockInVo.getUsername()!=null,"username",healthClockInVo.getUsername());
        queryWrapper.eq(healthClockInVo.getUid()!=null,"uid",healthClockInVo.getUid());
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
        IPage<HealthClockIn> page1 = healthClockInService.page(page, queryWrapper);
        for(HealthClockIn healthClockIn:page1.getRecords()){
            XueYuan byId = xueYuanService.getById(healthClockIn.getXueYuanId());
            healthClockIn.setXueYuanName(byId.getName());
            BanJi banJi = banJiService.getById(healthClockIn.getBanJiId());
            healthClockIn.setBanJiName(banJi.getName());
        }
        ViewData viewData = new ViewData();
        viewData.setCount(page1.getTotal());
        viewData.setData(page1.getRecords());
        return viewData;
    }

    @RequestMapping("/addHealthClockIn")
    @ResponseBody
    public ViewData addHealthClockIn(HealthClockIn healthClockIn, HttpSession session){
        User user =(User) session.getAttribute("user");
        Integer id = user.getId();
        healthClockIn.setUid(user.getUid());
        healthClockIn.setCreateTime(new Date());
        boolean save=healthClockInService.saveOrUpdate(healthClockIn);
        ViewData viewData = new ViewData();
        if(save){
            viewData.setCode(200);
            viewData.setMsg("数据新增成功!");
            return viewData;
        }
        viewData.setCode(100);
        viewData.setMsg("数据新增失败!");
        return viewData;
    }

    @RequestMapping("/updateHealthClockIn")
    @ResponseBody
    public ViewData updateHealthClockIn(HealthClockIn healthClockIn){
        boolean update=healthClockInService.updateById(healthClockIn);
        ViewData viewData=new ViewData();
        if(update){
            viewData.setCode(200);
            viewData.setMsg("数据更新成功!");
            return viewData;
        }
        viewData.setCode(100);
        viewData.setMsg("数据更新失败!");
        return viewData;
    }

    @RequestMapping("deleteHealthClockInById")
    @ResponseBody
    public ViewData deleteHealthClockInById(Integer id){
        healthClockInService.removeById(id);
        ViewData viewData = new ViewData();
        viewData.setCode(200);
        viewData.setMsg("数据删除成功！");
        return viewData;
    }
}
