package com.inspur.labor.security.util;

import java.io.Serializable;

/**
 * 数据响应的对象
 *
 * @author sangcg
 * @version V1.0
 * @Date 2017/3/8 15:00
 * @Description 描述
 */
public class ResponseDTO<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 响应编码
     */
    private String code;

    /**
     * 响应描述
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;


    public ResponseDTO() {

    }

    public ResponseDTO(ErrorCodeEnum errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}