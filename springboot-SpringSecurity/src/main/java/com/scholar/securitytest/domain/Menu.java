package com.scholar.securitytest.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_menu")
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 菜单ID
    private String menuName; // 菜单名称
    private Long parentId; // 父菜单ID
    private Integer orderNum; // 显示顺序
    private String path; // 路由地址
    private String component; // 组件路径
    private Integer isFrame; // 是否为外链（0是 1否）
    private String menuType; // 菜单类型（M目录 C菜单 F按钮）
    private String visible; // 菜单状态（0显示 1隐藏）
    private String status; // 菜单状态（0正常 1停用）
    private String perms; // 权限标识
    private String icon; // 菜单图标
    private Long createBy; // 创建者
    private LocalDateTime createTime; // 创建时间
    private Long updateBy; // 更新者
    private LocalDateTime updateTime; // 更新时间
    private String remark; // 备注
    private String delFlag; // 删除标志
}