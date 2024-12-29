package com.scholar.securitytest.service;

import com.scholar.securitytest.common.ResponseResult;
import com.scholar.securitytest.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}