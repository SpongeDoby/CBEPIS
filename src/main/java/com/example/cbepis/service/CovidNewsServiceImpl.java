package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cbepis.dao.NewsMapper;
import com.example.cbepis.entity.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CovidNewsServiceImpl extends ServiceImpl<NewsMapper, News> implements CovidNewsService {
    @Autowired
    private NewsMapper newsMapper;

    @Override
    public List<News> list5News() {
        return newsMapper.list5News();
    }
}
