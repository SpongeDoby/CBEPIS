package com.example.cbepis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("health_clock_in")
public class HealthClockIn {
    @TableId(value="id",type = IdType.AUTO)
    private Integer id;
    private String uid;
    private String username;
    private String gradeId;
    private Integer banJiId;
    private Integer xueYuanId;
    private String sex;
    private Integer age;
    private String phone;
    private String morningTemp;
    private String afternoonTemp;
    private String nightTemp;
    private String feverAndCough;
    private String recentHome;
    private String riskZone;
    private String recentZone;
    private String healthStatus;
    private Date createTime;

    @TableField(exist = false)
    private String banJiName;
    @TableField(exist = false)
    private String xueYuanName;
}
