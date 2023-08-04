package com.example.cbepis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("community_detail")
public class CommunityDetail {
    @TableId(value="id",type= IdType.AUTO)
    private Integer id;
    private String areaName;
    private String type;
    private String address;
}
