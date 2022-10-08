package com.inspur.labor.security.util;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * hessian 序列化实现工具类
 *
 * @author duanqp
 * @date 2022/06/12
 */
public final class HessianUtils {
    private static final Logger logger = LoggerFactory.getLogger(HessianUtils.class);

    /**
     * 构造方法，不允许new
     */
    private HessianUtils() {

    }

    /**
     * 序列化
     *
     * @param obj 序列化对象
     * @return 字节
     */
    public static byte[] serialize(final Object obj) {
        HessianOutput hessianOutput = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            // Hessian的序列化输出
            hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (final IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("hessian 序列化出错了");
            }
        } finally {
            try {
                if (hessianOutput != null) {
                    hessianOutput.close();
                }
            } catch (final IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("hessian 序列化出错了");
                }
            }
        }
        return null;
    }

    /**
     * 反序列化
     *
     * @param <T> 返回值类型
     * @param b   字节
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T unserizal(final byte[] b) {
        HessianInput hessianInput = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b)) {
            // Hessian的反序列化读取对象
            hessianInput = new HessianInput(byteArrayInputStream);
            return (T) hessianInput.readObject();
        } catch (final IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("hessian 反序列化出错了");
            }
        } finally {

            try {
                if (hessianInput != null) {
                    hessianInput.close();
                }
            } catch (final Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error("hessian 反序列化出错了");
                }
            }
        }
        return null;
    }
}
