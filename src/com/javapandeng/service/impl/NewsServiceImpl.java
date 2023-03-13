package com.javapandeng.service.impl;

import com.javapandeng.base.BaseMapper;
import com.javapandeng.base.BaseServiceImpl;
import com.javapandeng.mapper.NewsMapper;
import com.javapandeng.po.News;
import com.javapandeng.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl extends BaseServiceImpl<News> implements NewsService {
    @Autowired
    private NewsMapper newsMapper;

    @Override
    public BaseMapper<News> getBaseMapper() {
        return newsMapper;
    }
}
