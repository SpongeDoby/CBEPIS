package com.example.cbepis.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cbepis.entity.ConfirmedData;
import com.example.cbepis.entity.LineTrend;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface IndexMapper extends BaseMapper<ConfirmedData> {
    @Select("select * from line_trend order by create_time limit 7")
    List<LineTrend> getSevenDayData();
}
