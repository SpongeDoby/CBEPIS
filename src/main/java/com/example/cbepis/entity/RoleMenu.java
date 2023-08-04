package com.example.cbepis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("role_menu")
@Data
public class RoleMenu {
    private Integer rid;
    private Integer mid;
}
