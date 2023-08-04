package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cbepis.entity.DomesticTotal;
import org.springframework.stereotype.Service;
import com.example.cbepis.dao.DomesticTotalMapper;

@Service
public class DomesticTotalServiceImpl extends ServiceImpl<DomesticTotalMapper, DomesticTotal> implements DomesticTotalService {
}
