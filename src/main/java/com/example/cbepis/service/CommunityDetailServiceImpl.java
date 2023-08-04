package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cbepis.dao.CommunityDetailMapper;
import com.example.cbepis.entity.CommunityDetail;
import org.springframework.stereotype.Service;

@Service
public class CommunityDetailServiceImpl extends ServiceImpl<CommunityDetailMapper, CommunityDetail> implements CommunityDetailService {
}
