package com.example.cbepis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cbepis.entity.News;

import java.util.List;

public interface CovidNewsService extends IService<News> {
    List<News> list5News();
}
