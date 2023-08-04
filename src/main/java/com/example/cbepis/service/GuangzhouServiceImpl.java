package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cbepis.dao.GuangzhouMapper;
import com.example.cbepis.entity.GzConfirmedData;
import org.springframework.stereotype.Service;

@Service
public class GuangzhouServiceImpl extends ServiceImpl<GuangzhouMapper, GzConfirmedData> implements GuangzhouService {
}
