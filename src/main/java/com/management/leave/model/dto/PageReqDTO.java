package com.management.leave.model.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * page struct
 *
 * @author zh
 */
@Data
public class PageReqDTO {
    @NotNull(message = "pageSize cannot be null")
    @Max(value = 100, message = "pageSize cannot be greater than 100")
    @Min(value = 1, message = "pageSize cannot be less than 1")
    private Integer pageSize;

    @NotNull(message = "pageNo cannot be null")
    @Min(value = 1, message = "pageNo cannot be less than 1")
    private Integer pageNo;
}
