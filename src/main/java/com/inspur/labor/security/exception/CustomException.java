package com.inspur.labor.security.exception;


import com.inspur.labor.security.util.ErrorCodeEnum;

/**
 * @ClassName CustomException
 * @Description 自定义异常
 * @Date 2022/4/26 14:47
 * @Author gengpeng
 */
public class CustomException extends RuntimeException {

    private final ErrorCodeEnum errorCode;
    private final String errorMsg;

    public CustomException(ErrorCodeEnum errorCodeEnum, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCodeEnum;
        this.errorMsg = errorMsg;
    }

    public CustomException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMsg());
        this.errorCode = errorCodeEnum;
        this.errorMsg = errorCodeEnum.getMsg();
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
