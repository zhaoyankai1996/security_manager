package com.inspur.labor.security.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWTUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 赵彦凯
 * @version 1.0
 * @date 2022/8/9 11:23
 */
public class JwtUtil {
    public static void main(String[] args) {
//        String token = initToken("志愿者四号","O1501020000000","新城区总工会","15863636364","150102");
//        String token = initToken();
//        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIzNjcwMzIwYjgxMDc0MDdhOWU0NTU5NjVjZjhiY2M2YyIsInVzZXJOYW1lIjoi5b-X5oS_6ICF5LiA5Y-3Iiwib3JnSWQiOiJPMTUwMTAyMDAwMDAwMCIsIm9yZ05hbWUiOiLmlrDln47ljLrmgLvlt6XkvJoiLCJjb3JwSWQiOiJPMTUwMTAyMDAwMDAwMCIsImNvcnBOYW1lIjoi5paw5Z-O5Yy65oC75bel5LyaIiwicGhvbmUiOiIxNTg2MzYzNjM2MyIsImV4dCI6IntcImNoYXJ0TmFtZVwiOlwi5oi35aSW5Yqz5Yqo6ICF5pyN5Yqh56uZ55uR566hXCIsXCJtZW1iZXJJZFwiOlwiODRmNTcwODdiZTI4NDQzMjlmMjMzYjdmYTFmYzc3MTdcIixcIm1lbWJlck5hbWVcIjpcIuW_l-aEv-iAheS4gOWPt1wiLFwic2V4XCI6XCJNXCIsXCJiaXJ0aGRheVwiOlwiMjAyMi0wMS0wMVwiLFwiZW5jcnlwdElkTm9cIjpcIjIwMjItMDEtMDFcIixcImRpc3RyaWN0Q29kZVwiOlwiMjAyMi0wMS0wMVwifSIsImxvZ2luTmFtZSI6IuW_l-aEv-iAheS4gOWPtyIsImNoYXJ0SWQiOiIzNjcwMzIwYjgxMDc0MDdhOWU0NTU5NjVjZjhiY2M2YyIsInJvbGVzIjpbXX0.BI8YDPpp0zq2Y6rHJR0J0nM_2VvySnaQMOmcqGp-MOU";
//        JWT jwt = JWTUtil.parseToken(token);
//        Map map = JsonUtils.toJavaObject(jwt.getPayload().getClaimsJson(), Map.class);
//        Object o = map.get("ext");
//        Map memberMap = JsonUtils.toJavaObject(o, Map.class);
//        System.err.println(memberMap.toString());
//        boolean verifyKey = jwt.setKey(key.getBytes()).verify();
//        System.out.println(verifyKey);
//        boolean verifyTime = jwt.validate(0);
//        System.out.println(verifyTime);
    }

    public static Map<String, String> initToken(String name, String organId, String organName, String phone, String dict, String idcard) {
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.MINUTE, 10);
        Map<String, Object> payload = new HashMap<String, Object>(20);
        String memberId = UUID.randomUUID().toString().replaceAll("-", "");
        String userName = UUID.randomUUID().toString().replaceAll("-", "");
        payload.put("userId", userName);
        payload.put("userName", name);
        payload.put("orgId", organId);
        payload.put("orgName", organName);
        payload.put("corpId", "666889");
        payload.put("corpName", "666889");
        payload.put("phone", phone);
        Map<String, Object> ext = new HashMap<String, Object>(20);
        ext.put("chartName", "户外劳动者服务站监管");
        ext.put("memberId", memberId);
        ext.put("memberName", name);
        ext.put("sex", "M");
        ext.put("birthday", "2022-01-01");
        ext.put("encryptIdNo", idcard);
        ext.put("districtCode", dict);
        payload.put("ext", JsonUtils.toJsonString(ext));
        payload.put("loginName", "zyzyh");
        payload.put("chartId", "666889");
        payload.put("roles", new ArrayList<>());
        String key = "123";
        String token = JWTUtil.createToken(payload, key.getBytes());
        System.out.println(token);
        Map<String, String> map = new HashMap<>(20);
        map.put("memberId", memberId);
        map.put("userId", userName);
        map.put("token", token);
        return map;
    }


}
