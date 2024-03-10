package com.management.leave.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author zh
 */
@Getter
@ToString
@AllArgsConstructor
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
    ERROR_FORM_STATUS_IS_TERMINATION(10016, "form status is termination!"),
    ERROR_LOGIN_INFO_ERROR(10017, "phone or loginName error!"),
    ;
    private final int errorCode;
    private final String errorMsg;
}
