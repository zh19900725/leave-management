package com.management.leave.exception;

import com.management.leave.common.ErrorInfo;
import com.management.leave.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@ConditionalOnProperty(prefix = "switch", value = "globalException", havingValue = "true")
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MyException.class)
    public ResponseEntity<Result> bizExceptionHandler(MyException e) {
        log.error("business exception：", e);
        Result resultModel = Result.failure(e.getErrorMsg(), e.getErrorCode());
        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    /**
     * 处理空指针的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<Result> exceptionHandler(NullPointerException e) {
        log.error("NullPointerException happened :", e);
        Result resultModel = Result.failure(ErrorInfo.ERROR_10000);
        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }


    /**
     * process unKnow exception
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Result> exceptionHandler(Exception e) {
        log.error("unKnow exception :", e);
        Result resultModel = Result.failure(ErrorInfo.ERROR_10000);
        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    /**
     * no handler found exception
     *
     * @return result
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Result> constraintViolationExceptionHandler() {
        Result resultModel = Result.failure(ErrorInfo.ERROR_10000);
        return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
    }

}
