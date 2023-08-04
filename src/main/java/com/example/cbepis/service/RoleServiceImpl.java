package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cbepis.dao.RoleMapper;
import com.example.cbepis.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    RoleMapper roleMapper;

    @Override
    public List<Integer> queryPermissionByRid(Integer roleId) {
        return roleMapper.queryPermissionByRid(roleId);
    }

    @Override
    public void deleteRoleByRid(Integer rid) {
        roleMapper.deleteRoleByRid(rid);
    }

    @Override
    public void saveMidByRoleId(Integer rid, Integer mid) {
        roleMapper.saveMidByRoleId(rid,mid);
    }

    @Override
    public List<Integer> getRoleByUid(Integer id) {
        return roleMapper.getRoleByUid(id);
    }

    @Override
    public List<Integer> getTeachers() {
        return roleMapper.getTeachers();
    }


}
