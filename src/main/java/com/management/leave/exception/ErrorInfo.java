package com.management.leave.exception;

public enum ErrorInfo {
    ERROR_UNKNOWN_ERROR(10000, "unknown error!"),
    ERROR_PARAM_ERROR(10001,"param error"),
    ERROR_PLEASE_LOGIN(10002, "please login!"),
    ERROR_LOGIN_TIMEOUT(10003, "login timeout!"),
    ERROR_NO_PERMISSION(10004, "you have no permission!"),
    ERROR_TOKEN_EXPIRED(10005, "token is expired!"),
    ERROR_TOKEN_CHECK_FAILED(10006, "token check failed!"),
    ERROR_TOKEN_NOT_FOUND(10007, "token not fount!"),
    ERROR_LEAVE_FORM_NOT_FOUND(10008, "leave form not found!"),
    ERROR_APPLICANT_NOT_EXIST(10009, "applicant not exist"),
    ERROR_LEAVE_IS_STILL_EXIST(10010, "leave form is still exist"),
    ERROR_CROSS_LEVEL_APPROVAL(10011, "Disable cross-level approval"),
    ERROR_ACTION_NOT_SUPPORT(10012, "action not support"),
    ERROR_USER_NOT_EXIST(10013, "user not exist"),
    ERROR_USER_IS_DELETE(10014, "user is deleted!"),
    ERROR_SMS_CODE(10015, " sms code check failed!"),
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
