package com.management.leave.common.constats;

/**
 * some constant params
 * @author zh
 */
public class Constants {
    /**
     * trace log id
     */
    public final static String TRACE_ID = "TraceId";
    /**
     * 用户登录信息缓存
     */
    public final static String USER_CACHE = "user-cache";
    /**
     * 验证码接口并发锁redisKey前缀
     */
    public final static String SEND_CODE_LOCK = "SEND_CODE_LOCK";
    /**
     * 验证码缓存
     */
    public final static String LOGIN_PHONE_CODE = "LOGIN_PHONE_CODE_";
    /**
     * token 缓存
     */
    public final static String LOGIN_TOKEN = "LOGIN_TOKEN";
    public final static Integer TIME_BETWEEN = 10;
}
