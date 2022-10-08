package com.inspur.labor.security.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author 赵彦凯
 * @version 1.0
 * @date 2022/8/24 19:02
 */
public class RequestInterceptor implements HandlerInterceptor {

    private static String[] filterStr = {"and"
            , "exec"
            , "insert"
            , "select"
            , "delete"
            , "update"
            , "count"
            , "or"
            //,"*"
            , "%"
            , ":"
            , "\'"
            , "\""
            , "chr"
            , "mid"
            , "master"
            , "truncate"
            , "char"
            , "declare"
            , "SiteName"
            , "net user"
            , "xp_cmdshell"
            , "/add"
            , "exec master.dbo.xp_cmdshell"
            , "net localgroup administrators"};

    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String order = request.getParameter("order");
        if (StringUtils.isNotBlank(order)) {
            if (Arrays.stream(filterStr).anyMatch(v -> order.contains(v))) {
                throw new Exception("排序参数未授权");
            }
        }
        return true;
    }
}
