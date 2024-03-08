package com.management.leave.model.dto;

import com.management.leave.exception.ErrorInfo;
import lombok.Data;

/**
 * Unified return parameter format
 * @author zh
 */
@Data
public class ResultDTO<T> {
    private Integer code;
    private String msg;
    private T Data;

    public ResultDTO(T data) {
        this.code = 0;
        this.msg = "OK";
        Data = data;
    }

    public ResultDTO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> ResultDTO<T> success(T data) {
        return new ResultDTO<T>(data);
    }

    public static <T> ResultDTO<T> failure(String msg, int code) {
        return new ResultDTO<T>(code,msg);
    }

    public static <T> ResultDTO<T> failure(ErrorInfo err) {
        return new ResultDTO<T>(err.getErrorCode(),err.getErrorMsg());
    }

}
