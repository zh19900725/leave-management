package com.management.leave.interceptor;

import com.management.leave.common.constats.Constants;
import com.management.leave.exception.Assert;
import com.management.leave.exception.ErrorInfo;
import com.management.leave.model.pojo.EmployeeInfo;
import com.management.leave.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                             Object object) {
        String path = httpServletRequest.getServletPath();
        String token = httpServletRequest.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            token = httpServletRequest.getParameter("Authorization");
        }
        log.info("path {} token is {}",path,token);
        // 执行认证
        Assert.assertNotEmpty(ErrorInfo.ERROR_TOKEN_NOT_FOUND, token);
        // 这个为了应对http 1.0的规范，Bearer 常见于 OAuth 和 JWT 授权
        if(token.contains("Bearer ")){
            token = token.substring(token.substring(0,7).length(),token.length());
        }
        EmployeeInfo employeeInfo = tokenService.tokenVerify(token);
        Assert.assertNotNull(ErrorInfo.ERROR_TOKEN_CHECK_FAILED, employeeInfo);
        httpServletRequest.setAttribute(Constants.USER_CACHE, employeeInfo);
        return true;
    }



}
