package com.management.leave.common;

public enum ErrorInfo {
    ERROR_10000(10000, "unknown error!"),
    ERROR_10001(10001,"param error"),
    ERROR_10002(10002, "please login!"),
    ERROR_10003(10003, "login timeout!"),
    ERROR_10004(10004, "you have no permission!"),

    ;
    private int errorCode;
    private String errorMsg;
    ErrorInfo(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "[" + this.errorCode + "] " + this.errorMsg;
    }
}
