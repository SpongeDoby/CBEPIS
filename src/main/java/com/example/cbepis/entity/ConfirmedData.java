package com.example.cbepis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("confirmed_data")
public class ConfirmedData {
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    private String name;
    private Integer value;
    private Date updateTime;
}
