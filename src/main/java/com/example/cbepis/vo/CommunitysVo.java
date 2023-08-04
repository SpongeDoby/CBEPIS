package com.example.cbepis.vo;

import com.example.cbepis.entity.Communitys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunitysVo extends Communitys {
    private Integer page;
    private Integer limit;
    private String province;
    private String city;
    private String county;
}
