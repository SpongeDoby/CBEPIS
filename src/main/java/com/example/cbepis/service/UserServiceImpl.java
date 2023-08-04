package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cbepis.dao.RoleMapper;
import com.example.cbepis.dao.UserMapper;
import com.example.cbepis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public User login(String username, String password) {
        return userMapper.login(username,password);
    }

    @Override
    public void deleteRoleByUid(Integer uid) {
        roleMapper.deleteRoleByUid(uid);
    }

    @Override
    public void saveRidByUserId(Integer uid, Integer rid) {
        roleMapper.saveRidByUserId(uid,rid);
    }
}
