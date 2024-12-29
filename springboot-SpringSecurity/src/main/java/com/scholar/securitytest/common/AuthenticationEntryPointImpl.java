package com.scholar.securitytest.common;

import com.alibaba.fastjson.JSON;
import com.scholar.securitytest.utils.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // 创建统一的响应对象
        ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "认证失败，请重新登录");
        String json = JSON.toJSONString(result);  // 将响应对象转为JSON格式
        WebUtils.renderString(response, json);  // 返回响应
    }
}