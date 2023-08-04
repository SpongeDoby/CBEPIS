package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cbepis.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    List<Integer> queryPermissionByRid(Integer roleId);

    void deleteRoleByRid(Integer rid);

    void saveMidByRoleId(Integer rid, Integer mid);

    List<Integer> getRoleByUid(Integer id);

    List<Integer> getTeachers();
}
