package com.management.leave.model;

import com.management.leave.common.StatusEnum;
import lombok.Data;

import java.util.Date;

/**
 * 请假请求参数
 * @author zh
 */
@Data
public class LeaveRequest {
    /**
     * employee user id
     */
    private Integer userId;
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
