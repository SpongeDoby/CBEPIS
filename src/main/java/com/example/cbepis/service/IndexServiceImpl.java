package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cbepis.dao.IndexMapper;
import com.example.cbepis.entity.ConfirmedData;
import com.example.cbepis.entity.LineTrend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexServiceImpl extends ServiceImpl<IndexMapper, ConfirmedData> implements IndexService {
    @Autowired
    private IndexMapper indexMapper;
    @Override
    public List<LineTrend> getSevenDayData() {
        return indexMapper.getSevenDayData();
    }
}
