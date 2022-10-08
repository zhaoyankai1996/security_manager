package com.inspur.labor.security.util;

/**
 * 错误编码
 *
 * @author 赵彦凯
 */
public enum ContestCodeEnum {

    /**
     * 可以申请
     */
    VOL_CAN_APPLY(0, "可以申请"),
    /**
     * 已存在志愿者
     */
    VOL_HAS(1, "已存在志愿者"),
    /**
     * 已提交审核
     */
    VOL_WAIT_CHECK(2, "已提交审核"),
    /**
     * 审核不通过
     */
    VOL_NOT_PASS(3, "审核不通过"),
    /**
     * 审核不通过
     */
    VOL_LIMIT_SVC(4, "限制服务"),

    RES_WAIT_PUBLISH(1, "待发布"),

    RES_HAS_PUBLISH(2, "已发布"),

    RES_CANCEL_PUBLISH(3, "取消发布");


    /**
     * 默认描述
     */
    private String msg;

    /**
     * 编码
     */
    private Integer code;

    ContestCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 获取错误编码默认消息
     *
     * @return
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 获取错误码
     *
     * @return
     */
    public Integer getCode() {
        return code;
    }

}
