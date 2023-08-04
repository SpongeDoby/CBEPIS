package com.example.cbepis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("vaccine")
public class Vaccine {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String uid;
    private String name;
    private Integer age;
    private String phone;
    private String card;

    private String zhenci;
    private String pici;
    private String changjia;
    private String gradeId;
    private Integer banJiId;
    private Integer xueYuanId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(exist = false)
    private String banJiName;
    @TableField(exist = false)
    private String xueYuanName;

}
