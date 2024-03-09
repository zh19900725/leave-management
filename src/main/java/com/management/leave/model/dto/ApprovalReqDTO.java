package com.management.leave.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Approval Req
 * @author zh
 */
@Data
public class ApprovalReqDTO {
    /**
     * an id for a leave form
     */
    @NotNull(message = "formId can not be null")
    private Integer formId;

    /**
     * new status want to change
     */
    @NotBlank(message = "newStatus can not be empty")
    private String newStatus;

    /**
     * approval comments
     */
    @NotBlank(message = "comments can not be empty")
    @Length(max = 250)
    private String comments;

}
