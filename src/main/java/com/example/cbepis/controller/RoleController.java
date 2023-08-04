package com.example.cbepis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cbepis.entity.Menu;
import com.example.cbepis.entity.Role;
import com.example.cbepis.service.MenuService;
import com.example.cbepis.service.RoleService;
import com.example.cbepis.utils.TreeNode;
import com.example.cbepis.vo.RoleVo;
import com.example.cbepis.vo.ViewData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.text.View;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    @RequestMapping("/toRole")
    public String toRole(){
        return "/role/role";
    }

    //加载、模糊查询表格
    @RequestMapping("/loadAllRole")
    @ResponseBody
    public ViewData loadAllRole(RoleVo roleVo){
        ViewData viewData=new ViewData();
        IPage<Role> page=new Page<>(roleVo.getPage(),roleVo.getLimit());
        QueryWrapper<Role> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getName()),"name",roleVo.getName());
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getRemark()),"remark",roleVo.getRemark());
        roleService.page(page,queryWrapper);
        viewData.setCount(page.getTotal());
        viewData.setData(page.getRecords());
        return  viewData;
    }

    //新增角色
    @RequestMapping("/addRole")
    @ResponseBody
    public ViewData addRole(Role role){
        ViewData viewData=new ViewData();
        if(roleService.save(role)){
            viewData.setCode(200);
            viewData.setMsg("添加角色成功");
            return viewData;
        }
        return null;
    }

    //删除角色
    @RequestMapping("/deleteRole")
    @ResponseBody
    public ViewData deleteRole(Role role){
        ViewData viewData=new ViewData();
        if(roleService.removeById(role.getId())){
            viewData.setCode(200);
            viewData.setMsg("角色删除成功");
            return viewData;
        }
        return null;
    }

    //编辑
    @RequestMapping("/updateRole")
    @ResponseBody
    public ViewData updateRole(Role role){
        ViewData viewData=new ViewData();
        if(roleService.updateById(role)){
            viewData.setCode(200);
            viewData.setMsg("角色编辑成功");
            return viewData;
        }
        return null;
    }

    //打开权限分配弹窗，初始化权限下拉树（已拥有权限的自动打钩）
    @RequestMapping("/initPermissionByRoleId")
    @ResponseBody
    public ViewData initPermissionByRoleId(Integer roleId){
        QueryWrapper<Menu> queryWrapper=new QueryWrapper<>();
        //获取所有可访问资源
        List<Menu> allResources = menuService.list();
        //根据角色id拿到菜单mid
        List<Integer> currentRoleMid=roleService.queryPermissionByRid(roleId);
        //使用工具类构造下拉树
            //先将查询到的menu实体放入列表
        List<Menu> currentRolePermission=null;
        if(currentRoleMid.size()>0){
            queryWrapper.in("id",currentRoleMid);
            currentRolePermission=menuService.list(queryWrapper);
        }else{
            currentRolePermission=new ArrayList<>();
        }
            //返回节点列表
        List<TreeNode> treeNodes=new ArrayList<>();
        for(Menu allPermission:allResources){
            String checkArr="";
            for(Menu currentPermission:currentRolePermission){
                if(allPermission.getId().equals(currentPermission.getId())){
                    checkArr="1";
                    break;
                }
            }
            Boolean spread=(allPermission.getOpen()==null || allPermission.getOpen()==1)?true:false;
            treeNodes.add(new TreeNode(allPermission.getId(),allPermission.getPid(),allPermission.getTitle(),spread,checkArr));
        }
        return new ViewData(treeNodes);
    }

    /**确认分配权限，将权限保存到role_menu表中（插库）
     *
     * @param=rid=？&ids=？
     */
    @RequestMapping("/saveRolePermission")
    @ResponseBody
    public ViewData saveRolePermission(Integer rid,Integer[] mids){
        //根据rid清除存在的权限
        roleService.deleteRoleByRid(rid);
        //更新权限
        if(mids!=null && mids.length>0){
            for(Integer mid:mids){
                roleService.saveMidByRoleId(rid,mid);
            }
        }
        ViewData viewData=new ViewData();
        viewData.setCode(200);
        viewData.setMsg("权限分配成功");
        return viewData;
    }
}
