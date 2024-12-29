package com.scholar.securitytest.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 登录用户的封装类，实现 Spring Security 的 UserDetails 接口。
 * 用于封装用户信息和权限信息，并提供给 Spring Security 进行身份认证和授权。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements UserDetails {

    /**
     * 封装用户的基本信息（如用户名、密码等），该对象来自数据库。
     */
    private User user;

    /**
     * 用户的权限信息，通常是一个权限的列表。
     * 这些权限信息通常来自数据库，并将用于控制用户可以访问的资源。
     */
    private List<String> permissions;

    /**
     * 用户权限的 Spring Security 表示形式，存储在 SimpleGrantedAuthority 对象中。
     * 这个字段不参与序列化，以避免冗余数据被存入 Redis。
     */
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    /**
     * 带有用户和权限信息的构造函数。
     *
     * @param user 用户信息
     * @param permissions 用户的权限信息列表
     */
    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    /**
     * 重载的构造函数（此构造函数为空的，仅用于初始化）。
     */
    public LoginUser(User user) {
    }

    /**
     * 获取用户的权限信息，转换为 Spring Security 所需要的 GrantedAuthority 对象。
     *
     * @return 用户的权限信息
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 如果 authorities 尚未被设置，进行权限信息的转换
        if (authorities == null) {
            // 将字符串类型的权限信息（如 "ROLE_USER"）转换为 SimpleGrantedAuthority 对象
            authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)  // 将权限字符串映射为 GrantedAuthority 对象
                    .collect(Collectors.toList());  // 收集成一个 List
        }
        return authorities;
    }

    /**
     * 获取用户的密码。
     *
     * @return 用户的密码
     */
    @Override
    public String getPassword() {
        return user.getPassword();  // 返回用户密码
    }

    /**
     * 获取用户的用户名。
     *
     * @return 用户名
     */
    @Override
    public String getUsername() {
        return user.getUserName();  // 返回用户名
    }

    /**
     * 判断账户是否已过期。返回 true 表示账户未过期。
     *
     * @return 是否账户未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;  // 返回 true 表示账户未过期
    }

    /**
     * 判断账户是否已被锁定。返回 true 表示账户未被锁定。
     *
     * @return 是否账户未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;  // 返回 true 表示账户未被锁定
    }

    /**
     * 判断凭证（即密码）是否已过期。返回 true 表示凭证未过期。
     *
     * @return 是否凭证未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 返回 true 表示凭证未过期
    }

    /**
     * 判断账户是否可用。返回 true 表示账户可用。
     *
     * @return 是否账户可用
     */
    @Override
    public boolean isEnabled() {
        return true;  // 返回 true 表示账户可用
    }
}
