package com.management.leave.task;

import com.management.leave.dao.entity.EmployeeEntity;
import com.management.leave.dao.entity.LeaveFormEntity;
import com.management.leave.service.EmployeeService;
import com.management.leave.service.LeaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is a compensation mechanism that sends regular emails to remind approvers of unapproved orders
 * @author zh
 */
@Component
@Slf4j
public class LeaveFormTask {
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private EmployeeService employeeService;

    /**
     * a scheduled task. For orders that have not been approved, send regular emails to remind the approver
     */
    @Scheduled(cron = "0 30 10 * * ?")
    public void  noticeTask(){
        // 针对7日以内没有审批的订单，每天早上10:30分发起一次邮件通知，提醒审批人审批邮件，超过7天则不再提醒
        // 创建一个Calendar对象，并设置为当前时间
        Calendar calendar = Calendar.getInstance();
        // 减去7天（1周）
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        // 获取一周前的日期
        Date oneWeekAgo = calendar.getTime();
        List<LeaveFormEntity> forms = leaveService.getUnfinishedForm(oneWeekAgo, new Date());
        // 按当前操作人分类
        Map<String, List<LeaveFormEntity>> collect = forms.stream()
                .collect(Collectors.groupingBy(LeaveFormEntity::getCurOperator));
        // 通知当前操作人有待审批的休假单
        collect.forEach((operatorId,list)->{
            if (CollectionUtils.isEmpty(list)){
                return;
            }
            // 查询操作人信息
            EmployeeEntity operator = employeeService.queryEmployeeById(operatorId);
            String email =  operator.getEmail();
            String employeeName =  operator.getEmployeeName();
            // TODO: 发起邮件通知，这里有伪代码代替
            log.info("send email to notice:dear {} , you have {} need to approve",employeeName, list.size());
        });
    }

}
