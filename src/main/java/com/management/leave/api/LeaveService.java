package com.management.leave.api;

import com.management.leave.model.ApprovalReq;
import com.management.leave.model.LeaveFormListReq;
import com.management.leave.model.LeaveRequest;
import com.management.leave.model.Result;

import java.util.List;

/**
 * function about leave request
 * @author zh
 */
public interface LeaveService {
    /**
     * submit leave request
     * @param req
     * @return
     */
    Result<String> submitLeaveForm(LeaveRequest req);

    /**
     * cancel leave request
     * @param formId the number of request form
     * @return
     */
    Result<String> cancelLeaveForm(Integer formId);

    /**
     * approval leave request
     * @param req the approval request
     * @return
     */
    Result<String> approvalLeaveForm(ApprovalReq req);

    /**
     * list for request records
     * @return
     */
    Result<List<LeaveRequest>>  LeaveFormList(LeaveFormListReq req);
}
