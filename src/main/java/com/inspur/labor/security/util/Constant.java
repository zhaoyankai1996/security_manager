package com.inspur.labor.security.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 赵彦凯
 * @version 1.0
 * @date 2022/8/26 17:19
 */
public class Constant {
    /**
     * redis用户头像前缀
     */
    public static final String AVATAR_PREFIX = "uac:user:avatar:";
    /**
     * 用户名字
     */
    public static final String NAME = "name";
    /**
     * 最上级区划(内蒙古)
     */
    public static final String ROOT_AREA = "150000";
    /**
     * oss类型
     */
    public static final String OSS_FLAG = "_oss_flag_";
    /**
     * 最上级工会id
     */
    public static final String ROOT_ID = "rootId";
    /**
     * 活动二维码文字限定长度
     */
    public static final int QRCODE_CONTENT_LENGTH = 15;
    /**
     * 过检查URL
     */
    public static final List<String> EXCLUDE_URL = new ArrayList<>();
    /**
     * 跨域类型请求方法
     */
    public static final String OPTIONS = "OPTIONS";
    /**
     * 字节长度
     */
    public static final int BUF_LENGTH = 100;
    /**
     * true类型
     */
    public static final String TRUE = "true";
    /**
     * 内蒙古工会id
     */
    public static final String NMG_ORGAN = "O1500000000000";
    /**
     * 逗号分隔符
     */
    public static final String SPLIT_STR = ",";
    /**
     * 中国区划
     */
    public static final String CHINA = "0000";
    /**
     * 省级标识，省级区划后四位为0
     */
    public static final String PROVINCE = "0000";
    /**
     * 省级区划有效位数，即前两位一定不全是0
     */
    public static final int PROVINCE_LENGTH = 2;
    /**
     * 市级标识，市级区划后两位为0
     */
    public static final String CITY = "00";
    /**
     * 市级区划有效位数，即前四位一定不全是0
     */
    public static final int CITY_LENGTH = 4;
    /**
     * 脱敏字符串
     */
    public static final String MASK_STR = "*";
    /**
     * base64
     */
    public static final String IS_BASE_64 = "isBase64";
    /**
     * 排序参数名称
     */
    public static final String ORDER = "order";
    /**
     * 排序类型名称
     */
    public static final String ORDER_TYPE = "orderType";
    /**
     * 志愿者激活状态码
     */
    public static final int VOL_ACT = 1;
    /**
     * 志愿者未激活状态码
     */
    public static final int NO_ACT = 2;
    /**
     * 志愿者限制服务
     */
    public static final int LIMIT_SVC = 3;
    /**
     * 志愿者审核待审核状态码
     */
    public static final int WAIT_CHECK = 1;
    /**
     * 志愿者审核通过状态码
     */
    public static final int CHECK_PASS = 2;
    /**
     * 志愿者审核不通过状态码
     */
    public static final int CHECK_NOT_PASS = 3;
    /**
     * 是志愿者
     */
    public static final int IS_VOL = 1;
    /**
     * 不是志愿者
     */
    public static final int IS_NOT_VOL = 0;
    /**
     * 截取区划判断是否是市级的位置
     */
    public static final int CITY_POS = 4;
    /**
     * 已发布状态
     */
    public static final int HAS_PUBLISH = 2;
    /**
     * 15位身份证性别截取开始位置
     */
    public static final int IDCARD_SEX_START = 15;
    /**
     * 15位身份证性别截取结束位置
     */
    public static final int IDCARD_SEX_END = 16;
    /**
     * 18位身份证性别截取位置
     */
    public static final int IDCARD_SEX_START_18 = 16;
    /**
     * 取余判断
     */
    public static final int ODD = 2;
    /**
     * 审核状态字典code
     */
    public static final String CHECK_ENUM_CODE = "736b5d84f2b0411a8b5d91b123cc471b";
    /**
     * 失败状态码
     */
    public static final String P10001 = "P10001";
    /**
     * 返回值参数
     */
    public static final String CODE = "code";
    /**
     * 类型:身份证
     */
    public static final Integer IDCARD = 1;
    /**
     * 活动进行中
     */
    public static final Integer ACT_START = 1;
    /**
     * 活动未开始
     */
    public static final Integer ACT_NOT_START = 2;
    /**
     * 活动已结束
     */
    public static final Integer ACT_FINISH = 3;

    public static final Integer IDCARD_6 = 6;

    public static final Integer IDCARD_8 = 8;

    public static final Integer IDCARD_9 = 9;

    public static final Integer IDCARD_10 = 10;

    public static final Integer IDCARD_14 = 14;

    public static final String IDCARD_SEX_1 = "1";

    public static final String IDCARD_SEX_2 = "2";

    public static final String IDCARD_STR = "idcard";

    public static final String A = "A";

    public static final Integer YEAR_1900 = 1900;

    public static final Integer YEAR_2000 = 2000;

    public static final Integer MOUTH_2 = 2;

    public static final Integer MOUTH_4 = 4;

    public static final Integer MOUTH_6 = 6;

    public static final Integer MOUTH_9 = 9;

    public static final Integer MOUTH_11 = 11;

    public static final Integer MOUTH_12 = 12;

    public static final Integer DAY_31 = 31;

    public static final int RANK_NUM = 20;

    public static final int FORE = 4;

    public static final int THREE = 3;

    public static final int YES = 1;

    public static final int NO = 0;

    public static final int AUDIT = 0;

    public static final int PASS = 1;

    public static final int NOT_PASS = 2;

    public static final int QZ_NOT_PASS = 4;

    public static final int REPORTSTATUSLOGO = 2;

    public static final String CONTEST_TYPE = "contest_type";

    public static final String CONTEST_CATEGORY = "contest_category";

    public static final String IDENTITY = "participating_status";

    public static final String NATION = "nation";

    public static final String STRU_LEAVE_2 = "2";
    public static final String STRU_LEAVE_4 = "4";
    public static final String STRU_LEAVE_6 = "6";

    public static final String XLSX = "xlsx";
    public static final Integer BUFFLENGTH = 8192;

    public static final Integer NUM_0 =0;
    public static final Integer NUM_1 =1;
    public static final Integer NUM_2 =2;
    public static final Integer NUM_3 =3;
    public static final Integer NUM_4 =4;
    public static final Integer NUM_5 =5;
    public static final Integer NUM_6 =6;
    public static final Integer NUM_7 =7;
    public static final Integer NUM_8 =8;
    public static final Integer NUM_9 =9;
    public static final Integer NUM_10 =10;
    public static final Integer NUM_20 =20;
    public static final Integer NUM_100 =100;

    static {
        EXCLUDE_URL.add("/contest/ueditor/exec");
    }

}
