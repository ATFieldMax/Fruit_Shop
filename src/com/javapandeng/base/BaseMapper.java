package com.javapandeng.base;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


//基礎Mapper提供一些通用方法
public interface BaseMapper<T>{
	//新增
	int insert(T entity) ; 
      
    //根據id刪除
    void deleteById(Serializable id);  
    
    //根據實體類刪除
    void deleteByEntity(T entity);
    
    //根據Map刪除
    void deleteByMap(Map<String,Object> params);
    
    //根據實體類修改
    void update(T entity);

    //根據id修改
    void updateById(T entity);

    //根據Map查詢
    public List<T> listByMap(Map<String,Object> params);
    
    //查詢全部
    List<T> listAll();

    //查詢(根據屬性值為判斷條件)
    List<T> listAllByEntity(T entity);

    //根據id獲取一個實體
    T getById(Serializable id);

    //根據實體類查詢
    public T getByEntity(T entity);

    //批量新增
    public void insertBatch(List<T> list);

    //批量修改
    public void updateBatch(List<T> list);
    
    //==============================封装纯sql语法================================
    
    //查詢一個對象返回Map
    public Map<String,Object> getBySqlReturnMap(@Param("sql")String sql);
    
    //查詢一個對象返回實體類
    public T getBySqlReturnEntity(@Param("sql")String sql);
    
    //查詢對象返回Map列表
    public List<Map<String,Object>> listBySqlReturnMap(@Param("sql")String sql);
    
    //查詢對象返回實體列表
    public List<T> listBySqlReturnEntity(@Param("sql")String sql);
    
    //查詢分頁
    public List<T> findBySqlReturnEntity(@Param("sql")String sql);
    
    //通過SQL修改
    public void updateBySql(@Param("sql")String sql);
    
    //通過SQL刪除
    public void deleteBySql(@Param("sql")String sql);
}
