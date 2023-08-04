package com.example.cbepis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("global_data")
public class GlobalData {
    private String name;
    private Integer value;
}
