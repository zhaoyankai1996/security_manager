package com.inspur.labor.security.util;

import com.inspur.msy.uid.dict.DictHelper;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 赵彦凯
 * @version 1.0
 * @date 2022/8/4 9:25
 */

public class ContestUtil {
    /**
     * 脱敏字符
     */
    private static final String OVERLAY = "****";
    private static final int START = 3;
    private static final int END = 7;
    /**
     * 15位身份证长度
     */
    private static final Integer FIFTEEN_ID_CARD = 15;
    /**
     * 18位身份证长度
     */
    private static final Integer EIGHTEEN_ID_CARD = 18;
    /**
     * 随机手机开头码
     */
    private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
    private static final String TWP = "^[a-zA-Z][0-9]{9}$";
    private static final String AMP = "^[157][0-9]{6}\\(?[0-9A-Z]\\)?$";
    private static final String XGP = "^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$";

    /**
     * 脱敏手机号
     *
     * @param str 手机号
     * @return 脱敏手机号
     */
    public static String maksString(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return StringUtils.overlay(str, OVERLAY, START, END);
    }

    /**
     * 脱敏身份证
     *
     * @param str 身份证
     * @return 脱敏身份证
     */
    public static String maksIdcard(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        StringBuilder idcard = new StringBuilder();
        idcard.append(str.substring(0, 3)).append("**********").append(str.substring(str.length() - 4));
        return idcard.toString();
    }

    /**
     * uuid生成
     *
     * @return uuid
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    /**
     * 获取性别
     *
     * @param idCard 身份证
     * @return 性别 M:男 F:女
     */
    public static String getSex(String idCard) {
        String sex = null;
        if (StringUtils.isNotBlank(idCard)) {
            //15位身份证号
            if (idCard.length() == FIFTEEN_ID_CARD) {
                if (Integer.parseInt(idCard.substring(Constant.IDCARD_SEX_START, Constant.IDCARD_SEX_END)) % Constant.ODD == 0) {
                    sex = "F";
                } else {
                    sex = "M";
                }
            } else if (idCard.length() == EIGHTEEN_ID_CARD) {
                if (Integer.parseInt(idCard.substring(Constant.IDCARD_SEX_START_18).substring(0, 1)) % Constant.ODD == 0) {
                    sex = "F";
                } else {
                    sex = "M";
                }
            }
        }
        return sex;
    }

    /**
     * 获取生日
     *
     * @param idCard 身份证
     * @return 生日
     */
    public static Date getBirthday(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return null;
        }
        String birthday = "";
        String year = "";
        String month = "";
        String day = "";
        if (StringUtils.isNotBlank(idCard)) {
            if (idCard.length() == FIFTEEN_ID_CARD) {
                year = "19" + idCard.substring(6, 8);
                month = idCard.substring(8, 10);
                day = idCard.substring(10, 12);
            } else if (idCard.length() == EIGHTEEN_ID_CARD) {
                year = idCard.substring(6).substring(0, 4);
                month = idCard.substring(10).substring(0, 2);
                day = idCard.substring(12).substring(0, 2);
            }
            birthday = year + "-" + month + "-" + day;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {
            parse = format.parse(birthday);
        } catch (ParseException e) {
            parse = null;
        }
        return parse;
    }

    /**
     * 检查区划等级
     *
     * @param id 区划
     * @return 区划等级
     */
    public static Map<String, String> checkAreaLevel(String id) {
        Map<String, String> map = new HashMap<>(10);
        if (Constant.PROVINCE.equals(id.substring(Constant.PROVINCE_LENGTH))) {
            map.put("prefix", id.substring(0, 2));
            map.put("suffix", "00");
            map.put("level", "1");
            map.put("index", "4");
        } else if (Constant.CITY.equals(id.substring(Constant.CITY_LENGTH))) {
            map.put("prefix", id.substring(0, 4));
            map.put("suffix", "");
            map.put("level", "2");
            map.put("index", "6");
        } else {
            map.put("prefix", id);
            map.put("suffix", "");
            map.put("level", "3");
            map.put("index", "6");
        }
        return map;
    }

