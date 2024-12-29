package com.scholar.securitytest.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HandControllerTest {

    @RequestMapping(value = "/test")
    public String test() {
        return "HelloWorld";
    }

    // 只有具有 "user:view" 权限的用户才能访问
    @PreAuthorize("hasAuthority('user:view')") // 这里需要修改为数据中的权限标识
    @GetMapping("/sayHello")
    public String sayHello() {
        return "Hello, World!";
    }

    // 只有具有 "admin" 权限的用户才能访问
    @RequestMapping("/admin")
    @PreAuthorize("hasAuthority('admin')")
    public String admin() {
        return "Admin Page";
    }

}
