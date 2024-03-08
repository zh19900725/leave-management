package com.management.leave.model.dto;

import com.management.leave.common.enums.ActionEnum;
import com.management.leave.common.enums.StatusEnum;
import lombok.Data;

import java.util.Date;

/**
 * 请假请求参数
 * @author zh
 */
@Data
public class LeaveRequestDTO {
    /**
     * action about leave form
     */
    private ActionEnum action;

    /**
     * When submit a leave form,please ignore this attribute
     * When edit a leave form, this field cannot be null.
     */
    private Integer formId;

    /**
     * employee user id
     */
    private String applicantId;
    /**
     * leave start time
     */
    private Date startTime;
    /**
     * end start time
     */
    private Date endTime;
    /**
     * form status
     */
    private StatusEnum status;
    /**
     * leave reason
     */
    private String reason;

}
