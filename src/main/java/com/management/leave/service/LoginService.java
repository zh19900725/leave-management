package com.management.leave.service;

import com.management.leave.common.constats.Constants;
import com.management.leave.dao.entity.EmployeeEntity;
import com.management.leave.exception.Assert;
import com.management.leave.exception.ErrorInfo;
import com.management.leave.model.dto.LoginReqDTO;
import com.management.leave.model.pojo.EmployeeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author zh
 */
@Service
@Slf4j
public class LoginService {
    @Autowired
    TokenService tokenService;
    @Autowired
    StringRedisTemplate redis;
    @Autowired
    EmployeeService employeeService;


    /**
     * user login
     *
     * @param req
     * @return
     */
    public String login(LoginReqDTO req) {
        log.info("login info {}", req);
        // 查询验证码是否存在
        String key = Constants.LOGIN_PHONE_CODE + req.getMobile() + "_" + req.getSmsCode();
        String s = redis.opsForValue().get(key);
        Assert.assertNotEmpty(ErrorInfo.ERROR_SMS_CODE, s);
        Assert.assertNotEmpty(ErrorInfo.ERROR_SMS_CODE, s.equals(req.getMobile()));

        // 根据手机号和用户名查询用户是否存在（手机号是唯一主键）
        EmployeeEntity employee = employeeService.queryEmployeeByMobile(req.getMobile());
        Assert.assertNotNull(ErrorInfo.ERROR_USER_NOT_EXIST, employee);
        Assert.assertTrue(ErrorInfo.ERROR_USER_IS_DELETE, employee.getRowStatus() == 0);
        Assert.assertTrue(ErrorInfo.ERROR_LOGIN_INFO_ERROR, employee.getLoginName().equals(req.getLoginName()));

        // 生成token
        EmployeeInfo employeeInfo = new EmployeeInfo();
        employeeInfo.setUserId(String.valueOf(employee.getId()));
        employeeInfo.setUserName(employee.getEmployeeName());
        String token = tokenService.getToken(employeeInfo, 24);
        log.info("login token {}", token);
        return token;
    }


    /**
     * send phone code
     *
     * @param mobile
     * @return
     */
    public void sendCode(String mobile) {
        log.info("send code req {}", mobile);
        //setNX 并发锁防止频繁请求验证码,锁定5分钟
        String lockKey = Constants.SEND_CODE_LOCK + mobile;
        redis.opsForValue().setIfAbsent(lockKey, "1");
        redis.expire(lockKey, 5, TimeUnit.MINUTES);

        // 生成短信验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000 + 1));

        // todo 这里发送手机验证码，用伪代码代替
        log.info("call the sms service ...");
        log.info("a sms code {} send to {} success!", code, mobile);

        // sms code存入缓存，1分钟有效期
        redis.opsForValue().set(Constants.LOGIN_PHONE_CODE + mobile + "_" + code, mobile, 60, TimeUnit.SECONDS);
    }
}
