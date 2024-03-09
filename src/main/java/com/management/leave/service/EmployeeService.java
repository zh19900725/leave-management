package com.management.leave.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.leave.dao.entity.EmployeeEntity;
import com.management.leave.dao.service.impl.EmployeeServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zh
 */
@Service
public class EmployeeService {
    @Autowired
    private EmployeeServiceDao employeeServiceDao;

    /**
     * select employee by phone
     * @param phone
     * @return
     */
    public EmployeeEntity queryEmployeeByMobile(String phone){
        QueryWrapper<EmployeeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", phone);
        wrapper.eq("row_status", 0);
        EmployeeEntity EmployeeEntity = employeeServiceDao.getBaseMapper().selectOne(wrapper);
        return EmployeeEntity;
    }

    /**
     * query employee by Id
     * @param id
     * @return
     */
    public EmployeeEntity queryEmployeeById(String id){
        EmployeeEntity EmployeeEntity = employeeServiceDao.getBaseMapper().selectById(id);
        return EmployeeEntity;
    }

}
