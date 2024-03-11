package com.management.leave.exception;


/**
 * 自定义异常类
 * @author zh
 */
public class MyException extends RuntimeException {
    /**
     * 错误码
     */
    private int errorCode;
    /**
     * 错误描述
     */
    private String errorMsg;
    /**
     * 左标记符
     */
    private final String leftFlag="[";
    /**
     * 右标记符
     */
    private final String rightFlag="]";

    public MyException(Object errorInfo) {
        super(errorInfo.toString());
        if(errorInfo instanceof String &&  ((String) errorInfo).contains(leftFlag)){
            String code = ((String) errorInfo).split(rightFlag)[0].replace(leftFlag,"");
            errorCode = Integer.parseInt(code);
            errorMsg = ((String) errorInfo).replace(code,"").replace(leftFlag,"").replace(rightFlag,"").trim();
        } else {
            this.errorMsg = errorInfo.toString();
            this.errorCode = -1;
        }
    }

    public MyException(ErrorInfo errorInfo) {
        super(errorInfo.toString());
        this.errorCode = errorInfo.getErrorCode();
        this.errorMsg = errorInfo.getErrorMsg();
    }

    public MyException(int code, String msg) {
        super("[" + code + "] " + msg);
        this.errorCode = code;
        this.errorMsg = msg;
    }

    public MyException(ErrorInfo errorInfo,String msg) {
        super(errorInfo.toString() + ":" +msg);
        this.errorCode = errorInfo.getErrorCode();
        this.errorMsg = errorInfo.getErrorMsg()  + ":" +msg;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }
}
