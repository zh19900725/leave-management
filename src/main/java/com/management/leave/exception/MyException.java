package com.management.leave.exception;


/**
 * @author zh
 */
public class MyException extends RuntimeException {
    private static final long serialVersionUID = 4010950949705194814L;
    private int errorCode;
    private String errorMsg;
    private String leftFlag="[";
    private String rightFlag="]";
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
