package com.scholar.securitytest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scholar.securitytest.domain.User;

// 继承 BaseMapper 提供基础的 CRUD 操作
public interface UserMapper extends BaseMapper<User> {}