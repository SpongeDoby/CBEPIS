package com.example.cbepis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("role")
@Data
public class Role {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private String remark;
}
