package com.management.leave.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 请假请求参数
 * @author zh
 */
@Data
public class LeaveRequestDTO {
    /**
     * action about leave form
     */
    @NotBlank(message = "action can not be empty")
    private String action;

    /**
     * When submit a leave form,please ignore this attribute
     * When edit a leave form, this field cannot be null.
     */
    @NotNull(message = "formId can not be null")
    private Integer formId;

    /**
     * leave start time,timeStamp ms
     */
    @NotNull(message = "startTime can not be null")
    private Long startTime;
    /**
     * end start time ,timeStamp ms
     */
    @NotNull(message = "endTime can not be null")
    private Long endTime;
    /**
     * leave reason
     */
    @NotBlank(message = "reason can not be empty")
    private String reason;

}
