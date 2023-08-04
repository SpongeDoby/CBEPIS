package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cbepis.dao.GlobalDataMapper;
import com.example.cbepis.entity.GlobalData;
import org.springframework.stereotype.Service;

@Service
public class GlobalDataServiceImpl extends ServiceImpl<GlobalDataMapper, GlobalData> implements GlobalDataService{
}
