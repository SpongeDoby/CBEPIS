package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cbepis.entity.ConfirmedData;
import com.example.cbepis.entity.LineTrend;

import java.util.List;

public interface IndexService extends IService<ConfirmedData> {
    List<LineTrend> getSevenDayData();
}
