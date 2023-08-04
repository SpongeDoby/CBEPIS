package com.example.cbepis.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cbepis.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where username=#{username} and password=#{password}")
    User login(String username,String password);
}
