package com.management.leave.model.dto;

import com.management.leave.common.enums.StatusEnum;
import lombok.Data;

/**
 * Approval Req
 * @author zh
 */
@Data
public class ApprovalDTO {
    /**
     * an id for a leave form
     */
    private Integer formId;

    /**
     * new status want to change
     */
    private StatusEnum changeStatus;

    /**
     * approval comments
     */
    private String comments;

}
