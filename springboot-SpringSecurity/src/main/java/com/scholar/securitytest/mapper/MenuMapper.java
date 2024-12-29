package com.scholar.securitytest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scholar.securitytest.domain.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    // 根据用户ID查询该用户的所有权限
    List<String> selectPermsByUserId(@Param("userId") Long userId);
}