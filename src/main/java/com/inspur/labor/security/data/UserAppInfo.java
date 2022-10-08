package com.inspur.labor.security.data;

import java.io.Serializable;

/**
 * app端用户信息
 *
 * @author 耿鹏
 */
public class UserAppInfo implements Serializable {

    /**
     * 用户编码
     */
    private String userId;

    /**
     * 02
     * 登录用户昵称
     */
    private String nickName;

    /**
     * 所属工会id
     */
    private String organId;

    /**
     * 工会名称
     */
    private String organName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 会员id
     */
    private String memberId;

    /**
     * 会员姓名
     */
    private String memberName;

    /**
     * 身份证号码
     */
    private String idcard;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 性别
     */
    private String sex;

    /**
     * 区划代码
     */
    private String districtCode;

    /**
     * 是否是志愿者
     */
    private Integer isVol;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public void setIsVol(Integer isVol) {
        this.isVol = isVol;
    }

    public Integer getIsVol() {
        return isVol;
    }

    @Override
    public String toString() {
        return "UserAppInfo{" +
                "userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", organId='" + organId + '\'' +
                ", organName='" + organName + '\'' +
                ", phone='" + phone + '\'' +
                ", memberId='" + memberId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", idcard='" + idcard + '\'' +
                ", birthday='" + birthday + '\'' +
                ", sex='" + sex + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", isVol=" + isVol +
                '}';
    }
}