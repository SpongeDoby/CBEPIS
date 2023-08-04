package com.example.cbepis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cbepis.entity.BanJi;
import com.example.cbepis.entity.User;
import com.example.cbepis.entity.XueYuan;
import com.example.cbepis.service.BanJiService;
import com.example.cbepis.service.RoleService;
import com.example.cbepis.service.UserService;
import com.example.cbepis.service.XueYuanService;
import com.example.cbepis.vo.UserVo;
import com.example.cbepis.vo.ViewData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.swing.text.View;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private BanJiService banJiService;
    @Autowired
    private XueYuanService xueYuanService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("/toUser")
    public String toUserAdmin(){
        return "user/user";
    }

    @RequestMapping("/toChangePwd")
    public String toChangePwd(){
        return "user/changepassword";
    }

    @RequestMapping("/toUserInfo")
    public String toUserInfo(){
        return "user/userInfo";
    }

    //用户数据表格
    @RequestMapping("/loadAllUser")
    @ResponseBody
    public ViewData getUsers(UserVo userVo){
        IPage<User> page=new Page<>(userVo.getPage(), userVo.getLimit());
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(userVo.getUsername()),"username",userVo.getUsername());
        queryWrapper.eq(StringUtils.isNotBlank(userVo.getUid()),"uid",userVo.getUid());
        IPage<User> page1 = userService.page(page, queryWrapper);
        //班级名赋值、学院名赋值、教师名赋值
        for(User user:page1.getRecords()){
            if(user.getBanJiId()!=null){
                BanJi banJi = banJiService.getById(user.getBanJiId());
                user.setBanJiName(banJi.getName());
            }
            if(user.getXueYuanId()!=null){
                XueYuan xueYuan=xueYuanService.getById(user.getXueYuanId());
                user.setXueYuanName(xueYuan.getName());
            }
            if(user.getTeacherId()!=null){
                User teacher=userService.getById(user.getTeacherId());
                user.setTeacherName(teacher.getUsername());
            }
        }
        return new ViewData(page1.getTotal(),page1.getRecords());
    }

    //新增用户
    @RequestMapping("/addUser")
    @ResponseBody
    public ViewData addUser(User user){
        user.setImg("/images/login.jpg");
        userService.save(user);
        ViewData viewData=new ViewData();
        viewData.setCode(200);
        viewData.setMsg("用户添加成功");
        return  viewData;
    }

    //编辑
    @RequestMapping("/updateUser")
    @ResponseBody
    public ViewData UpdateUser(User user){
        ViewData viewData=new ViewData();
        if(userService.updateById(user)){
            viewData.setCode(200);
            viewData.setMsg("数据更新成功");
        }else{
            viewData.setCode(100);
            viewData.setMsg("数据更新失败");
        }
        return viewData;
    }

    //删除
    @RequestMapping("/deleteUser/{id}")
    @ResponseBody
    public ViewData deleteUser(@PathVariable("id") Integer id){
        ViewData viewData=new ViewData();
        if(userService.removeById(id)){
            viewData.setCode(200);
            viewData.setMsg("数据删除成功");
        }else{
            viewData.setCode(100);
            viewData.setMsg("数据删除失败");
        }
        return viewData;
    }

    //班级下拉框数据
    @RequestMapping("/listAllBanJi")
    @ResponseBody
    public List<BanJi> listAllBanJi(@Param("xueyuan") Integer xueyuan){
        QueryWrapper<BanJi> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("xue_yuan_id",xueyuan);
        List<BanJi> list = banJiService.list(queryWrapper);
        return list;
    }

    //学院下拉框数据
    @RequestMapping("/listAllXueYuan")
    @ResponseBody
    public List<XueYuan> listAllXueYuan(){
        return xueYuanService.list();
    }

    //教师下拉框数据
    @RequestMapping("/listTeachers")
    @ResponseBody
    public List<User> listTeachers(@Param("xueyuan") Integer xueyuan){
        List<Integer> teachers = roleService.getTeachers();
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("xue_yuan_id",xueyuan);
        queryWrapper.in("id",teachers);
        List<User> list = userService.list(queryWrapper);
        return list;
    }

    //重置密码(改为默认的123456）
    @RequestMapping("/resetPwd/{id}")
    @ResponseBody
    public ViewData resetPwd(@PathVariable("id") Integer id){
        User user=new User();
        user.setId(id);
        user.setPassword("123456");
        userService.updateById(user);
        ViewData viewData=new ViewData();
        viewData.setCode(200);
        viewData.setMsg("密码重置成功");
        return  viewData;
    }

    //处理分配角色请求，初始化用户角色列表，已拥有的角色自动勾选，同权限分配
    @RequestMapping("/initRoleByUserId")
    @ResponseBody
    public ViewData initRoleByUserId(Integer id){
        //所有角色
        List<Map<String, Object>> listMaps = roleService.listMaps();
        //当前用户所拥有的角色
        List<Integer> currentUserRole=roleService.getRoleByUid(id);
        for(Map<String,Object> map:listMaps){
            boolean LAY_CHECKED=false;
            Integer roleId = (Integer)map.get("id");
            for(Integer rid:currentUserRole){
                if(rid.equals(roleId)){
                    LAY_CHECKED=true;
                    break;
                }
            }
            map.put("LAY_CHECKED",LAY_CHECKED);
        }
        return new ViewData(Long.valueOf(listMaps.size()),listMaps);
    }

    //分配角色并保存
    @RequestMapping("/saveUserRole")
    @ResponseBody
    public ViewData saveUserRole(Integer uid,Integer[] rids){
        userService.deleteRoleByUid(uid);
        ViewData viewData=new ViewData();
        if(rids!=null && rids.length>0){
            for(Integer rid:rids){
                userService.saveRidByUserId(uid,rid);
            }
            viewData.setCode(200);
            viewData.setMsg("角色分配成功");
        }else{
            viewData.setCode(100);
            viewData.setMsg("角色分配失败");
        }
        return viewData;
    }

    //修改密码
    @RequestMapping("/changePassword")
    @ResponseBody
    public ViewData changePassword(User user,String newPwdOne,String newPwdTwo){
        ViewData viewData=new ViewData();
        User byId = userService.getById(user.getId());
        if(StringUtils.equals(byId.getPassword(),user.getPassword())){
            if(StringUtils.equals(newPwdOne,newPwdTwo)){
                byId.setPassword(newPwdOne);
                userService.updateById(byId);
                viewData.setCode(200);
                viewData.setMsg("重置密码成功！");
                return viewData;
            }else{
                viewData.setCode(100);
                viewData.setMsg("输入的两次新密码不匹配！");
                return viewData;
            }
        }else{
            viewData.setCode(100);
            viewData.setMsg("旧密码有误！");
            return viewData;
        }
    }

    //从文件导入用户数据
    @RequestMapping("/userUpload")
    @ResponseBody
    public ViewData userUpload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        ViewData viewData = new ViewData();
        //POI获取文件数据
        XSSFWorkbook xssfWorkbook=new XSSFWorkbook(multipartFile.getInputStream());
        //拿到工作表
        XSSFSheet xssfSheet=xssfWorkbook.getSheetAt(0);
        //解析数据，使用集合接收文件数据
        List<User> userList=new ArrayList<>();
        XSSFRow xssfRow=null;
        QueryWrapper<BanJi> banJiQueryWrapper=new QueryWrapper<>();
        for (int i = 0; i < xssfSheet.getPhysicalNumberOfRows(); i++) {
            User user=new User();
            xssfRow=xssfSheet.getRow(i);
            //学号、名字、密码、性别、年龄、籍贯、电话、身份证、班级、学院
            user.setImg("/images/login.jpg");
            XSSFCell uidCell = xssfRow.getCell(0);
            uidCell.setCellType(CellType.STRING);
            user.setUid(uidCell.getStringCellValue());
            user.setUsername(xssfRow.getCell(1).getStringCellValue());
            XSSFCell pwdCell = xssfRow.getCell(2);
            pwdCell.setCellType(CellType.STRING);
            user.setPassword(pwdCell.getStringCellValue());
            user.setSex(xssfRow.getCell(3).getStringCellValue());
            XSSFCell ageCell = xssfRow.getCell(4);
            ageCell.setCellType(CellType.STRING);
            user.setAge(ageCell.getStringCellValue());
            user.setAddress(xssfRow.getCell(5).getStringCellValue());
            XSSFCell phoneCell = xssfRow.getCell(6);
            phoneCell.setCellType(CellType.STRING);
            user.setPhone(phoneCell.getStringCellValue());
            XSSFCell cardCell = xssfRow.getCell(7);
            cardCell.setCellType(CellType.STRING);
            user.setCard(cardCell.getStringCellValue());
            user.setBanJiName(xssfRow.getCell(8).getStringCellValue());
            banJiQueryWrapper.eq(StringUtils.isNotBlank(user.getBanJiName()),"name",user.getBanJiName());
            List<BanJi> list = banJiService.list(banJiQueryWrapper);
            BanJi banJi = list.get(0);
            user.setBanJiId(banJi.getId());
            user.setXueYuanId(banJi.getXueYuanId());
            XSSFCell gradeCell = xssfRow.getCell(9);
            gradeCell.setCellType(CellType.STRING);
            user.setGradeId(gradeCell.getStringCellValue());
            userList.add(user);
        }
        userService.saveBatch(userList);
        viewData.setCode(200);
        viewData.setMsg("数据导入成功,请刷新页面");
        return viewData;
    }
}
