package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cbepis.dao.VaccineMapper;
import com.example.cbepis.entity.Vaccine;
import org.springframework.stereotype.Service;

@Service
public class VaccineServiceImpl extends ServiceImpl<VaccineMapper,Vaccine> implements VaccineService{
}
