package com.management.leave.controller;

import com.management.leave.api.LoginServiceApi;
import com.management.leave.exception.ErrorInfo;
import com.management.leave.model.dto.LoginReqDTO;
import com.management.leave.model.dto.ResultDTO;
import com.management.leave.model.dto.SmsReqDTO;
import com.management.leave.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResultDTO<String> login(@Validated LoginReqDTO req) {
        String token = loginService.login(req);
        if (!StringUtils.isEmpty(token)) {
            return ResultDTO.success(token);
        } else {
            return ResultDTO.failure(ErrorInfo.ERROR_UNKNOWN_ERROR);
        }
    }

    @PostMapping("/sendCode")
    @Override
    public ResultDTO<Boolean> sendCode(@Validated SmsReqDTO req) {
        loginService.sendCode(req.getMobile());
        return ResultDTO.success(true);
    }
}
