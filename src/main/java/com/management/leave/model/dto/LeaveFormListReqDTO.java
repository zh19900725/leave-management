package com.management.leave.model.dto;

import lombok.Data;

/**
 * list of leave forms
 * @author zh
 */
@Data
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
