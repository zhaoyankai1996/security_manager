package com.inspur.labor.security.config.ueditor.define;

/**
 * 处理状态接口
 *
 * @author hancong03@baidu.com
 */
public interface State {

    /**
     * 是否成功
     *
     * @return
     */
    public boolean isSuccess();

    /**
     * 添加数据
     *
     * @param name
     * @param val
     */
    public void putInfo(String name, String val);

    /**
     * 添加数据
     *
     * @param name
     * @param val
     */
    public void putInfo(String name, long val);

    /**
     * 转json字符串
     *
     * @return
     */
    public String toJsonString();

}
