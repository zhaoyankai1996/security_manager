package com.inspur.labor.security.util;


import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wbe
 * @desc 身份证工具类
 * @date 2022/7/7 16:13
 */
public class IdCardNumberUtils {
    /**
     * 检验码：余数作为索引，值代表所替换的值
     */
    private static final String[] CHECK_INDEX = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
    /**
     * 居民身份证 正则表达式
     */
    private static final String SECOND_ID_CARD_REGULAR_EXP = "(^\\d{18}$)|(^\\d{17}(\\d|X|x)$|(^\\d{15}$))";
    /**
     * 居民身份证 年份所在位数
     */
    private static final int ID_CARD_YEAR_INDEX = 6;

    /**
     * 第一代居民身份证长度
     */
    private static final int FIRST_ID_CARD_LENGTH = 15;
    /**
     * 第一代居民身份证 年份值
     */
    private static final String FIRST_ID_CARD_YEAR = "19";

    /**
     * 第二代居民身份证长度
     */
    private static final int SECOND_ID_CARD_LENGTH = 18;
    /**
     * 第二代居民身份证 校验码的模
     */
    private static final int SECOND_ID_CARD_CHECK_MOD = 11;

    /**
     * 性别表示的值
     */
    private static final int MALE_SEX_INT = 1;
    private static final String MALE_SEX_STRING = "男";
    private static final int FEMALE_SEX_INT = 2;
    private static final String FEMALE_SEX_STRING = "女";
    /**
     * 性别 map 中的 key 值
     */
    public static final String SEX_BY_INT_MAP_KEY = "sex_by_int";
    public static final String SEX_BY_STRING_MAP_KEY = "sex_by_string";


    /**
     * @param idCard 身份证
     * @return java.lang.String
     * @desc 通过身份证获取年龄
     * @auth wbe
     * @date 2022/9/21 17:09
     */
    public static int getAgeFromIdCard(String idCard) {
        if (!isIdCard(idCard)) {
            return -1;
        }
        // 获取身份证的出生年月日串
        String birth = getIdCardBirthDayStr(idCard);
        // 获取年月日
        int year = Integer.parseInt(birth.substring(0, 4));
        int month = Integer.parseInt(birth.substring(4, 6));
        int day = Integer.parseInt(birth.substring(6, 8));
        // 计算年龄
        Calendar cal = Calendar.getInstance();
        int age = cal.get(Calendar.YEAR) - year;
        // 周岁计算
        boolean monthLtFlag = cal.get(Calendar.MONTH) + 1 < month;
        boolean monthDayFlag = cal.get(Calendar.MONTH) + 1 == month && cal.get(Calendar.DATE) < day;
        if (monthLtFlag || monthDayFlag) {
            age--;
        }
        return age;
    }

    /**
     * @param idCard 身份证
     * @return java.lang.String
     * @desc 通过身份证获取出生日期
     * @auth wbe
     * @date 2022/9/21 16:39
     */
    public static String getBirthDayFromIdCard(String idCard) {
        if (!isIdCard(idCard)) {
            return "idCard error!";
        }
        // 获取身份证的出生年月日串
        String birth = getIdCardBirthDayStr(idCard);
        // 获取年月日
        int year = Integer.parseInt(birth.substring(0, 4));
        int month = Integer.parseInt(birth.substring(4, 6));
        int day = Integer.parseInt(birth.substring(6, 8));
        return year + "-" + month + "-" + day;
    }

    /**
     * @param idCard 身份证
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @desc 通过身份证获取性别
     * @auth wbe
     * @date 2022/9/21 16:10
     */
    public static Map<String, Object> getSexFromIdCard(String idCard) {
        if (!isIdCard(idCard)) {
            return Collections.emptyMap();
        }
        Map<String, Object> sexMap = new HashMap<>(5);
        // 默认值
        int sexInt = -1;
        String sexStr = "未知";
        // 15 位身份证
        if (idCard.length() == FIRST_ID_CARD_LENGTH) {
            String sex = idCard.substring(14, 15);
            // 偶数表示女性，奇数表示男性
            if (Integer.parseInt(sex) % Constant.ODD == 0) {
                sexInt = FEMALE_SEX_INT;
                sexStr = FEMALE_SEX_STRING;
            } else {
                sexInt = MALE_SEX_INT;
                sexStr = MALE_SEX_STRING;
            }
        }
        // 18 位身份证
        if (idCard.length() == SECOND_ID_CARD_LENGTH) {
            String sex = idCard.substring(16, 17);
            // 偶数表示女性，奇数表示男性
            if (Integer.parseInt(sex) % Constant.ODD == 0) {
                sexInt = FEMALE_SEX_INT;
                sexStr = FEMALE_SEX_STRING;
            } else {
                sexInt = MALE_SEX_INT;
                sexStr = MALE_SEX_STRING;
            }
        }
        // 结果
        sexMap.put(SEX_BY_INT_MAP_KEY, sexInt);
        sexMap.put(SEX_BY_STRING_MAP_KEY, sexStr);

        return sexMap;
    }

