package com.javapandeng.service.impl;

import com.javapandeng.base.BaseMapper;
import com.javapandeng.base.BaseServiceImpl;
import com.javapandeng.mapper.ItemCategoryMapper;
import com.javapandeng.po.ItemCategory;
import com.javapandeng.service.ItemCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemCategoryServiceImpl extends BaseServiceImpl<ItemCategory> implements ItemCategoryService {
    @Autowired
    private ItemCategoryMapper itemCategoryMapper;

    @Override
    public BaseMapper<ItemCategory> getBaseMapper() {
        return itemCategoryMapper;
    }
}
