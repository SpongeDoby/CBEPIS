package com.example.cbepis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("gz_confirmed_data")
@Data
public class GzConfirmedData {
    @TableId(value = "id",type= IdType.AUTO)
    private Integer id;
    private String name;
    private Integer value;
}
