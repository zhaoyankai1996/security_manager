package com.inspur.labor.security.util;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.apache.commons.lang3.StringUtils;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 赵彦凯
 * @version 1.0
 * @date 2022/9/7 17:00
 */
public class GlobalJsonDateConvert extends StdDateFormat {
    public static final GlobalJsonDateConvert INSTANCE = new GlobalJsonDateConvert();

    /**
     * 覆盖parse(String)这个方法即可实现
     *
     * @param dateStr
     * @param pos
     * @return
     */
    @Override
    public Date parse(String dateStr, ParsePosition pos) {
        return getDate(dateStr, pos);
    }

    @Override
    public Date parse(String dateStr) {
        ParsePosition pos = new ParsePosition(0);
        return getDate(dateStr, pos);
    }

    private Date getDate(String dateStr, ParsePosition pos) {
        SimpleDateFormat sdf = null;
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        } else if (dateStr.matches("^\\d{4}-\\d{1,2}$")) {
            sdf = new SimpleDateFormat("yyyy-MM");
            return sdf.parse(dateStr, pos);
        } else if (dateStr.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr, pos);
        } else if (dateStr.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.parse(dateStr, pos);
        } else if (dateStr.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(dateStr, pos);
        } else if (dateStr.length() == 23) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            return sdf.parse(dateStr, pos);
        }
        return super.parse(dateStr, pos);
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date, toAppendTo, fieldPosition);
    }

    @Override
    public GlobalJsonDateConvert clone() {
        return new GlobalJsonDateConvert();
    }
}
