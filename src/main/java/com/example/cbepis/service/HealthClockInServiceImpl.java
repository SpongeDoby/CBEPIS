package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cbepis.dao.HealthClockInMapper;
import com.example.cbepis.entity.HealthClockIn;
import org.springframework.stereotype.Service;

@Service
public class HealthClockInServiceImpl extends ServiceImpl<HealthClockInMapper, HealthClockIn> implements HealthClockInService {
}
