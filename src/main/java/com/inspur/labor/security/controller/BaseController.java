package com.inspur.labor.security.controller;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.inspur.labor.security.data.UserAdminInfo;
import com.inspur.labor.security.data.UserAppInfo;
import com.inspur.labor.security.exception.CustomException;
import com.inspur.labor.security.util.AesUtil;
import com.inspur.labor.security.util.ErrorCodeEnum;
import com.inspur.labor.security.util.JsonUtils;
import com.inspur.msy.component.greenchannel.web.data.IPubUser;
import com.inspur.msy.component.greenchannel.web.utils.MsySecurityContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName BaseController
 * @Description 基础类
 * @Date 2022/8/1 16:45
 * @Author gengpeng
 */
@Component
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 获取管理端用户信息
     *
     * @return UserAdminInfo用户管理员信息
     */
    public UserAdminInfo getUser() {
        IPubUser pubUser = MsySecurityContextHolder.getUser();
        logger.info("pubUser：{}", JSONObject.toJSONString(pubUser));
        UserAdminInfo userInfo = new UserAdminInfo();
        userInfo.setUserId(pubUser.getUserId());
        userInfo.setNickName(pubUser.getUserName());
        userInfo.setOrganId(pubUser.getOrgId());
        userInfo.setOrganName(pubUser.getOrgName());
        userInfo.setPhone(pubUser.getPhone());
        if (StringUtils.isNotBlank(pubUser.getExt())) {
            Map memberMap = JsonUtils.toJavaObject(pubUser.getExt(), Map.class);
            if (null != memberMap) {
                userInfo.setDistrictCode(Convert.toStr(memberMap.get("districtCode")));
                String organId = (String) memberMap.get("organId");
                if (StringUtils.isNotBlank(organId)) {
                    userInfo.setOrganId(organId);
                }
                String organName = (String) memberMap.get("organName");
                if (StringUtils.isNotBlank(organName)) {
                    userInfo.setOrganName(organName);
                }
            }
        }
        logger.info("管理端用户：{}", JSONObject.toJSONString(userInfo));
        if (userInfo == null || StringUtils.isBlank(userInfo.getUserId())) {
            throw new CustomException(ErrorCodeEnum.S_USER_ERROR);
        }
        if (StringUtils.isBlank(userInfo.getOrganId())) {
            throw new CustomException(ErrorCodeEnum.S_VOL_ORGAN_EMPTY);
        }
        return userInfo;
    }

    /**
     * 获取app端/PC端/微信端用户信息
     *
     * @return UserAppInfo用户信息
     */
    public UserAppInfo getAppUser() {
        IPubUser pubUser = MsySecurityContextHolder.getUser();
        logger.info("pubUser：{}", JSONObject.toJSONString(pubUser));
        UserAppInfo userInfo = new UserAppInfo();
        userInfo.setUserId(pubUser.getUserId());
        userInfo.setNickName(pubUser.getUserName());
        userInfo.setOrganId(pubUser.getOrgId());
        userInfo.setOrganName(pubUser.getOrgName());
        userInfo.setPhone(pubUser.getPhone());
        if (StringUtils.isNotBlank(pubUser.getExt())) {
            Map memberMap = JsonUtils.toJavaObject(pubUser.getExt(), Map.class);
            if (null != memberMap) {
                userInfo.setMemberId(Convert.toStr(memberMap.get("memberId")));
                String encryptIdNo = Convert.toStr(memberMap.get("encryptIdNo"));
                if (!StringUtils.isBlank(encryptIdNo)) {
                    String idcard = AesUtil.aesDecrypt(encryptIdNo);
                    userInfo.setIdcard(idcard);
                }
                userInfo.setMemberName(Convert.toStr(memberMap.get("memberName")));
                userInfo.setBirthday(Convert.toStr(memberMap.get("birthday")));
                userInfo.setSex(Convert.toStr(memberMap.get("sex")));
                userInfo.setDistrictCode(Convert.toStr(memberMap.get("districtCode")));
                String organId = (String) memberMap.get("organId");
                if (StringUtils.isNotBlank(organId)) {
                    userInfo.setOrganId(organId);
                }
                String organName = (String) memberMap.get("organName");
                if (StringUtils.isNotBlank(organName)) {
                    userInfo.setOrganName(organName);
                }
            }
        }
        if (userInfo == null || StringUtils.isBlank(userInfo.getUserId())) {
            throw new CustomException(ErrorCodeEnum.S_USER_ERROR);
        }
        logger.info("移动端用户：{}", JSONObject.toJSONString(userInfo));
        return userInfo;
    }


    public Logger log() {
        return LoggerFactory.getLogger(this.getClass().getName());
    }

}
