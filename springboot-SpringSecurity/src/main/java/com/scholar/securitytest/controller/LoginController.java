package com.scholar.securitytest.controller;

import com.scholar.securitytest.common.ResponseResult;
import com.scholar.securitytest.domain.User;
import com.scholar.securitytest.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    // 登录接口，接收用户名和密码，进行认证
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        System.out.println(user);
        return loginService.login(user);
    }



    @PostMapping("/user/logout")
    public ResponseResult logout() {
        System.out.println("开始登出");
        return loginService.logout();  // 调用服务层的退出登录方法
    }
}