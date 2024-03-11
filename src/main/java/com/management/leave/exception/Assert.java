package com.management.leave.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 自定义断言工具
 * @author zh
 */
@Log4j2
public class Assert {

    public static void assertNotNull(ErrorInfo msg, Object obj) {
        assertTrue(msg, !isNull(obj));
    }

    public static void assertNotEmpty(ErrorInfo msg, Object obj) {
        assertTrue(msg, !isNull(obj));
    }

    public static void assertTrue(ErrorInfo msg, boolean condition) {
        if (!condition) {
            throw throwException(msg);
        }
    }

    private static MyException throwException(ErrorInfo errorEnum) {
        return errorEnum != null ? new MyException(errorEnum) : new MyException(ErrorInfo.ERROR_UNKNOWN_ERROR);
    }

    private static Boolean isNull(Object obj) {
        if (obj == null) {
            return Boolean.TRUE;
        }

        if (obj instanceof Collection) {
            if (CollectionUtils.isEmpty((Collection<?>) obj)) {
                return Boolean.TRUE;
            }
        }

        if (obj instanceof String) {
            if (StringUtils.isEmpty(obj)) {
                return Boolean.TRUE;
            }
        }

        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }

        return Boolean.FALSE;

    }

}
