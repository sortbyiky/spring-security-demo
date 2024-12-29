package com.scholar.securitytest.service.impl;

import com.scholar.securitytest.common.ResponseResult;
import com.scholar.securitytest.domain.LoginUser;
import com.scholar.securitytest.domain.User;
import com.scholar.securitytest.service.LoginService;
import com.scholar.securitytest.utils.JwtUtil;
import com.scholar.securitytest.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;  // 用于进行用户认证
    @Autowired
    private RedisCache redisCache;  // 用于存储用户信息到Redis

    @Override
    public ResponseResult login(User user) {

        // 1. 封装Authentication对象，用于携带用户名和密码进行认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());

        // 2. 通过AuthenticationManager的authenticate方法进行认证
        Authentication authenticated = authenticationManager.authenticate(authenticationToken);

        // 3. 获取认证后的用户信息
        LoginUser loginUser = (LoginUser) authenticated.getPrincipal();
        String userId = loginUser.getUser().getId().toString();  // 获取用户ID

        // 4. 认证通过后生成JWT令牌
        String jwt = JwtUtil.createJWT(userId);

        // 5. 将用户信息存入Redis缓存，key为"userId"
        redisCache.setCacheObject("login:" + userId, loginUser);

        // 6. 返回包含JWT的响应
        HashMap<Object, Object> response = new HashMap<>();
        response.put("token", jwt);
        return new ResponseResult(200, "登录成功", response);
    }

    @Override
    public ResponseResult logout() {
        // 获取SecurityContextHolder中的用户认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();  // 获取当前认证的用户
        Long userId = loginUser.getUser().getId();  // 获取当前用户的ID

        // 删除redis中的用户信息，缓存失效
        redisCache.deleteObject("login:" + userId);  // 删除Redis中的用户缓存

        // 返回退出成功的响应
        return new ResponseResult(200, "退出成功");
    }
}