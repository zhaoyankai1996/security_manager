package com.inspur.labor.security.util;

import com.inspur.labor.security.exception.CustomException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 赵彦凯
 * @version 1.0
 * @date 2022/9/2 10:34
 */
@Component
public class GlobalFormDateConvert implements Converter<String, Date> {
    /**
     * 静态初始化定义日期字符串参数列表（需要转换的）
     */
    private static final List<String> PARAMLIST = new ArrayList<>();

    /**
     * 静态初始化可能初夏你的日期格式
     */
    private static final String PARAM1 = "yyyy-MM";
    private static final String PARAM2 = "yyyy-MM-dd";
    private static final String PARAM3 = "yyyy-MM-dd HH:mm";
    private static final String PARAM4 = "yyyy-MM-dd HH:mm:ss";

    //静态代码块，将日期参数加入到列表中
    static {
        PARAMLIST.add(PARAM1);
        PARAMLIST.add(PARAM2);
        PARAMLIST.add(PARAM3);
        PARAMLIST.add(PARAM4);
    }

    /**
     * 自定义函数，将字符串转Date  参1：传入的日期字符串  参2：格式参数
     *
     * @param source
     * @param format
     * @return
     */
    public Date parseDate(String source, String format) {
        Date date = null;
        try {
            //日期格式转换器
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    @Override
    public Date convert(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        source = source.trim();
        DateFormat dateFormat = new SimpleDateFormat(PARAM1);
        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return parseDate(source, PARAMLIST.get(0));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(source, PARAMLIST.get(1));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, PARAMLIST.get(2));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, PARAMLIST.get(3));
        } else {
            throw new CustomException(ErrorCodeEnum.S_REQ_PARAM_ERROR, "日期参数错误");
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
