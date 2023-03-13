package com.javapandeng.service.impl;

import com.javapandeng.base.BaseMapper;
import com.javapandeng.base.BaseServiceImpl;
import com.javapandeng.mapper.ScMapper;
import com.javapandeng.po.Sc;
import com.javapandeng.service.ScService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScServiceImpl extends BaseServiceImpl<Sc> implements ScService {
    @Autowired
    private ScMapper scMapper;

    @Override
    public BaseMapper<Sc> getBaseMapper() {
        return scMapper;
    }
}
