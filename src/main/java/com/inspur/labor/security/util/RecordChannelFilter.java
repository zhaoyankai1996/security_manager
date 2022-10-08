package com.inspur.labor.security.util;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author 赵彦凯
 * @version 1.0
 * @date 2022/8/24 19:12
 */
@Component
@WebFilter(urlPatterns = "/**")
public class RecordChannelFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            if (!Constant.EXCLUDE_URL.contains(((HttpServletRequest) servletRequest).getRequestURI())) {
                requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
            }
        }
        if (requestWrapper == null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }
}