    /**
     * 获取区划等级
     *
     * @param svcAreaCode 区划code
     * @return 区划等级
     */
    public static String areaCodeSplit(String svcAreaCode) {
        if (Constant.CITY.equals(svcAreaCode.substring(Constant.CITY_LENGTH))) {
            if (Constant.PROVINCE.equals(svcAreaCode.substring(Constant.PROVINCE_LENGTH))) {
                if (Constant.CHINA.equals(svcAreaCode)) {
                    return null;
                } else {
                    return svcAreaCode.substring(0, 2);
                }
            } else {
                return svcAreaCode.substring(0, 4);
            }
        } else {
            return svcAreaCode;
        }
    }

    /**
     * 获取字典对应中文
     *
     * @param str  字符串
     * @param type 字典类型
     * @return
     */
    public static String changeDict(String str, String type) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        String[] split = str.split(",");
        String splitStr = "";
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            String itemValue = DictHelper.getDictItem(type, s).getItemValue();
            if ("".equals(splitStr)) {
                sb.append(itemValue);
            } else {
                sb.append(",").append(itemValue);
            }
        }
        return sb.toString();
    }

    public static String getRandomId() {
        String id = "";
        // 随机生成省、自治区、直辖市代码 1-2
        String[] provinces = {"11", "12", "13", "14", "15", "21", "22", "23",
                "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
                "44", "45", "46", "50", "51", "52", "53", "54", "61", "62",
                "63", "64", "65", "71", "81", "82"};
        String province = provinces[new Random().nextInt(provinces.length - 1)];
        // 随机生成地级市、盟、自治州代码 3-4
        String[] citys = {"01", "02", "03", "04", "05", "06", "07", "08",
                "09", "10", "21", "22", "23", "24", "25", "26", "27", "28"};
        String city = citys[new Random().nextInt(citys.length - 1)];
        // 随机生成县、县级市、区代码 5-6
        String[] countys = {"01", "02", "03", "04", "05", "06", "07", "08",
                "09", "10", "21", "22", "23", "24", "25", "26", "27", "28",
                "29", "30", "31", "32", "33", "34", "35", "36", "37", "38"};
        String county = countys[new Random().nextInt(countys.length - 1)];
        // 随机生成出生年月 7-14
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE,
                date.get(Calendar.DATE) - new Random().nextInt(365 * 100));
        String birth = dft.format(date.getTime());
        // 随机生成顺序号 15-17
        String no = new Random().nextInt(999) + "";
        // 随机生成校验码 18
        String[] checks = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "X"};
        String check = checks[new Random().nextInt(checks.length - 1)];
        // 拼接身份证号码
        id = province + city + county + birth + no + check;

        return id;
    }

    public static int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    public static String getTel() {
        int index = getNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + third;
    }

    public static boolean isValidCard(String idCard) {
        idCard = idCard.trim();
        int length = idCard.length();
        switch (length) {
            // 18位身份证
            case 18:
                return isValidCard18(idCard);
            // 15位身份证
            case 15:
                return isValidCard15(idCard);
            // 10位身份证，港澳台地区
            case 10: {
                String[] cardval = isValidCard10(idCard);
                return null != cardval && "true".equals(cardval[2]);
            }
            default:
                return false;
        }
    }

    /**
     * 判断18位身份证的合法性
     * <p>
     * 公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成，排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
     * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
     * 第1、2位数字表示：所在省份的代码；第3、4位数字表示：所在城市的代码；第5、6位数字表示：所在区县的代码，第7~14位数字表示：出生年、月、日
     * 第15、16位数字表示：所在地的派出所的代码；第17位数字表示性别：奇数表示男性，偶数表示女性
     * 第18位数字是校检码，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示
     * 第十八位数字(校验码)的计算方法为：
     *
     * @param idCard 待验证的身份证
     * @return 是否有效的18位身份证
     */
    private static boolean isValidCard18(String idCard) {
        if (CHINA_ID_MAX_LENGTH != idCard.length()) {
            return false;
        }

        //校验生日
        if (false == isBirthday(idCard.substring(Constant.IDCARD_6, Constant.IDCARD_14))) {
            return false;
        }

        // 前17位
        String code17 = idCard.substring(0, 17);
        // 第18位
        char code18 = Character.toLowerCase(idCard.charAt(17));
        if (isMatch(NUMBERS, code17)) {
            // 获取校验位
            char val = getCheckCode18(code17);
            return val == code18;
        }
        return false;
    }

    /**
     * 验证15位身份编码是否合法
     *
     * @param idCard 身份编码
     * @return 是否合法
     */
    private static boolean isValidCard15(String idCard) {
        if (CHINA_ID_MIN_LENGTH != idCard.length()) {
            return false;
        }
        if (isMatch(NUMBERS, idCard)) {
            // 省份
            String proCode = idCard.substring(0, 2);
            if (null == cityCodes.get(proCode)) {
                return false;
            }

            //校验生日（两位年份，补充为19XX）
            return false != isBirthday("19" + idCard.substring(6, 12));
        } else {
            return false;
        }
    }

    /**
     * 验证10位身份编码是否合法
     *
     * @param idCard 身份编码
     * @return 身份证信息数组
     * [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false) 若不是身份证件号码则返回null
     */
    private static String[] isValidCard10(String idCard) {
        if (idCard == null || idCard.trim().length() == 0) {
            return null;
        }
        String[] info = new String[3];
        String card = idCard.replaceAll("[()]", "");
        if (card.length() != Constant.IDCARD_8 && card.length() != Constant.IDCARD_9 && idCard.length() != Constant.IDCARD_10) {
            return null;
        }
        // 台湾
        if (idCard.matches(TWP)) {
            info[0] = "台湾";
            String char2 = idCard.substring(1, 2);
            if (Constant.IDCARD_SEX_1.equals(char2)) {
                info[1] = "M";
            } else if (Constant.IDCARD_SEX_2.equals(char2)) {
                info[1] = "F";
            } else {
                info[1] = "N";
                info[2] = "false";
                return info;
            }
            info[2] = isValidTwCard(idCard) ? "true" : "false";
        } else if (idCard.matches(AMP)) {
            // 澳门
            info[0] = "澳门";
            info[1] = "N";
        } else if (idCard.matches(XGP)) {
            // 香港
            info[0] = "香港";
            info[1] = "N";
            info[2] = isValidTwCard(idCard) ? "true" : "false";
        } else {
            return null;
        }
        return info;
    }

    /**
     * 验证台湾身份证号码
     *
     * @param idCard 身份证号码
     * @return 验证码是否符合
     */
    private static boolean isValidTwCard(String idCard) {
        if (idCard == null || idCard.trim().length() == 0) {
            return false;
        }
        String start = idCard.substring(0, 1);
        Integer iStart = twFirstCode.get(start);
        if (null == iStart) {
            return false;
        }
        String mid = idCard.substring(1, 9);
        String end = idCard.substring(9, 10);
        int sum = iStart / 10 + (iStart % 10) * 9;
        final char[] chars = mid.toCharArray();
        int iflag = 8;
        for (char c : chars) {
            sum += Integer.valueOf(String.valueOf(c)) * iflag;
            iflag--;
        }
        return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.valueOf(end);
    }

    /**
     * 验证香港身份证号码
     * 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
     * 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
     *
     * @param idCard 身份证号码
     * @return 验证码是否符合
     */
    private static boolean isValidHkCard(String idCard) {
        String card = idCard.replaceAll("[()]", "");
        int sum;
        if (card.length() == Constant.IDCARD_9) {
            sum = (Character.toUpperCase(card.charAt(0)) - 55) * 9 + (Character.toUpperCase(card.charAt(1)) - 55) * 8;
            card = card.substring(1, 9);
        } else {
            sum = 522 + (Character.toUpperCase(card.charAt(0)) - 55) * 8;
        }
        String start = idCard.substring(0, 1);
        Integer iStart = hkFirstCode.get(start);
        if (null == iStart) {
            return false;
        }
        String mid = card.substring(1, 7);
        String end = card.substring(7, 8);
        char[] chars = mid.toCharArray();
        int iflag = 7;
        for (char c : chars) {
            sum = sum + Integer.valueOf(String.valueOf(c)) * iflag;
            iflag--;
        }
        if (Constant.A.equals(end.toUpperCase())) {
            sum += 10;
        } else {
            sum += Integer.parseInt(end);
        }
        return sum % 11 == 0;
    }

    /**
     * 根据身份编号获取生日，只支持15或18位身份证号码
     *
     * @param idCard 身份编号
     * @return 生日(yyyyMMdd)
     */
    public static String getBirth(String idCard) {
        final int len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = convertIdCard(idCard);
        }
        return idCard.substring(6, 14);
    }

    /**
     * 将15位身份证号码转换为18位
     *
     * @param idCard 15位身份编码
     * @return 18位身份编码
     */
    public static String convertIdCard(String idCard) {
        StringBuilder idCard18;
        if (idCard.length() != CHINA_ID_MIN_LENGTH) {
            return null;
        }
        if (isMatch(NUMBERS, idCard)) {
            // 获取出生年月日
            String birthday = idCard.substring(6, 12);
            Date birthDate = strToDate(birthday, YY_MM_DD);
            // 获取出生年
            int sYear = year(birthDate);
            // 理论上2000年之后不存在15位身份证，可以不要此判断
            if (sYear > Constant.YEAR_2000) {
                sYear -= 100;
            }
            idCard18 = new StringBuilder().append(idCard, 0, 6).append(sYear).append(idCard.substring(8));
            // 获取校验位
            char sVal = getCheckCode18(idCard18.toString());
            idCard18.append(sVal);
        } else {
            return null;
        }
        return idCard18.toString();
    }

    /**
     * 获得18位身份证校验码
     * 计算方式：
     * 将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * 将这17位数字和系数相乘的结果相加
     * 用加出来和除以11，看余数是多少
     * 余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2
     * 通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2
     *
     * @param code17 18位身份证号中的前17位
     * @return 第18位
     */
    private static char getCheckCode18(String code17) {
        int sum = getPowerSum(code17.toCharArray());
        return getCheckCode18(sum);
    }

    /**
     * 将power和值与11取模获得余数进行校验码判断
     *
     * @param iSum 加权和
     * @return 校验位
     */
    private static char getCheckCode18(int iSum) {
        switch (iSum % 11) {
            case 10:
                return '2';
            case 9:
                return '3';
            case 8:
                return '4';
            case 7:
                return '5';
            case 6:
                return '6';
            case 5:
                return '7';
            case 4:
                return '8';
            case 3:
                return '9';
            case 2:
                return 'x';
            case 1:
                return '0';
            case 0:
                return '1';
            default:
                return SPACE;
        }
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param iArr 身份证号码的数组
     * @return 身份证编码
     */
    private static int getPowerSum(char[] iArr) {
        int iSum = 0;
        if (POWER.length == iArr.length) {
            for (int i = 0; i < iArr.length; i++) {
                iSum += Integer.valueOf(String.valueOf(iArr[i])) * POWER[i];
            }
        }
        return iSum;
    }

    /**
     * 根据日期获取年
     *
     * @param date 日期
     * @return 年的部分
     */
    public static int year(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        return ca.get(Calendar.YEAR);
    }

    /**
     * 验证是否为生日<br>
     * 只支持以下几种格式：
     * <ul>
     * <li>yyyyMMdd</li>
     * <li>yyyy-MM-dd</li>
     * <li>yyyy/MM/dd</li>
     * <li>yyyy.MM.dd</li>
     * <li>yyyy年MM月dd日</li>
     * </ul>
     *
     * @param value 值
     * @return 是否为生日
     */
    public static boolean isBirthday(CharSequence value) {
        if (isMatch(BIRTHDAY, value)) {
            Matcher matcher = BIRTHDAY.matcher(value);
            if (matcher.find()) {
                int year = Integer.parseInt(matcher.group(1));
                int month = Integer.parseInt(matcher.group(3));
                int day = Integer.parseInt(matcher.group(5));
                return isBirthday(year, month, day);
            }
        }
        return false;
    }

    /**
     * 验证是否为生日
     *
     * @param year  年，从1900年开始计算
     * @param month 月，从1开始计数
     * @param day   日，从1开始计数
     * @return 是否为生日
     */
    public static boolean isBirthday(int year, int month, int day) {
        // 验证年
        int thisYear = year(new Date());
        if (year < Constant.YEAR_1900 || year > thisYear) {
            return false;
        }

        // 验证月
        if (month < 1 || month > Constant.MOUTH_12) {
            return false;
        }

        // 验证日
        if (day < 1 || day > Constant.DAY_31) {
            return false;
        }
        // 检查几个特殊月的最大天数
        if (day == Constant.DAY_31) {
            if (month == Constant.MOUTH_4 || month == Constant.MOUTH_6 || month == Constant.MOUTH_9 || month == Constant.MOUTH_11) {
                return false;
            }
        }
        if (month == Constant.MOUTH_2) {
            // 在2月，非闰年最大28，闰年最大29
            return day < 29 || (day == 29 && isLeapYear(year));
        }
        return true;
    }

    /**
     * 是否闰年
     *
     * @param year 年
     * @return 是否闰年
     */
    private static boolean isLeapYear(int year) {
        return new GregorianCalendar().isLeapYear(year);
    }

    /**
     * 将字符串转换成指定格式的日期
     *
     * @param str        日期字符串.
     * @param dateFormat 日期格式. 如果为空，默认为:yyyy-MM-dd HH:mm:ss.
     * @return
     */
    private static Date strToDate(final String str, String dateFormat) {
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        try {
            if (dateFormat == null || dateFormat.length() == 0) {
                dateFormat = DATE_FORMAT;
            }
            DateFormat fmt = new SimpleDateFormat(dateFormat);
            return fmt.parse(str.trim());
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 给定内容是否匹配正则
     *
     * @param pattern 模式
     * @param content 内容
     * @return 正则为null或者""则不检查，返回true，内容为null返回false
     */
    private static boolean isMatch(Pattern pattern, CharSequence content) {
        if (content == null || pattern == null) {
            // 提供null的字符串为不匹配
            return false;
        }
        return pattern.matcher(content).matches();
    }

    public static Integer getAge(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return null;
        }
        Integer age = 0;
        Date date = new Date();
        if (StringUtils.isNotBlank(idCard)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if (idCard.length() == FIFTEEN_ID_CARD) {
                String uyear = "19" + idCard.substring(6, 8);
                String uyue = idCard.substring(8, 10);
                String fyear = format.format(date).substring(0, 4);
                String fyue = format.format(date).substring(5, 7);
                if (Integer.parseInt(uyue) <= Integer.parseInt(fyue)) {
                    age = Integer.parseInt(fyear) - Integer.parseInt(uyear) + 1;
                } else {
                    age = Integer.parseInt(fyear) - Integer.parseInt(uyear);
                }
            } else if (idCard.length() == EIGHTEEN_ID_CARD) {
                String year = idCard.substring(6).substring(0, 4);
                String yue = idCard.substring(10).substring(0, 2);
                String fyear = format.format(date).substring(0, 4);
                String fyue = format.format(date).substring(5, 7);
                if (Integer.parseInt(yue) <= Integer.parseInt(fyue)) {
                    age = Integer.parseInt(fyear) - Integer.parseInt(year) + 1;
                } else {
                    age = Integer.parseInt(fyear) - Integer.parseInt(year);
                }
            }
        }
        return age;
    }

    public static String getCurrYear() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 中国公民身份证号码最小长度。
     */
    private static final int CHINA_ID_MIN_LENGTH = 15;
    /**
     * 中国公民身份证号码最大长度。
     */
    private static final int CHINA_ID_MAX_LENGTH = 18;
    /**
     * 每位加权因子
     */
    private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    /**
     * 省市代码表
     */
    private static Map<String, String> cityCodes = new HashMap<>();
    /**
     * 台湾身份首字母对应数字
     */
    private static Map<String, Integer> twFirstCode = new HashMap<>();
    /**
     * 香港身份首字母对应数字
     */
    private static Map<String, Integer> hkFirstCode = new HashMap<>();

    /**
     * 默认日期模板
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String YYYY_MM_DD = "yyyyMMdd";
    private static final String YY_MM_DD = "yyMMdd";
    public static final char SPACE = ' ';
    /**
     * 数字
     */
    public final static Pattern NUMBERS = Pattern.compile("\\d+");
    /**
     * 生日
     */
    public final static Pattern BIRTHDAY = Pattern.compile("^(\\d{2,4})([/\\-.年]?)(\\d{1,2})([/\\-.月]?)(\\d{1,2})日?$");

    static {
        cityCodes.put("11", "北京");
        cityCodes.put("12", "天津");
        cityCodes.put("13", "河北");
        cityCodes.put("14", "山西");
        cityCodes.put("15", "内蒙古");
        cityCodes.put("21", "辽宁");
        cityCodes.put("22", "吉林");
        cityCodes.put("23", "黑龙江");
        cityCodes.put("31", "上海");
        cityCodes.put("32", "江苏");
        cityCodes.put("33", "浙江");
        cityCodes.put("34", "安徽");
        cityCodes.put("35", "福建");
        cityCodes.put("36", "江西");
        cityCodes.put("37", "山东");
        cityCodes.put("41", "河南");
        cityCodes.put("42", "湖北");
        cityCodes.put("43", "湖南");
        cityCodes.put("44", "广东");
        cityCodes.put("45", "广西");
        cityCodes.put("46", "海南");
        cityCodes.put("50", "重庆");
        cityCodes.put("51", "四川");
        cityCodes.put("52", "贵州");
        cityCodes.put("53", "云南");
        cityCodes.put("54", "西藏");
        cityCodes.put("61", "陕西");
        cityCodes.put("62", "甘肃");
        cityCodes.put("63", "青海");
        cityCodes.put("64", "宁夏");
        cityCodes.put("65", "新疆");
        cityCodes.put("71", "台湾");
        cityCodes.put("81", "香港");
        cityCodes.put("82", "澳门");
        cityCodes.put("91", "国外");

        twFirstCode.put("A", 10);
        twFirstCode.put("B", 11);
        twFirstCode.put("C", 12);
        twFirstCode.put("D", 13);
        twFirstCode.put("E", 14);
        twFirstCode.put("F", 15);
        twFirstCode.put("G", 16);
        twFirstCode.put("H", 17);
        twFirstCode.put("J", 18);
        twFirstCode.put("K", 19);
        twFirstCode.put("L", 20);
        twFirstCode.put("M", 21);
        twFirstCode.put("N", 22);
        twFirstCode.put("P", 23);
        twFirstCode.put("Q", 24);
        twFirstCode.put("R", 25);
        twFirstCode.put("S", 26);
        twFirstCode.put("T", 27);
        twFirstCode.put("U", 28);
        twFirstCode.put("V", 29);
        twFirstCode.put("X", 30);
        twFirstCode.put("Y", 31);
        twFirstCode.put("W", 32);
        twFirstCode.put("Z", 33);
        twFirstCode.put("I", 34);
        twFirstCode.put("O", 35);

        //来自http://shenfenzheng.bajiu.cn/?rid=40
        // 持证人拥有香港居留权
        hkFirstCode.put("A", 1);
        // 持证人所报称的出生日期或地点自首次登记以后，曾作出更改
        hkFirstCode.put("B", 2);
        // 持证人登记领证时在香港的居留受到入境事务处处长的限制
        hkFirstCode.put("C", 3);
        // 持证人所报的姓名自首次登记以后，曾作出更改
        hkFirstCode.put("N", 14);
        // 持证人报称在香港、澳门及中国以外其他地区或国家出生
        hkFirstCode.put("O", 15);
        // 持证人拥有香港入境权
        hkFirstCode.put("R", 18);
        // 持证人登记领证时在香港的居留不受入境事务处处长的限制
        hkFirstCode.put("U", 21);
        // 持证人报称在澳门地区出生
        hkFirstCode.put("W", 23);
        // 持证人报称在中国大陆出生
        hkFirstCode.put("X", 24);
        // 持证人报称在香港出生
        hkFirstCode.put("Z", 26);
    }

    public static void main(String[] args) {
        String s = AesUtil.aesEncrypt("130629199001213610");
        System.err.println(s);
    }
}
