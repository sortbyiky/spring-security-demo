package com.scholar.securitytest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scholar.securitytest.domain.LoginUser;
import com.scholar.securitytest.domain.User;
import com.scholar.securitytest.mapper.MenuMapper;
import com.scholar.securitytest.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service // 声明为服务层组件
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper; // 注入 UserMapper，用于与数据库交互

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询数据库中的用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);

        // 如果用户不存在，抛出异常
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名或密码错误");
        }

        // TODO: 如果需要权限信息，可在此处查询并封装
//        List<String> permissions = new ArrayList<>(Arrays.asList("test","admin"));
        List<String> permissions = menuMapper.selectPermsByUserId(user.getId());

        // 将用户信息封装为自定义的 UserDetails 实现类
        return new LoginUser(user,permissions);
    }
}