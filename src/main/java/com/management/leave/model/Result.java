package com.management.leave.model;

import com.management.leave.common.ErrorInfo;
import lombok.Data;

/**
 * Unified return parameter format
 * @author zh
 */
@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T Data;

    public Result(T data) {
        this.code = 0;
        this.msg = "OK";
        Data = data;
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    public static <T> Result<T> failure(String msg, int code) {
        return new Result<T>(code,msg);
    }

    public static <T> Result<T> failure(ErrorInfo err) {
        return new Result<T>(err.getErrorCode(),err.getErrorMsg());
    }

}
