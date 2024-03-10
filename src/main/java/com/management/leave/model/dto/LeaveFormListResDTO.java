package com.management.leave.model.dto;

import lombok.Data;

import java.util.List;

/**
 * list of leave forms
 * @author zh
 */
@Data
public class LeaveFormListResDTO {
    /**
     * total page
     */
    private Long totalPage;
    /**
     * total rows
     */
    private Long totalRows;
    /**
     * page data list
     */
    private List<LeaveResDTO> forms;
}
