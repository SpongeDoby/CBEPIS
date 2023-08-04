package com.example.cbepis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cbepis.entity.Menu;
import com.example.cbepis.entity.User;
import com.example.cbepis.service.MenuService;
import com.example.cbepis.service.RoleService;
import com.example.cbepis.utils.TreeNode;
import com.example.cbepis.utils.TreeNodeBuilder;
import com.example.cbepis.vo.MenuVo;
import com.example.cbepis.vo.ViewData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

//菜单管理，管理菜单数据、根据用户角色动态生成页面
@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("/toMenu")
    public String toMenu(){
        return "menu/menu";
    }

    //菜单管理页，展示菜单数据
    @RequestMapping("loadAllMenu")
    @ResponseBody
    public ViewData loadAllMenu(MenuVo menuVo){
        IPage<Menu> page=new Page<>(menuVo.getPage(),menuVo.getLimit());
        QueryWrapper<Menu> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(menuVo.getTitle()),"title",menuVo.getTitle());
        queryWrapper.orderByAsc("ordernum");
        menuService.page(page,queryWrapper);
        return new ViewData(page.getTotal(),page.getRecords());
    }

    //处理新增菜单请求
    @RequestMapping("/addMenu")
    @ResponseBody
    public ViewData addMenu(Menu menu){
        ViewData viewData = new ViewData();
        menu.setType("Menu");
        boolean flag=menuService.save(menu);
        if(!flag){
            viewData.setCode(100);
            viewData.setMsg("数据插入失败");
        }else{
            viewData.setCode(200);
            viewData.setMsg("数据插入失败");
        }
        return viewData;
    }

    //获取菜单数据，初始化新增菜单弹窗中的下拉树
    @RequestMapping("loadMenuManagerLeftTreeJson")
    @ResponseBody
    public ViewData loadMenuManagerLeftTreeJson(){
        List<Menu> list = menuService.list();
        List<TreeNode> treeNodes=new ArrayList<>();
        for (Menu i: list) {
            Boolean open=i.getOpen()==1?true: false;
            treeNodes.add(new TreeNode(i.getId(),i.getPid(),i.getTitle(),open));
        }
        return new ViewData(treeNodes);
    }

    //增加菜单时，自动填充排序码
    @RequestMapping("/loadMenuMaxOrderNum")
    @ResponseBody
    public Map<String,Object> loadMenuMaxOrderNum(){
        QueryWrapper<Menu> queryWrapper=new QueryWrapper<>();
        Map<String,Object> map=new HashMap<>();
        queryWrapper.orderByDesc("ordernum");
        IPage<Menu> page=new Page<>(1,1);
        menuService.page(page,queryWrapper);
        List<Menu> list=page.getRecords();
        Menu menu=list.get(0);
        map.put("value",menu.getOrdernum()+1);
        return map;
    }

    //更新菜单
    @RequestMapping("/updateMenu")
    @ResponseBody
    public ViewData updateMenu(Menu menu){
        menuService.updateById(menu);
        ViewData viewData = new ViewData();
        viewData.setCode(200);
        viewData.setMsg("更新菜单成功");
        return viewData;
    }

    //删除菜单,先判断是否为父节点
    @RequestMapping("/checkMenuHasChildrenNode")
    @ResponseBody
    public Map<String,Object> check(Menu menu){
        Map<String,Object> map=new HashMap<>();
        QueryWrapper<Menu> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("pid",menu.getId());
        List<Menu> list = menuService.list(queryWrapper);
        if(list.size()>0){
            map.put("value",true);
        }else{
            map.put("value",false);
        }
        return map;
    }
    //删除菜单
    @RequestMapping("/deleteMenu")
    @ResponseBody
    public ViewData deleteMenu(Menu menu){
        ViewData viewData=new ViewData();
        boolean delete = menuService.removeById(menu.getId());
        if(delete){
            viewData.setCode(200);
            viewData.setMsg("数据删除成功");
        }else{
            viewData.setCode(100);
            viewData.setMsg("数据删除失败");
        }
        return viewData;
    }

    //动态加载菜单，根据用户动态展示
    @RequestMapping("/loadIndexLeftMenuJson")
    @ResponseBody
    public ViewData loadIndexLeftMenuJson(HttpSession session){
        List<Menu> list = null;
        QueryWrapper<Menu> queryWrapper=new QueryWrapper<>();
        //拿到session中的用户id
        User user=(User)session.getAttribute("user");
        Integer uid=user.getId();
        //根据uid拿到rid
        List<Integer> roleByUid = roleService.getRoleByUid(uid);
        Integer integer = roleByUid.get(0);
        if(integer==1){
            list=menuService.list();
        }else{
            //根据用户id拿到角色
            List<Integer> currentRids = roleService.getRoleByUid(uid);
            //根据角色查询菜单栏id、去重（用Set)、考虑到可能一个用户有多角色，注：在完成请假功能后决定一个用户只有一个角色
            Set<Integer> mids=new HashSet<>();
            for(Integer rid: currentRids){
                List<Integer> permissionIds = roleService.queryPermissionByRid(rid);
                mids.addAll(permissionIds);
            }
            //根据查询到的菜单id拿到菜单列表
            if(mids.size()>0){
                queryWrapper.in("id",mids);
                list=menuService.list(queryWrapper);
            }
        }

        //构造层级关系
        List<TreeNode> treeNodes=new ArrayList<>();
        for(Menu i:list){
            Integer id= i.getId();
            Integer pid = i.getPid();
            String title = i.getTitle();
            String icon = i.getIcon();
            String href = i.getHref();
            boolean open=i.getOpen().equals(1)?true: false;
            treeNodes.add(new TreeNode(id,pid,title,icon,href,open));
        }
        //传入节点列表以及标识父ID的值到工具类构建层级关系
        List<TreeNode> nodeList = TreeNodeBuilder.build(treeNodes, 0);
        return new ViewData(nodeList);
    }
}
