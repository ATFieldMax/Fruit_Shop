package com.javapandeng.base;


import com.github.pagehelper.PageHelper;
import com.javapandeng.utils.Pager;
import com.javapandeng.utils.SystemContext;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class BaseServiceImpl<T> implements BaseService<T>{

    private BaseMapper<T> baseMapper;
    public abstract BaseMapper<T> getBaseMapper();
    
    //新增
	public int insert(T entity) {
		return this.getBaseMapper().insert(entity);
	}

	//根據id刪除
	public void deleteById(Serializable id) {
		this.getBaseMapper().deleteById(id);
	}


	//根據實體類刪除
	public void deleteByEntity(T entity) {
		this.getBaseMapper().deleteByEntity(entity);
	}

	//根據Map刪除
	public void deleteByMap(Map<String, Object> params) {
		this.getBaseMapper().deleteByMap(params);
	}

	//根據實體類修改
	public void update(T entity) {
		this.getBaseMapper().update(entity);
	}

	//根據id修改
	public void updateById(T entity) {
         this.getBaseMapper().updateById(entity);
	}

	//根據Map查詢
	public List<T> listByMap(Map<String, Object> params) {
		return this.getBaseMapper().listByMap(params);
	}

	//查詢全部
	public List<T> listAll() {
		return this.getBaseMapper().listAll();
	}

	//查詢(根據屬性值為判斷條件)
	public List<T> listAllByEntity(T entity) {
		return this.getBaseMapper().listAllByEntity(entity);
	}

	//根據id獲取一個實體
	/*
	public T load(Serializable id) {
		return this.getBaseMapper().load(id);
	}*/

	//根據id獲取一個實體
	public T getById(Serializable id) {
		return this.getBaseMapper().getById(id);
	}

	//根據Map查詢
	/*
	public T getByMap(Map<String, Object> params) {
		return this.getBaseMapper().getByMap(params);
	}*/

	//根據實體類查詢
	public T getByEntity(T entity) {
		return this.getBaseMapper().getByEntity(entity);
	}

	//=======================以下是分页方法================================

	 /**
     * 默认 sqlId find是分页
     */
	public Pager<T> findByMap(Map<String, Object> params) {
		/**
		 * 执行分页
		 */
    	Integer pageSize = SystemContext.getPageSize();
		Integer pageOffset = SystemContext.getPageOffset();
		if(pageOffset==null||pageOffset<0) pageOffset = 0;
		if(pageSize==null||pageSize<0) pageSize = 15;
		String order = SystemContext.getOrder();
		String sort = SystemContext.getSort();
		Integer pageNum = null;
		if(pageOffset == 0){
			pageNum = 1;
		}else{
			pageNum = pageOffset/pageSize+1;
		}
		PageHelper.startPage(pageNum, pageSize);
		//Pager<T> pages = new Pager<T>(this.getBaseMapper().findByMap(params));
		Pager<T> pages = new Pager<T>(this.getBaseMapper().listByMap(params));
    	return pages;
	}
	
	/**
     * 通过对象查询分页
     * @param entity
     * @return
     */
    public Pager<T> findByEntity(T entity){
    	/**
		 * 执行分页
		 */
    	Integer pageSize = SystemContext.getPageSize();
		Integer pageOffset = SystemContext.getPageOffset();
		if(pageOffset==null||pageOffset<0) pageOffset = 0;
		if(pageSize==null||pageSize<0) pageSize = 15;
		String order = SystemContext.getOrder();
		String sort = SystemContext.getSort();
		Integer pageNum = null;
		if(pageOffset == 0){
			pageNum = 1;
		}else{
			pageNum = pageOffset/pageSize+1;
		}
		PageHelper.startPage(pageNum, pageSize);
		//Pager<T> pages = new Pager<T>(this.getBaseMapper().findByEntity(entity));
		Pager<T> pages = new Pager<T>(this.getBaseMapper().listAllByEntity(entity));
    	return pages;
    }

	//批量新增
	 public void insertBatch(List<T> list) {
		 this.getBaseMapper().insertBatch(list);
	}

	//批量修改
	public void updateBatch(List<T> list) {
		 this.getBaseMapper().updateBatch(list);
	}

	//=====================自定义sql=========================================

	//查詢一個對象返回Map
	public Map<String, Object> getBySqlReturnMap(String sql) {
		
		return  this.getBaseMapper().getBySqlReturnMap(sql);
	}

	//查詢一個對象返回實體類
	public T getBySqlReturnEntity(String sql) {
		return this.getBaseMapper().getBySqlReturnEntity(sql);
	}

	//查詢對象返回Map列表
	public List<Map<String, Object>> listBySqlReturnMap(String sql) {
		return this.getBaseMapper().listBySqlReturnMap(sql);
	}

	//查詢對象返回實體列表
	public List<T> listBySqlReturnEntity(String sql) {
		return this.getBaseMapper().listBySqlReturnEntity(sql);
	}

	//查詢分頁
	public Pager<T> findBySqlReturnEntity(String sql) {
		//執行分頁
    	Integer pageSize = SystemContext.getPageSize();
		Integer pageOffset = SystemContext.getPageOffset();
		if(pageOffset==null||pageOffset<0) pageOffset = 0;
		if(pageSize==null||pageSize<0) pageSize = 15;
		String order = SystemContext.getOrder();
		String sort = SystemContext.getSort();
		Integer pageNum = null;
		if(pageOffset == 0){
			pageNum = 1;
		}else{
			pageNum = pageOffset/pageSize+1;
		}
		PageHelper.startPage(pageNum, pageSize);
		Pager<T> pages = new Pager<T>(this.getBaseMapper().findBySqlReturnEntity(sql));
    	return pages;
	}

	//使用List查詢分頁
	public Pager<T> createPagerEntity(){
		//執行分頁
		Integer pageSize = SystemContext.getPageSize();
		Integer pageOffset = SystemContext.getPageOffset();
		if(pageOffset==null||pageOffset<0) pageOffset = 0;
		if(pageSize==null||pageSize<0) pageSize = 15;
		String order = SystemContext.getOrder();
		String sort = SystemContext.getSort();
		Integer pageNum = null;
		if(pageOffset == 0){
			pageNum = 1;
		}else{
			pageNum = pageOffset/pageSize+1;
		}
		PageHelper.startPage(pageNum, pageSize);
		Pager<T> pages = new Pager<T>();

		return pages;
	}

	//通過SQL修改
	public void updateBySql(String sql) {
		this.getBaseMapper().updateBySql(sql);
	}

	//通過SQL刪除
	public void deleteBySql(String sql) {
		this.getBaseMapper().deleteBySql(sql);
	}

	//判断空
	public boolean isEmpty(String str) {
		return (null == str) || (str.trim().length() <= 0);
	}

	public boolean isEmpty(Character cha) {
		return (null == cha) || cha.equals(' ');
	}

	public boolean isEmpty(Object obj) {
		return (null == obj);
	}

	public boolean isEmpty(Object[] objs) {
		return (null == objs) || (objs.length <= 0);
	}

	public boolean isEmpty(Collection<?> obj) {
		return (null == obj) || obj.isEmpty();
	}

	public boolean isEmpty(Set<?> set) {
		return (null == set) || set.isEmpty();
	}

	public boolean isEmpty(Serializable obj) {
		return null == obj;
	}

	public boolean isEmpty(Map<?, ?> map) {
		return (null == map) || map.isEmpty();
	}
}
