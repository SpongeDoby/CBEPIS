package com.example.cbepis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("community")
public class Communitys {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String areaName;
    private String highCount;
    private String middleCount;
    private String endUpdateTime;
}
