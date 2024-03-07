package com.management.leave.controller;

import com.management.leave.api.LeaveService;
import com.management.leave.model.ApprovalReq;
import com.management.leave.model.LeaveFormListReq;
import com.management.leave.model.LeaveRequest;
import com.management.leave.model.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * leave controller
 * @author zh
 */
@RestController
@RequestMapping("/leave")
public class LeaveController implements LeaveService {

    @Override
    @PostMapping("/submit")
    public Result<String> submitLeaveForm(LeaveRequest req) {
        // 秒并发控制，避免数据重复入库
        // 插入请假单
        // 通知审批人
        return null;
    }

    @Override
    @GetMapping("/cancel")
    public Result<String> cancelLeaveForm(Integer id) {
        // 查询数据是否存在
        // 更新请假单状态
        return null;
    }

    @Override
    @PostMapping("/approval")
    public Result<String> approvalLeaveForm(ApprovalReq req) {
        // 查询请求单是否存在
        // 提交审批记录，更新请求单状态
        // 如果是1级审批通过，通知申请人和2级审批
        // 如果是2级审批通过，或者请求单被驳回，通知申请人
        return null;
    }

    @Override
    @PostMapping("/LeaveFormList")
    public Result<List<LeaveRequest>> LeaveFormList(LeaveFormListReq req) {
        return null;
    }


}
