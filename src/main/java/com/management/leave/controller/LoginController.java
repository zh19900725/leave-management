package com.management.leave.controller;

import com.alibaba.fastjson.JSONObject;
import com.management.leave.api.LoginServiceApi;
import com.management.leave.exception.Assert;
import com.management.leave.exception.ErrorInfo;
import com.management.leave.model.dto.LoginReq;
import com.management.leave.model.dto.ResultDTO;
import com.management.leave.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zh
 */
@RestController
@RequestMapping("/sys/auth")
public class LoginController implements LoginServiceApi {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    @Override
    public ResultDTO<String> login(LoginReq req) {
        // 参数校验
        Assert.assertNotEmpty(ErrorInfo.ERROR_PARAM_ERROR,req.getUserName());
        Assert.assertNotEmpty(ErrorInfo.ERROR_PARAM_ERROR,req.getMobile());
        Assert.assertNotEmpty(ErrorInfo.ERROR_PARAM_ERROR,req.getSmsCode());
        String token = loginService.login(req);
        if (!StringUtils.isEmpty(token)) {
            return ResultDTO.success(token);
        } else {
            return ResultDTO.failure(ErrorInfo.ERROR_UNKNOWN_ERROR);
        }
    }

    @PostMapping("/sendCode")
    @Override
    public ResultDTO<String> sendCode(@RequestBody(required = true) JSONObject phone) {
        // 参数校验
        String mobile = phone.getString("mobile");
        Assert.assertNotEmpty(ErrorInfo.ERROR_PARAM_ERROR, mobile);
        String code = loginService.sendCode(mobile);
        if (!StringUtils.isEmpty(code)) {
            return ResultDTO.success(code);
        } else {
            return ResultDTO.failure(ErrorInfo.ERROR_UNKNOWN_ERROR);
        }
    }
}
