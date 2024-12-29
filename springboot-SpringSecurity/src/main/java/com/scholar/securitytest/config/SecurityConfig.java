package com.scholar.securitytest.config;

import com.scholar.securitytest.common.AccessDeniedHandlerImpl;
//import com.scholar.securitytest.common.AuthenticationEntryPointImpl;
//import com.scholar.securitytest.common.AuthenticationEntryPointImpl;
import com.scholar.securitytest.common.AuthenticationEntryPointImpl;
import com.scholar.securitytest.utils.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity  // 开启Spring Security功能
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 启用基于注解的权限控制
public class SecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;  // 注入AuthenticationConfiguration以获取AuthenticationManager

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    AccessDeniedHandlerImpl accessDeniedHandler;  // 注入自定义的AccessDeniedHandler

    @Autowired
    AuthenticationEntryPointImpl authenticationEntryPoint;  // 注入自定义的AuthenticationEntryPoint

    // 配置密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 使用BCrypt加密算法
    }

    // 配置AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();  // 获取AuthenticationManager
    }

    /**
     * 配置Spring Security的过滤链。
     *
     * @param http 用于构建安全配置的HttpSecurity对象。
     * @return 返回配置好的SecurityFilterChain对象。
     * @throws Exception 如果配置过程中发生错误，则抛出异常。
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // 禁用CSRF保护
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 设置无状态会话
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/user/login").anonymous()  // 放行登录接口，允许匿名访问
                        .requestMatchers("/admin/**").hasAuthority("admin")  // 只有admin角色才能访问/admin/**路径
                        .requestMatchers("/user/**").hasAuthority("user")  // 只有user角色才能访问/user/**路径
                    .anyRequest().authenticated())  // 其他接口需要身份认证
                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())) // 配置CORS
                // 添加JWT认证过滤器，确保在UsernamePasswordAuthenticationFilter之前执行
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 配置异常处理
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler)// 配置授权失败处理器
                        .authenticationEntryPoint(authenticationEntryPoint));  // 配置认证失败处理器;
//        // 返回配置好的过滤链
        return http.build();
    }
}