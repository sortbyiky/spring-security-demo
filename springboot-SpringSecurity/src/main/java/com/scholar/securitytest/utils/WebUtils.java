package com.scholar.securitytest.utils;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Web 工具类
 * 提供 Web 开发中的常用功能，主要用于将字符串渲染到客户端。
 */
public class WebUtils {

    /**
     * 将字符串渲染到客户端
     * 
     * 该方法通过设置 HTTP 响应的状态码、内容类型和字符编码，
     * 将指定的字符串直接写入响应体中，返回给前端。
     *
     * @param response HttpServletResponse 对象，用于向客户端发送响应
     * @param string   待渲染的字符串（通常是 JSON 格式的字符串）
     * @return null 始终返回 null
     */
    public static String renderString(HttpServletResponse response, String string) {
        try {
            // 设置响应状态码为 200（OK）
            response.setStatus(200);

            // 设置响应内容类型为 JSON
            response.setContentType("application/json");

            // 设置字符编码为 UTF-8，防止中文乱码
            response.setCharacterEncoding("utf-8");

            // 将字符串写入到响应体中，返回给客户端
            response.getWriter().print(string);
        } catch (IOException e) {
            // 如果发生 I/O 异常，打印堆栈信息以便调试
            e.printStackTrace();
        }

        // 返回 null 作为默认值
        return null;
    }
}