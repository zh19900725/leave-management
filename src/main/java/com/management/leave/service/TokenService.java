package com.management.leave.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.management.leave.dao.entity.EmployeeEntity;
import com.management.leave.exception.Assert;
import com.management.leave.exception.ErrorInfo;
import com.management.leave.exception.MyException;
import com.management.leave.model.pojo.EmployeeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author zh
 */
@Service
@Slf4j
public class TokenService {
    @Autowired
    EmployeeService employeeService;

    /**
     * 获取登录token
     * @param employeeInfo  jwtParam
     * @param expired 过期时间,单位小时
     * @return
     */
    public String getToken(EmployeeInfo employeeInfo, long expired) {
        String token = "";
        Date date = new Date(System.currentTimeMillis() + expired*3600*1000);
        log.info("token will expired at:{}", date.toString());
        token = JWT.create()
                .withKeyId(employeeInfo.getUserName())
                .withIssuedAt(new Date())
                .withClaim("jwtParam", JSON.toJSONString(employeeInfo))
                .withExpiresAt(date)
                .sign(Algorithm.HMAC256(employeeInfo.getUserId()));
        return token;
    }

    /**
     * check token
     * @param token
     * @return
     */
    public EmployeeInfo tokenVerify(String token) {
        EmployeeInfo employeeInfo = null;
        try {
            log.debug("tokenVerify token = [{}]", token);
            DecodedJWT decode = JWT.decode(token);
            String jwtParamStr = decode.getClaim("jwtParam").as(String.class);
            if (StringUtils.isEmpty(jwtParamStr)) {
                throw new MyException(ErrorInfo.ERROR_TOKEN_EXPIRED);
            }
            employeeInfo = JSONObject.parseObject(jwtParamStr, EmployeeInfo.class);
            // FIXME 其实这里最好是去查redis缓存,不过员工信息一般是入职时录入的，请假系统只是demo级暂时不考虑redis缓存
            EmployeeEntity employeeEntity = employeeService.queryEmployeeById(employeeInfo.getUserId());
            Assert.assertNotNull(ErrorInfo.ERROR_USER_NOT_EXIST,employeeEntity);
            Assert.assertTrue(ErrorInfo.ERROR_USER_IS_DELETE,employeeEntity.getRowStatus()==0);
        } catch (JWTDecodeException j) {
            throw new MyException(ErrorInfo.ERROR_TOKEN_CHECK_FAILED);
        }
        return employeeInfo;
    }


}
