package com.management.leave.model.dto;

import lombok.Data;

/**
 * 请假请求参数
 * @author zh
 */
@Data
public class LeaveRequestDTO {
    /**
     * action about leave form
     */
    private String action;

    /**
     * When submit a leave form,please ignore this attribute
     * When edit a leave form, this field cannot be null.
     */
    private Integer formId;

    /**
     * leave start time,timeStamp ms
     */
    private Long startTime;
    /**
     * end start time ,timeStamp ms
     */
    private Long endTime;
    /**
     * leave reason
     */
    private String reason;

}
