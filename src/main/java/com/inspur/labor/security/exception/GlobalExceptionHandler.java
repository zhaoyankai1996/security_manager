package com.inspur.labor.security.exception;

import com.inspur.labor.security.util.ErrorCodeEnum;
import com.inspur.labor.security.util.ResponseDTO;
import com.inspur.labor.security.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.StringJoiner;

/**
 * @ClassName GlobalExceptionHandler
 * @Description 全局异常处理
 * @Date 2022/4/26 14:48
 * @Author gengpeng
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    ResponseDTO handlerException(Exception e, HttpServletRequest request) {
        logger.error("请求异常", e);
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException err = (MethodArgumentNotValidException) e;
            FieldError fieldError = err.getBindingResult().getFieldError();
            return WebUtils.createFailureResponse(ErrorCodeEnum.S_REQ_PARAM_NULL, fieldError.getDefaultMessage());
        }
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException err = (ConstraintViolationException) e;
            StringJoiner sj = new StringJoiner(";");
            err.getConstraintViolations().forEach(x -> sj.add(x.getMessage()));
            return WebUtils.createFailureResponse(ErrorCodeEnum.S_REQ_PARAM_NULL, sj.toString());
        }
        if (e instanceof BindException) {
            BindException bindException = (BindException) e;
            List<FieldError> fieldErrors = bindException.getBindingResult().getFieldErrors();
            StringJoiner sj = new StringJoiner(";");
            fieldErrors.forEach(o -> sj.add(o.getDefaultMessage()));
            return WebUtils.createFailureResponse(ErrorCodeEnum.S_REQ_PARAM_NULL, sj.toString());
        }
        if (e instanceof Exception) {
            return WebUtils.createFailureResponse(ErrorCodeEnum.P_SYS_ERROR, e.getMessage());
        }
        return WebUtils.createFailureResponse(ErrorCodeEnum.P_SYS_ERROR, "请求异常");
    }

    /**
     * 处理自定义的业务异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = com.inspur.labor.security.exception.CustomException.class)
    public ResponseDTO bizExceptionHandler(HttpServletRequest req, com.inspur.labor.security.exception.CustomException e) {
        logger.error("发生业务异常！", e);
        return WebUtils.createFailureResponse(e.getErrorCode(), e.getMessage());
    }

}
