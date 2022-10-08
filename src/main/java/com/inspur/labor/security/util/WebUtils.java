package com.inspur.labor.security.util;

import org.apache.commons.lang3.StringUtils;


/**
 * 用于对ResponseDTO进行协具的方法类
 * <p>
 * P00000-成功;
 * <p>
 * P10007-请求的资源不存在;
 * <p>
 * P10001-系统异常;
 * <p>
 * C00001-为空;
 * <p>
 * S10019-系统中已存在
 * 从labor-uac迁移过来
 *
 * @author administrator
 */
public final class WebUtils {

    /**
     * 封装成功的返回值
     *
     * @param <T> 参数类型
     * @return 返回值
     */
    public static <T> ResponseDTO<T> createSuccessResponse() {
        ResponseDTO<T> dto = new ResponseDTO<>();
        dto.setCode(ErrorCodeEnum.P_SUCCESS.getCode());
        dto.setMsg("");
        dto.setData(null);
        return dto;
    }

    /**
     * 封装成功的返回值
     *
     * @param data 数据
     * @param <T>  参数类型
     * @return 返回值
     */
    public static <T> ResponseDTO<T> createSuccessResponse(T data) {
        ResponseDTO<T> dto = new ResponseDTO<>();
        dto.setCode(ErrorCodeEnum.P_SUCCESS.getCode());
        dto.setMsg("");
        dto.setData(data);
        return dto;
    }

    /**
     * 封装成功的返回值
     *
     * @param data 数据
     * @param <T>  参数类型
     * @return 返回值
     */
    public static <T> ResponseDTO<T> createSuccessResponse(ErrorCodeEnum errorCodeEnum, T data) {
        ResponseDTO<T> dto = new ResponseDTO<T>();
        dto.setCode(errorCodeEnum.getCode());
        dto.setMsg(errorCodeEnum.getMsg());
        dto.setData(data);
        return dto;
    }

    /**
     * 封装成功的返回值
     *
     * @param data 数据
     * @param <T>  参数类型
     * @return 返回值
     */
    public static <T> ResponseDTO<T> createSuccessResponse(ErrorCodeEnum errorCodeEnum, String message, T data) {
        ResponseDTO<T> dto = new ResponseDTO<T>();
        dto.setCode(errorCodeEnum.getCode());
        dto.setMsg(StringUtils.isBlank(message) ? errorCodeEnum.getMsg() : message);
        dto.setData(data);
        return dto;
    }

    /**
     * 封装失败的返回值
     *
     * @param errorCodeEnum 错误编码
     * @param message       信息
     * @param <T>           返回值类型
     * @return 返回值
     */
    public static <T> ResponseDTO<T> createFailureResponse(ErrorCodeEnum errorCodeEnum, String message) {
        ResponseDTO<T> dto = new ResponseDTO<>();
        dto.setCode(errorCodeEnum.getCode());
        dto.setMsg(StringUtils.isBlank(message) ? errorCodeEnum.getMsg() : message);
        dto.setData(null);
        return dto;
    }

    /**
     * 封装失败的返回值
     *
     * @param errorCodeEnum 错误编码
     * @param message       信息
     * @param <T>           返回值类型
     * @return 返回值
     */
    public static <T> ResponseDTO<T> createFailureResponse(ErrorCodeEnum errorCodeEnum, String message, T data) {
        ResponseDTO<T> dto = new ResponseDTO<>();
        dto.setCode(errorCodeEnum.getCode());
        dto.setMsg(message);
        dto.setData(data);
        return dto;
    }
}
