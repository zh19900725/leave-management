package com.management.leave.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.leave.db.entity.TEmployeeEntity;
import com.management.leave.db.service.impl.TEmployeeServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zh
 */
@Service
public class EmployeeService {
    @Autowired
    private TEmployeeServiceDao employeeServiceDao;

    /**
     * select employee by phone
     * @param phone
     * @return
     */
    public TEmployeeEntity queryEmployeeByMobile(String phone){
        QueryWrapper<TEmployeeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", phone);
        TEmployeeEntity tEmployeeEntity = employeeServiceDao.getBaseMapper().selectOne(wrapper);
        return tEmployeeEntity;
    }

    public TEmployeeEntity queryEmployeeById(String id){
        TEmployeeEntity tEmployeeEntity = employeeServiceDao.getBaseMapper().selectById(id);
        return tEmployeeEntity;
    }

}
