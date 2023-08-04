package com.example.cbepis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user")
@Data
public class User {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String uid;
    private String username;
    private String password;
    private String salt;
    private String gradeId;

    private String sex;
    private String age;
    private String address;
    private String img;
    private String phone;
    private String card;
    //外键，用于联表查询
    private Integer banJiId;
    private Integer xueYuanId;
    private Integer teacherId;

    @TableField(exist = false)
    private String banJiName;
    @TableField(exist = false)
    private String xueYuanName;
    @TableField(exist = false)
    private String teacherName;
}
