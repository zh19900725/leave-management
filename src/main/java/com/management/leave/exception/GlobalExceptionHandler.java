package com.management.leave.exception;

import com.management.leave.model.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理
 * @author zh
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 处理自定义的业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MyException.class)
    public ResponseEntity<ResultDTO> bizExceptionHandler(MyException e) {
        log.error("business exception：", e);
        ResultDTO resultModel = ResultDTO.failure(e.getErrorMsg(), e.getErrorCode());
        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    /**
     * 处理空指针的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<ResultDTO> exceptionHandler(NullPointerException e) {
        log.error("NullPointerException happened :", e);
        ResultDTO resultModel = ResultDTO.failure(ErrorInfo.ERROR_UNKNOWN_ERROR);
        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }


    /**
     * validator校验异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ResultDTO> handleValidException(MethodArgumentNotValidException e) {
        log.error("dhandleValidException,ata check error {}，exception class:{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        StringBuffer errorMsg = new StringBuffer();
        bindingResult.getFieldErrors().forEach(item->{
            if (!errorMsg.toString().contains(item.getDefaultMessage())) {
                errorMsg.append(item.getDefaultMessage()).append(",");
            }
        });
        ResultDTO resultModel = ResultDTO.failure(errorMsg.substring(0,errorMsg.length()-1),ErrorInfo.ERROR_PARAM_ERROR.getErrorCode());
        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    /**
     * validator校验异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<ResultDTO> handleBindException(BindException e) {
        log.error("handleBindException,data check error {}，exception class:{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        StringBuffer errorMsg = new StringBuffer();
        bindingResult.getFieldErrors().forEach(item->{
            if (!errorMsg.toString().contains(item.getDefaultMessage())) {
                errorMsg.append(item.getDefaultMessage()).append(",");
            }
        });
        ResultDTO resultModel = ResultDTO.failure(errorMsg.substring(0,errorMsg.length()-1),ErrorInfo.ERROR_PARAM_ERROR.getErrorCode());
        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    /**
     * process unKnow exception
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResultDTO> exceptionHandler(Exception e) {
        log.error("unKnow exception :", e);
        ResultDTO resultModel = ResultDTO.failure(ErrorInfo.ERROR_UNKNOWN_ERROR);
        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    /**
     * no handler found exception
     *
     * @return result
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResultDTO> constraintViolationExceptionHandler() {
        ResultDTO resultModel = ResultDTO.failure(ErrorInfo.ERROR_UNKNOWN_ERROR);
        return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
    }

}
