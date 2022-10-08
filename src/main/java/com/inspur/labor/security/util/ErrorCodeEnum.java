package com.inspur.labor.security.util;

/**
 * 错误编码
 *
 * @author sangchg
 * @since 20170927
 */
public enum ErrorCodeEnum {

    /**
     * P***** 开头系统平台级错误
     */
    P_SUCCESS("P00000", "操作成功"),
    P_SYS_ERROR("P10001", "系统异常"),
    P_SERVICE_PAUSE("P10002", "服务超时/暂停"),
    P_DB_TIMEOUT("P10003", "数据库链接超时"),
    P_DB_OPERATOR_EXCEPTION("P10004", "数据库操作异常"),
    P_IP_RESTRICT("P10005", "IP受限不能请求该资源"),
    P_INVALID("P10006", "非法请求"),
    P_RES_NOT_FOUND("P10007", "请求资源不存在"),
    P_RES_UNAUTHORIZED("P10008", "请求资源未授权"),
    P_RPC_TIMEOUT("P10009", "RPC调用超时"),
    P_RPC_EXCEPTION("P10000", "RPC调用异常"),
    P_RPC_SRV_NOT_FOUND("P10011", "未找到RPC服务"),
    P_REQUEST_FREQUENTLY("P10012", "请求过于频繁"),
    P_GET_ACCESS_TOKEN_FAILURE("P10013", "获取访问票据失败"),
    P_TASK_TYPE_NOT_EXIST("P10014", "任务类型不存在"),

    /**
     * S***** 定义服务级错误编码
     */
    S_NAME_OR_PWD_ERROR("S20001", "用户名或密码错误"),
    S_ACCOUNT_LOCK("S20002", "账号已锁定"),
    S_CODE_ERROR("S20003", "验证码不正确"),
    S_REQ_PARAM_COUNT_ERROR("S20004", "请求参数数量不正确"),
    S_REQ_PARAM_ERROR("S21004", "请求参数不正确"),
    S_REQ_PARAM_NULL("S20005", "必填参数为空"),
    S_REQ_PARAM_FOMAT_ERROR("S20006", "参数的格式不合法"),
    S_REQ_PARAM_INVALID_NUMBER("S20007", "非法数字"),
    S_TYPE_NOT_SUPPORT("S20008", "不支持的类型"),
    S_FILE_TOO_LARGE("S20009", "上传文件大小太大"),
    S_FILE_IS_ZERO("S20019", "上传文件大小为零"),
    S_FILE_EXIST("S20029", "文件已经存在"),
    S_WORDS_TOO_LONG("S20010", "输入的文字太长"),
    S_RECORD_NOT_EXIST("S20011", "更新记录不存在"),
    S_UNAUTHOR_BIZ_CODE("S20012", "业务编码没有授权"),
    S_ES_NOT_INDEX("S20010", "ES索引不存在"),
    S_ES_HAS_INDEX("S20011", "ES索引已存在"),
    S_ES_NOT_TYPE("S20012", "ES类型不存在"),
    S_ES_HAS_TYPE("S20013", "ES类型已存在"),
    S_DB_NOT_SUPPORT_TYPE("S20014", "不支持的数据库类型"),
    S_DB_SCHEMA_NONE("S20015", "数据库Schema为空"),
    S_DB_CONFIG_NONE("S20016", "数据库配置信息为空"),
    S_DB_ADD_ERROR("S20021", "新增数据失败"),
    S_DB_UPDATE_ERROR("S20022", "修改数据失败"),
    S_DB_DELETE_ERROR("S20023", "删除数据失败"),
    S_LOGIN_FAILURE_TOO_MUCH("S20017", "登录失败次数过多，请稍后重试"),
    S_USER_ERROR("S20018", "用户信息为空"),
    S_CON_FILE_EMPTY("S20019", "附件为空"),
    S_CON_WORKTYPE_EMPTY("S20020", "请选择工种"),
    S_CON_FILE_DEL_ERROR("S20021", "比赛文件删除失败"),
    S_CON_FILE_RECEIVE_ERROR("S20022", "比赛文件接收错误"),
    S_CON_TECH_FILE_EMPTY("S20023", "技术文件不存在"),
    S_CON_TECH_REPORT_ERROR("S20024", "技术文件上报失败"),
    S_CON_TECH_REPORT_REPEAT("S20024", "请不要重复上报"),
    S_CON_TECH_RECEIVE_REPEAT("S20025", "重复接收"),
    S_CON_YEAR_ERROR("S20039", "请传入正确年度"),
    S_VOL_ORGAN_EMPTY("S20026", "部门为空"),
    S_VOL_ORGAN_ERROR("S20027", "管理员对应区划获取失败"),
    S_CON_TEAM_PERSON_EMPTY("S20028", "参赛代表队伍人员为空"),
    S_VOL_ACT("S20029", "志愿已激活"),
    S_VOL_ADD_ERROR("S20030", "志愿者新增失败"),
    S_VOL_HIS_ADD_ERROR("S20031", "志愿者历史新增失败"),
    S_VOL_AUDIT_EMPTY("S20032", "未找到该志愿者申请信息"),
    S_CON_ID_ERROR("S20033", "未传输业务主键"),
    S_CON_EXCEL_IMP_ERROR("S20034", "excel导出失败"),
    S_CON_FILE_ERROR("S20035", "未传输附件"),
    S_VOL_AUDIT_STATUS_ERROR("S200333", "志愿者审核状态异常"),
    S_VOL_EXCEL_IMP_ERROR("S200334", "excel导出失败"),
    S_VOL_ADD_TRACK_ERROR("S200335", "增加轨迹表失败"),
    S_VOL_AUDIT_ERROR("S200334", "志愿者审核失败"),
    S_VOL_NOT_VOL("S200335", "该用户不是志愿者"),
    S_VOL_HAS_ACT("S200336", "志愿者已绑定"),
    S_VOL_HAS_AUDIT("S20337", "存在志愿者审核信息"),
    S_VOL_ACT_VOL_ERROR("S20338", "志愿者激活失败"),
    S_RESOURCE_ILLEGAL("S10017", "访问资源不合法"),
    S_CREDIT_CODE_EXIST("S10018", "此社会信用代码已经注册"),
    S_EXIST("S10019", "系统中已存在"),
    S_CHECK_SIGN_FAILURE("S10020", "校验签名失败"),
    C_SUCCESS("C00000", "检查通过");


    /**
     * 默认描述
     */
    private String msg;

    /**
     * 编码
     */
    private String code;

    ErrorCodeEnum(String code, String msg) {
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
    public String getCode() {
        return code;
    }
}
