package com.management.leave.api;

import com.management.leave.model.dto.LoginReqDTO;
import com.management.leave.model.dto.ResultDTO;
import com.management.leave.model.dto.SmsReqDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * function about system login
 * @author zh
 */
@Api(value = "LoginServiceApi", tags = {"鉴权接口"})
public interface LoginServiceApi {
    /**
     * system login
     * @param req we can get some login info from this param
     * @return token
     */
    @ApiOperation("登录操作")
    ResultDTO<String> login(LoginReqDTO req);


    /**
     * get phone sms code
     * @param req
     * @return
     */
    @ApiOperation("获取手机验证码")
    ResultDTO<Boolean> sendCode(SmsReqDTO req);

}
