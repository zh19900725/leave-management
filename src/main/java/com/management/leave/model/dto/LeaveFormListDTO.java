package com.management.leave.model.dto;

import com.management.leave.common.enums.StatusEnum;
import lombok.Data;

import java.util.Date;

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
}
