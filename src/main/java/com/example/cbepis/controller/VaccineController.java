package com.example.cbepis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cbepis.entity.BanJi;
import com.example.cbepis.entity.User;
import com.example.cbepis.entity.Vaccine;
import com.example.cbepis.entity.XueYuan;
import com.example.cbepis.service.BanJiService;
import com.example.cbepis.service.RoleService;
import com.example.cbepis.service.VaccineService;
import com.example.cbepis.service.XueYuanService;
import com.example.cbepis.vo.VaccineVo;
import com.example.cbepis.vo.ViewData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/vaccine")
public class VaccineController {
    @Autowired
    private VaccineService vaccineService;
    @Autowired
    private BanJiService banJiService;
    @Autowired
    private XueYuanService xueYuanService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("/toVaccine")
    public String toVaccine(HttpSession session){
        User user=(User) session.getAttribute("user");
        Integer id = user.getId();
        List<Integer> roleByUid = roleService.getRoleByUid(id);
        Integer integer = roleByUid.get(0);
        if(integer!=1){
            return "vaccine/vaccine_user";
        }
        return "vaccine/vaccine";
    }

    @RequestMapping("/loadAllVaccine")
    @ResponseBody
    public ViewData loadAllVaccine(VaccineVo vaccineVo,HttpSession session){
        IPage<Vaccine> page=new Page<>(vaccineVo.getPage(),vaccineVo.getLimit());
        QueryWrapper<Vaccine> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(vaccineVo.getName()),"name",vaccineVo.getName());
        User user=(User) session.getAttribute("user");
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
        IPage<Vaccine> page1 = vaccineService.page(page, queryWrapper);
        List<Vaccine> records = page1.getRecords();
        for(Vaccine i:records){
            if(i.getBanJiId()!=null){
                BanJi banji=banJiService.getById(i.getBanJiId());
                i.setBanJiName(banji.getName());
            }
            if(i.getXueYuanId()!=null){
                XueYuan xueYuan=xueYuanService.getById(i.getXueYuanId());
                i.setXueYuanName(xueYuan.getName());
            }
        }
        return new ViewData(page1.getTotal(),page1.getRecords());
    }

    @RequestMapping("/addVaccine")
    @ResponseBody
    public ViewData addVaccine(Vaccine vaccine, HttpSession session){
        ViewData viewData=new ViewData();
        User user=(User) session.getAttribute("user");
        Integer id = user.getId();
        vaccine.setUid(user.getUid());
        if(vaccineService.save(vaccine)){
            viewData.setMsg("数据新增成功");
            viewData.setCode(200);
            return viewData;
        }else{
            viewData.setCode(100);
            viewData.setMsg("数据新增失败");
            return viewData;
        }
    }

    @RequestMapping("/updateVaccine")
    @ResponseBody
    public ViewData updateVaccine(Vaccine vaccine){
        ViewData viewData=new ViewData();
        if(vaccineService.updateById(vaccine)){
            viewData.setMsg("数据更新成功");
            viewData.setCode(200);
            return viewData;
        }else{
            viewData.setCode(100);
            viewData.setMsg("数据更新失败");
            return viewData;
        }
    }

    @RequestMapping("/deleteVaccine")
    @ResponseBody
    public ViewData deleteVaccine(Integer id){
        ViewData viewData=new ViewData();
        if(vaccineService.removeById(id)){
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
