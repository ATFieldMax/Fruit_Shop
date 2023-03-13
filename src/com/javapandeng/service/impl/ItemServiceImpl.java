package com.javapandeng.service.impl;

import com.javapandeng.base.BaseMapper;
import com.javapandeng.base.BaseServiceImpl;
import com.javapandeng.mapper.ItemMapper;
import com.javapandeng.po.Item;
import com.javapandeng.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl extends BaseServiceImpl<Item> implements ItemService {
    @Autowired
    private ItemMapper itemMapper;

    @Override
    public BaseMapper<Item> getBaseMapper() {
        return itemMapper;
    }
}
