package com.management.leave.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author zh
 */
@Data
public class LoginReqDTO {
    /**
     * 用户登录名
     */
    @NotBlank(message = "loginName cannot be empty")
    private String loginName;

    /**
     * 手机验证码
     */
    @NotBlank(message = "smsCode cannot be empty")
    private String smsCode;

    /**
     * 手机号
     */
    @NotBlank(message = "mobile cannot be empty")
    @Length(min = 11, max = 11, message = "手机号格式有误")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    private String mobile;
}
