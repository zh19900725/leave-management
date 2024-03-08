package com.management.leave.service;

import com.management.leave.base.BaseSpringTest;
import com.management.leave.common.enums.ActionEnum;
import com.management.leave.common.enums.StatusEnum;
import com.management.leave.db.entity.TLeaveFormEntity;
import com.management.leave.model.dto.LeaveRequestDTO;
import com.management.leave.model.pojo.EmployeeInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 白盒测试
 */
@Slf4j
public class LeaveServiceTest extends BaseSpringTest {
    @Autowired
    LeaveService leaveService;
    static EmployeeInfo employeeInfo = new EmployeeInfo();
    static LeaveRequestDTO leaveRequestDTO = new LeaveRequestDTO();

    static {
        // mock login
        employeeInfo.setUserId("1");
        employeeInfo.setUserName("aarronzhang");

        // mock leave submit data
        LeaveRequestDTO leaveRequestDTO = new LeaveRequestDTO();
        leaveRequestDTO.setStartTime(1709888003086l);
        leaveRequestDTO.setEndTime(1709887003087l);
    }

    @Test
    public void edit() {
        // 测试正常场景
        leaveRequestDTO.setAction(ActionEnum.SUBMIT.getCode());
        leaveRequestDTO.setFormId(2);
        leaveRequestDTO.setStartTime(1709888003087l);
        int i = leaveService.edit(leaveRequestDTO, employeeInfo);
        Assert.isTrue(i>0);

        // 测试异常场景，修改一个已经被删除的请假单，应该报错或返回值位0
        leaveRequestDTO.setFormId(1);
        i = leaveService.edit(leaveRequestDTO, employeeInfo);
        Assert.isTrue(i==0);
    }

    @Test
    public void cancelOrDel() {
        int i = 0;
        // 撤销已经存在的单子
        i=leaveService.cancelOrDel(2,"aarronzhang", StatusEnum.CANCEL);
        Assert.isTrue(i>0);
        // 撤销不存在的单子
        i=leaveService.cancelOrDel(1000,"aarronzhang", StatusEnum.CANCEL);
        Assert.isTrue(i==0);
        // 删除已经存在的单子
        i=leaveService.cancelOrDel(2,"aarronzhang", StatusEnum.CLOSE);
        Assert.isTrue(i>0);
        // 删除不存在的单子
        i=leaveService.cancelOrDel(1000,"aarronzhang", StatusEnum.CLOSE);
        Assert.isTrue(i==0);

    }

    @Test
    public void approval() {
        // 这里用knife4j做黑盒测试
    }

    @Test
    public void insert() {
        // 异常场景，提交一个已经提交过的请假单
        leaveRequestDTO.setAction(ActionEnum.SUBMIT.getCode());
        leaveRequestDTO.setReason("i do not want work");
        leaveService.insert(employeeInfo,leaveRequestDTO);

        // 正常场景，提交一个新的请假单
        leaveRequestDTO.setStartTime(1709628803000l);
        leaveRequestDTO.setEndTime(1709715203000l);
    }

    @Test
    public void getLeaveFormList() {
        // 这里用knife4j做黑盒测试
    }

    @Test
    public void checkExist(){
        // 测试正常场景
        LeaveRequestDTO leaveRequestDTO = new LeaveRequestDTO();
        leaveRequestDTO.setAction(ActionEnum.SUBMIT.getCode());
        leaveRequestDTO.setStartTime(1709888003086l);
        leaveRequestDTO.setEndTime(1709887003086l);
        List<TLeaveFormEntity> tLeaveFormEntities = leaveService.checkExist(leaveRequestDTO, 1);
        Assert.isTrue(CollectionUtils.isEmpty(tLeaveFormEntities));
        // 异常异常场景 ，将这个相同数据再插入一次，应该要报错
        tLeaveFormEntities = leaveService.checkExist(leaveRequestDTO, 1);
        Assert.isTrue(!CollectionUtils.isEmpty(tLeaveFormEntities));
        log.info("tLeaveFormEntities {}",tLeaveFormEntities);
    }
}