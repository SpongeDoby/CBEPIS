package com.example.cbepis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("covid_news")
public class News {
    @TableId(value = "id",type= IdType.AUTO)
    private Integer id;
    private String title;
//    private String content;
    private String href;
    private Date createTime;
    private String publishBy;
}
