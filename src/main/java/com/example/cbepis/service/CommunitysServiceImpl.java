package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cbepis.dao.CommunitysMapper;
import com.example.cbepis.entity.Communitys;
import org.springframework.stereotype.Service;

@Service
public class CommunitysServiceImpl extends ServiceImpl<CommunitysMapper, Communitys> implements CommunitysService {
}
