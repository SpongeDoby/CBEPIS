package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cbepis.dao.ApprovalMapper;
import com.example.cbepis.entity.Approval;
import org.springframework.stereotype.Service;

@Service
public class ApprovalServiceImpl extends ServiceImpl<ApprovalMapper, Approval> implements ApprovalService {
}