    /**
     * @param idCard 身份证
     * @return boolean
     * @desc 检测是否为身份证
     * @auth wbe
     * @date 2022/9/21 15:32
     */
    public static boolean isIdCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return false;
        }
        StringBuilder idCardBuilder = new StringBuilder(idCard);
        // 正则表达式
        boolean matches = idCard.matches(SECOND_ID_CARD_REGULAR_EXP);
        // 第二代身份证的验证
        if (matches && idCardBuilder.length() == SECOND_ID_CARD_LENGTH) {
            int index = getIdCardCheckIndex(idCardBuilder);
            // 获取传入身份证的检验码
            String check = String.valueOf(idCardBuilder.charAt(idCardBuilder.length() - 1));
            // 检验码校验
            return StringUtils.equalsIgnoreCase(CHECK_INDEX[index], check);
        }
        return matches;
    }

    /**
     * @param idCard 身份证号码
     * @return java.lang.String
     * @desc 将 15位身份证号码转为 18位身份证号码
     * @auth wbe
     * @date 2022/9/21 15:13
     */
    public static String idCardNumber15To18(String idCard) {
        if (!isIdCard(idCard) || idCard.length() != FIRST_ID_CARD_LENGTH) {
            return "idCard error!";
        }
        StringBuilder idCardBuilder = new StringBuilder(idCard);
        // 在 第6位后插入年份
        idCardBuilder.insert(ID_CARD_YEAR_INDEX, FIRST_ID_CARD_YEAR);
        int index = getIdCardCheckIndex(idCardBuilder);
        // 根据余数在校验码数组里取值
        idCardBuilder.append(CHECK_INDEX[index]);
        return idCardBuilder.toString();
    }

    /**
     * @param idCard 身份证号码
     * @return java.lang.String
     * @desc 将 18位身份证号码转为 15位身份证号码
     * @auth wbe
     * @date 2022/9/21 16:40
     */
    public static String idCardNumber18To15(String idCard) {
        if (!isIdCard(idCard) || idCard.length() != SECOND_ID_CARD_LENGTH) {
            return "idCard error!";
        }
        // 去掉18位身份证的年份前两位 去掉校验码
        return idCard.substring(0, 5) + idCard.substring(8, 17);
    }

    /**
     * @param idCardBuilder 身份证
     * @return int 检验码下标
     * @desc 计算出校验码所在校验码数组的下标值
     * @auth wbe
     * @date 2022/9/21 9:51
     */
    private static int getIdCardCheckIndex(StringBuilder idCardBuilder) {
        // 判断传入的是17位还是18位身份证号
        int length = idCardBuilder.length() == SECOND_ID_CARD_LENGTH ? idCardBuilder.length() - 1 : idCardBuilder.length();
        // 计算出校验码
        int sum = 0;
        // length=17, i=0、2、3...16
        for (int i = 0; i < length; i++) {
            // 前17位数字
            int numVal = Integer.parseInt(String.valueOf(idCardBuilder.charAt(i)));
            int numMultiple = (int) (Math.pow(2, length - i) % SECOND_ID_CARD_CHECK_MOD);
            sum += (numVal * numMultiple);
        }
        // 总和取模11
        return sum % SECOND_ID_CARD_CHECK_MOD;
    }

    /**
     * @param idCard 身份证
     * @return java.lang.String
     * @desc 通过身份证获取出生日期的 str 串
     * @auth wbe
     * @date 2022/9/21 16:37
     */
    private static String getIdCardBirthDayStr(String idCard) {
        // 获取身份证的出生年月日串
        if (idCard.length() == FIRST_ID_CARD_LENGTH) {
            return FIRST_ID_CARD_YEAR + idCard.substring(ID_CARD_YEAR_INDEX, 12);
        }
        if (idCard.length() == SECOND_ID_CARD_LENGTH) {
            return idCard.substring(ID_CARD_YEAR_INDEX, 14);
        }
        return StringUtils.EMPTY;
    }
}
