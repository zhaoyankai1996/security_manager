package com.inspur.labor.security.data;

import java.io.Serializable;

/**
 * @author Administrator
 * 系统管理员信息
 */
public class UserAdminInfo implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    /**
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
     * 区划代码
     */
    private String districtCode;

    /**
     * 当前登录用户区划
     */
    private String svcAreaCode;

    /**
     * 当前登录用户区划名称
     */
    private String svcAreaName;

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

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getSvcAreaCode() {
        return svcAreaCode;
    }

    public void setSvcAreaCode(String svcAreaCode) {
        this.svcAreaCode = svcAreaCode;
    }

    public String getSvcAreaName() {
        return svcAreaName;
    }

    public void setSvcAreaName(String svcAreaName) {
        this.svcAreaName = svcAreaName;
    }

    @Override
    public String toString() {
        return "UserAdminInfo{" +
                "userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", organId='" + organId + '\'' +
                ", organName='" + organName + '\'' +
                ", phone='" + phone + '\'' +
                ", districtCode='" + districtCode + '\'' +
                '}';
    }
}