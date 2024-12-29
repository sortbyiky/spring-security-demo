package com.scholar.securitytest.utils;

import com.scholar.securitytest.domain.LoginUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
// OncePerRequestFilter 保证每次请求该过滤器的 doFilterInternal 方法只执行一次
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    RedisCache redisCache;  // 用于从Redis中获取用户信息

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 在请求头中获取token
        String token = request.getHeader("token");

        // 如果token为空，直接放行，SecurityContextHolder中没有用户信息，后续的过滤器会进行处理
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String subject;
        try {
            // 2. 解析token，获取用户id（subject）
            Claims claims = JwtUtil.parseJWT(token);  // 解析JWT
            subject = claims.getSubject();  // 获取subject（即用户ID）
        } catch (Exception e) {
            // 解析失败，抛出异常
            throw new RuntimeException("token非法");
        }

        // 3. 使用用户id（subject）从Redis中获取用户信息
        String redisKey = "login:" + subject;  // Redis中的key格式为 "login:userId"
        LoginUser loginUser = redisCache.getCacheObject(redisKey);  // 从Redis中获取LoginUser对象

        if (Objects.isNull(loginUser)) {
            // 如果Redis中没有找到用户信息，抛出异常表示用户未登录
            throw new RuntimeException("用户未登录");
        }

        // 4. 如果用户信息存在，将其封装为Authentication对象并设置到SecurityContextHolder中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);  // 设置认证信息

        // 5. 放行请求
        filterChain.doFilter(request, response);
    }
}