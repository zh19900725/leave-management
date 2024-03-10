package com.management.leave.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * list of leave forms
 * @author zh
 */
@Data
@ToString(callSuper = true)
public class LeaveFormListReqDTO extends PageReqDTO {
    /**
     * applicant id
     */
    private Integer applicantId;

    /**
     * approver id
     */
    private Integer approverId;

    /**
     * leave start time,timeStamp ms
     */
    private Long startTime;
    /**
     * end start time,timeStamp ms
     */
    private Long endTime;
    /**
     * form status
     */
    private String status;
}
