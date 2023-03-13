package com.javapandeng.service.impl;


import com.javapandeng.base.BaseMapper;
import com.javapandeng.base.BaseServiceImpl;
import com.javapandeng.mapper.EmployeeMapper;
import com.javapandeng.po.Employee;
import com.javapandeng.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends BaseServiceImpl<Employee> implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public BaseMapper<Employee> getBaseMapper() {
        return employeeMapper;
    }

}
