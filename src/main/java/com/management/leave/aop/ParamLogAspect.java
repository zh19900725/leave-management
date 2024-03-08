package com.management.leave.aop;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.management.leave.common.constats.Constants.TRACE_ID;

/**
 * @author zh
 */
@Aspect
@Component
@Slf4j
public class ParamLogAspect {

    @Pointcut("within(com.management.leave.controller.LeaveController..*)")
    public void expression() {
    }

    @Before(value = "expression()")
    public void beforeMethod(JoinPoint joinPoint) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            String servletPath = request.getServletPath();
            if (joinPoint != null && joinPoint.getArgs() != null) {
                log.info("request path: {} request size {}", servletPath, joinPoint.getArgs().toString().length());
            }

        }
    }

    @AfterReturning(value = "expression()", returning = "result")
    public void afterReturning(Object result) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            String servletPath = request.getServletPath();
            String res = "";
            if (result != null) {
                res = JSONObject.toJSONString(result);
            }

            String startTime = MDC.get(TRACE_ID);
            long timeBetween = 0;
            if (!StringUtils.isEmpty(startTime)) {
                startTime = startTime.split("-")[0];
                timeBetween = System.currentTimeMillis() - Long.parseLong(startTime);
            }
            log.info("request path: {} ,request return: {}, time cost: {}ms", servletPath, res, timeBetween);
        }
    }
}
