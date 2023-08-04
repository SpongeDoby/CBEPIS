package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cbepis.entity.User;

public interface UserService extends IService<User> {
    User login(String username,String password);

    void deleteRoleByUid(Integer uid);

    void saveRidByUserId(Integer uid,Integer rid);
}
