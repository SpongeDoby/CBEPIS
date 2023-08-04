package com.example.cbepis.vo;

import com.example.cbepis.entity.News;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NewsVo extends News {
    private Integer page;
    private Integer limit;
}
