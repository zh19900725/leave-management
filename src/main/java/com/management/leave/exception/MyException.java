package com.management.leave.exception;


import com.management.leave.common.ErrorInfo;

public class MyException extends RuntimeException {
    private static final long serialVersionUID = 4010950949705194814L;
    private int errorCode;
    private String errorMsg;

    public MyException(Object errorInfo) {
        super(errorInfo.toString());
        if(errorInfo instanceof String &&  ((String) errorInfo).contains("[")){
            String code = ((String) errorInfo).split("]")[0].replace("[","");
            errorCode = Integer.parseInt(code);
            errorMsg = ((String) errorInfo).replace(code,"").replace("[","").replace("]","").trim();
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

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }
}
