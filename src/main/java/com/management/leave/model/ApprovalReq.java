package com.management.leave.model;

import com.management.leave.common.StatusEnum;
import lombok.Data;

/**
 * Approval Req
 * @author zh
 */
@Data
public class ApprovalReq {
    /**
     * an id for a leave form
     */
    private Integer formId;
    /**
     * the employee who want to approval a leave form
     */
    private String approvalUserName;

    /**
     * approval time
     */
    private String approvalTime;

    /**
     * new status want to change
     */
    private StatusEnum changeStatus;

    /**
     * approval comments
     */
    private String comments;

}
