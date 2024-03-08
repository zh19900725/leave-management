package com.management.leave.model.dto;

import lombok.Data;

/**
 * @author zh
 */
@Data
public class LoginReq {
    /**
     * 用户登录名
     */
    private String userName;
    /**
     * 手机验证码
     */
    private String smsCode;
    /**
     * 手机号
     */
    private String mobile;
}
