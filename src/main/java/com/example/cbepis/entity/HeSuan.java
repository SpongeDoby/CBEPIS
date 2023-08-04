package com.example.cbepis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("hesuan")
public class HeSuan {
    @TableId(value="id",type= IdType.AUTO)
    private Integer id;
    private String uid;
    private String name;
    private String gradeId;
    private Integer banJiId;
    private Integer xueYuanId;
    private Integer age;
    private String phone;
    private String card;

    private String type;//【单检，混检、咽喉......】
    private String result;//三种值【未出，阴性，阳性】

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//检测时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//结果上报时间

    @TableField(exist = false)
    private String banJiName;
    @TableField(exist = false)
    private String xueYuanName;

}
