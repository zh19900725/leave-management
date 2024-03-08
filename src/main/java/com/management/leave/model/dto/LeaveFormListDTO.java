package com.management.leave.model.dto;

import com.management.leave.common.enums.StatusEnum;
import lombok.Data;

/**
 * list of leave forms
 * @author zh
 */
@Data
public class LeaveFormListDTO {
    /**
     * employee user id
     */
    private Integer userId;
    /**
     * leave start time,timeStamp ms
     */
    private long startTime;
    /**
     * end start time,timeStamp ms
     */
    private long endTime;
    /**
     * form status
     */
    private StatusEnum status;
}
