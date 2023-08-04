package com.example.cbepis.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cbepis.entity.News;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NewsMapper extends BaseMapper<News> {
    @Select("select * from covid_news order by create_time limit 5")
    List<News> list5News();
}
