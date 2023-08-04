package com.example.cbepis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cbepis.entity.*;
import com.example.cbepis.enums.ApprovalNodeStatusEnum;
import com.example.cbepis.service.ApprovalService;
import com.example.cbepis.service.BanJiService;
import com.example.cbepis.service.RoleService;
import com.example.cbepis.service.XueYuanService;
import com.example.cbepis.vo.ApprovalVo;
import com.example.cbepis.vo.ViewData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("approval")
public class ApprovalController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private BanJiService banJiService;
    @Autowired
    private XueYuanService xueYuanService;

    @RequestMapping("/toApproval")
    public String toApproval(HttpSession session){
        User user=(User) session.getAttribute("user");
        Integer id = user.getId();
        Integer integer = roleService.getRoleByUid(id).get(0);
        if(integer==3){
            return "approval/approval_user";
        }
        return "approval/approval";
    }

    @RequestMapping("/loadMyApproval")
    @ResponseBody
    public ViewData loadMyApproval(ApprovalVo approvalVo, HttpSession session){
        //拿到用户
        User user = (User)session.getAttribute("user");
        Integer id = user.getId();
        List<Integer> role = roleService.getRoleByUid(id);
        Integer integer = role.get(0);
        IPage<Approval> page=new Page<>(approvalVo.getPage(),approvalVo.getLimit());
        QueryWrapper<Approval> queryWrapper=new QueryWrapper<>();
        if(integer==1){
            IPage<Approval> page1 = approvalService.page(page, queryWrapper);
            for(Approval approval: page1.getRecords()){
                BanJi banJi = banJiService.getById(approval.getBanJiId());
                XueYuan xueYuan = xueYuanService.getById(approval.getXueYuanId());
                approval.setBanJiName(banJi.getName());
                approval.setXueYuanName(xueYuan.getName());
            }
            return new ViewData(page1.getTotal(),page1.getRecords());
        }else if(integer==3){
            //根据角色显示记录
            queryWrapper.eq("uid",user.getUid());
            IPage<Approval> page1 = approvalService.page(page, queryWrapper);
            for(Approval approval: page1.getRecords()){
                BanJi banJi = banJiService.getById(approval.getBanJiId());
                XueYuan xueYuan = xueYuanService.getById(approval.getXueYuanId());
                approval.setBanJiName(banJi.getName());
                approval.setXueYuanName(xueYuan.getName());
            }
            return new ViewData(page1.getTotal(),page1.getRecords());
        }else if(integer==2){
            queryWrapper.eq("grade_id",user.getGradeId());
            IPage<Approval> page1 = approvalService.page(page, queryWrapper);
            for(Approval approval: page1.getRecords()){
                BanJi banJi = banJiService.getById(approval.getBanJiId());
                XueYuan xueYuan = xueYuanService.getById(approval.getXueYuanId());
                approval.setBanJiName(banJi.getName());
                approval.setXueYuanName(xueYuan.getName());
            }
            return new ViewData(page1.getTotal(),page1.getRecords());
        }else if(integer==5){
            queryWrapper.eq("xue_yuan_id",user.getXueYuanId());
            IPage<Approval> page1 = approvalService.page(page, queryWrapper);
            for(Approval approval: page1.getRecords()){
                BanJi banJi = banJiService.getById(approval.getBanJiId());
                XueYuan xueYuan = xueYuanService.getById(approval.getXueYuanId());
                approval.setBanJiName(banJi.getName());
                approval.setXueYuanName(xueYuan.getName());
            }
            return new ViewData(page1.getTotal(),page1.getRecords());
        }else{
            ViewData viewData=new ViewData();
            viewData.setCode(100);
            viewData.setMsg("出错了！");
            return viewData;
        }
    }

    @RequestMapping("/addApproval")
    @ResponseBody
    public ViewData addApproval(Approval approval,HttpSession session){
        approval.setCreateTime(new Date());
        User user=(User) session.getAttribute("user");
        Integer id = user.getId();
        approval.setUid(user.getUid());
        List<Integer> role = roleService.getRoleByUid(id);
        Integer integer = role.get(0);
        Role byId = roleService.getById(integer);
        String roleName= byId.getName();
        if(StringUtils.equals(roleName,"管理员") ){
            approval.setNodeStatus(ApprovalNodeStatusEnum.COLLEGE_ING.getCode());
        }else if(StringUtils.equals(roleName,"学生")){
            approval.setNodeStatus(ApprovalNodeStatusEnum.TEACHER_ING.getCode());
        }else if(StringUtils.equals(roleName,"教师")){
            approval.setNodeStatus(ApprovalNodeStatusEnum.COLLEGE_ING.getCode());
        }else if(StringUtils.equals(roleName,"院系")){
            approval.setNodeStatus(ApprovalNodeStatusEnum.COLLEGE_PASSED.getCode());
        }else{
            approval.setNodeStatus(ApprovalNodeStatusEnum.TEACHER_ING.getCode());
        }
        approvalService.save(approval);
        ViewData viewData=new ViewData();
        viewData.setCode(200);
        viewData.setMsg("申请提交成功");
        return viewData;
    }

    @RequestMapping("/agreeApproval")
    @ResponseBody
    public ViewData agreeApproval(Approval approval,HttpSession session){
        ViewData viewData=new ViewData();
        approval.setUpdateTime(new Date());
        User user=(User) session.getAttribute("user");
        Integer id = user.getId();
        List<Integer> role = roleService.getRoleByUid(id);
        Integer integer = role.get(0);
        Role byId = roleService.getById(integer);
        String roleName= byId.getName();
        if(StringUtils.equals(roleName,"学生")){
            viewData.setCode(100);
            viewData.setMsg("无权进行此操作！");
            return viewData;
        }
        Approval serviceById=approvalService.getById(approval.getId());
        if(serviceById.getDone()==1){
            viewData.setCode(100);
            viewData.setMsg("流程已结束，不可进行审批");
            return viewData;
        }
        Integer nodeStatus=serviceById.getNodeStatus();
        if((StringUtils.equals(String.valueOf(ApprovalNodeStatusEnum.TEACHER_ING.getCode()),String.valueOf(nodeStatus)))
        || (StringUtils.equals(String.valueOf(ApprovalNodeStatusEnum.COLLEGE_ING.getCode()),String.valueOf(nodeStatus)))){
            if(StringUtils.equals(roleName,"管理员")){
                approval.setNodeStatus(ApprovalNodeStatusEnum.COLLEGE_PASSED.getCode());
            }else if(StringUtils.equals(roleName,"教师")){
                approval.setNodeStatus(ApprovalNodeStatusEnum.COLLEGE_ING.getCode());
            }else if(StringUtils.equals(roleName,"院系")){
                approval.setNodeStatus(ApprovalNodeStatusEnum.COLLEGE_PASSED.getCode());
            }else{
                approval.setNodeStatus(ApprovalNodeStatusEnum.TEACHER_PASSED.getCode());
            }
            approvalService.updateById(approval);
            viewData.setCode(200);
            viewData.setMsg("审批成功");
            return viewData;
        }
        viewData.setCode(100);
        viewData.setMsg("此状态不可进行审批");
        return viewData;
    }

    @RequestMapping("/rejectApproval")
    @ResponseBody
    public ViewData rejectApproval(Approval approval,HttpSession session){
        ViewData viewData=new ViewData();
        approval.setUpdateTime(new Date());
        User user=(User) session.getAttribute("user");
        Integer id = user.getId();
        List<Integer> role = roleService.getRoleByUid(id);
        Integer integer = role.get(0);
        Role byId = roleService.getById(integer);
        String roleName= byId.getName();
        if(StringUtils.equals(roleName,"学生")){
            viewData.setCode(100);
            viewData.setMsg("无权进行此操作！");
            return viewData;
        }
        Approval serviceById=approvalService.getById(approval.getId());
        if(serviceById.getDone()==1){
            viewData.setCode(100);
            viewData.setMsg("流程已结束，不可进行审批");
            return viewData;
        }
        Integer nodeStatus=serviceById.getNodeStatus();
        if((StringUtils.equals(String.valueOf(ApprovalNodeStatusEnum.TEACHER_ING.getCode()),String.valueOf(nodeStatus)))
                || (StringUtils.equals(String.valueOf(ApprovalNodeStatusEnum.COLLEGE_ING.getCode()),String.valueOf(nodeStatus)))){
            if(StringUtils.equals(roleName,"管理员")){
                approval.setNodeStatus(ApprovalNodeStatusEnum.COLLEGE_REJECTED.getCode());
            }else if(StringUtils.equals(roleName,"教师")){
                approval.setNodeStatus(ApprovalNodeStatusEnum.TEACHER_REJECTED.getCode());
            }else if(StringUtils.equals(roleName,"院系")){
                approval.setNodeStatus(ApprovalNodeStatusEnum.COLLEGE_REJECTED.getCode());
            }else{
                approval.setNodeStatus(ApprovalNodeStatusEnum.COLLEGE_REJECTED.getCode());
            }
            approvalService.updateById(approval);
            viewData.setCode(200);
            viewData.setMsg("驳回成功");
            return viewData;
        }
        viewData.setCode(100);
        viewData.setMsg("此状态不可驳回");
        return viewData;
    }

    /**
     * 拿到用户，判断角色
     * 若为教师或院系，判断有没有待审核的工单，有就提示并传过去
     * 若为学生，则拿到最新的请假单，判断该请假单是超时、已通过、或已拒绝
     * @param session
     * @return viewData
     * 返回提示信息并跳转至请假页
     */
    @RequestMapping("/tips")
    @ResponseBody
    public ViewData tips(HttpSession session){
        ViewData viewData=new ViewData();
        User user =(User) session.getAttribute("user");
        List<Integer> role = roleService.getRoleByUid(user.getId());
        Integer rid = role.get(0);
        QueryWrapper<Approval> queryWrapper=new QueryWrapper<>();
        //教师
        if(rid==2){
            queryWrapper.eq("grade_id",user.getGradeId());
            queryWrapper.eq("done",0);
            queryWrapper.eq("node_status",1);
            List<Approval> suspendedList = approvalService.list(queryWrapper);
            if(!suspendedList.isEmpty()){
                viewData.setCode(200);
                viewData.setMsg("有待审核的请假申请");
                return viewData;
            }else{
                return null;
            }
        }else if(rid==3){//学生
            //拿到当前学生最新的未结束处理请假记录
            queryWrapper.orderByDesc("id");
            queryWrapper.eq("uid",user.getUid());
            queryWrapper.eq("done",0);
            List<Approval> suspendedList = approvalService.list(queryWrapper);
            if(!suspendedList.isEmpty()){
                Approval approval = suspendedList.get(0);
                if(approval.getNodeStatus()==6){
                    approval.setDone(1);
                    approvalService.updateById(approval);
                    viewData.setCode(200);
                    viewData.setMsg("您提交的申请已通过审核！请前往查看");
                    return viewData;
                }else if(approval.getNodeStatus()==5 || approval.getNodeStatus()==2){
                    approval.setDone(1);
                    approvalService.updateById(approval);
                    viewData.setCode(200);
                    viewData.setMsg("您提交的申请已被驳回！请前往查看");
                    return viewData;
                }else{
                    Date createTime = approval.getCreateTime();
                    long time1 = createTime.getTime();
                    Date now=new Date();
                    long time2=now.getTime();
                    long flag=(time2-time1)/(1000*60*60*24);
                    if(flag>=1){
                        viewData.setCode(200);
                        viewData.setMsg("您的申请已超时未完成处理！请前往查看");
                        approval.setNodeStatus(7);
                        approval.setDone(1);
                        approvalService.updateById(approval);
                        return viewData;
                    }
                }
            }else{
                return null;
            }

        }else if(rid==5){//院系
            queryWrapper.eq("xue_yuan_id",user.getXueYuanId());
            queryWrapper.eq("done",0);
            queryWrapper.eq("node_status",4);
            List<Approval> suspendedList = approvalService.list(queryWrapper);
            if(!suspendedList.isEmpty()){
                viewData.setCode(200);
                viewData.setMsg("有待审核的请假申请");
                return viewData;
            }else{
                return null;
            }
        }
        return null;
    }
}
