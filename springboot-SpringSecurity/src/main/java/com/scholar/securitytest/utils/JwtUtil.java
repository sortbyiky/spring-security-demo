package com.scholar.securitytest.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 * 用于生成、解析和管理JWT（JSON Web Token）
 */
public class JwtUtil {
    // Token 有效期（默认为1小时，单位：毫秒）
    public static final Long JWT_TTL = 60 * 60 * 1000L;

    // 签名密钥明文（用于生成加密密钥）
    public static final String JWT_KEY = "sangeng";

    /**
     * 生成唯一的 UUID
     *
     * @return UUID 字符串
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 创建 JWT（使用默认的有效期）
     *
     * @param subject token 中存储的数据（通常是用户标识信息）
     * @return 生成的 JWT 字符串
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID()); // 设置过期时间
        return builder.compact();
    }

    /**
     * 创建 JWT（指定有效期）
     *
     * @param subject   token 中存储的数据（通常是用户标识信息）
     * @param ttlMillis token 的有效期（毫秒）
     * @return 生成的 JWT 字符串
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID()); // 设置过期时间
        return builder.compact();
    }

    /**
     * 创建 JWT（指定 ID、有效期和存储数据）
     *
     * @param id        token 的唯一标识
     * @param subject   token 中存储的数据
     * @param ttlMillis token 的有效期（毫秒）
     * @return 生成的 JWT 字符串
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id); // 设置过期时间
        return builder.compact();
    }

    /**
     * 获取 JwtBuilder（生成 Token 的核心对象）
     *
     * @param subject   token 中存储的数据
     * @param ttlMillis token 的有效期（毫秒）
     * @param uuid      token 的唯一标识
     * @return JwtBuilder 对象
     */
    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 签名算法
        SecretKey secretKey = generalKey(); // 加密密钥
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis); // 当前时间

        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL; // 如果未指定有效期，使用默认值
        }
        long expMillis = nowMillis + ttlMillis; // 计算过期时间
        Date expDate = new Date(expMillis); // 过期时间

        return Jwts.builder()
                .setId(uuid) // 设置唯一 ID
                .setSubject(subject) // 设置主题（可以是 JSON 数据）
                .setIssuer("sg") // 设置签发者
                .setIssuedAt(now) // 设置签发时间
                .signWith(signatureAlgorithm, secretKey) // 使用 HS256 签名算法和密钥
                .setExpiration(expDate); // 设置过期时间
    }

    /**
     * 解析 JWT
     *
     * @param jwt token 字符串
     * @return 解析后的 Claims（包含 token 中存储的数据）
     * @throws Exception 如果解析失败，抛出异常
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey(); // 获取密钥
        return Jwts.parser()
                .setSigningKey(secretKey) // 设置签名密钥
                .parseClaimsJws(jwt) // 解析 JWT
                .getBody(); // 获取 Claims（存储的内容）
    }

    /**
     * 生成加密后的密钥 SecretKey
     *
     * @return SecretKey 对象
     */
    public static SecretKey generalKey() {
        // 使用 Base64 解码密钥明文
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        // 根据 AES 算法生成加密密钥
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    public static void main(String[] args) throws Exception {
        // 示例：解析一个示例 JWT
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjYWM2ZDVhZi1mNjVlLTQ0MDAtYjcxMi0zYWEwOGIyOTIwYjQiLCJzdWIiOiJzZyIsImlzcyI6InNnIiwiaWF0IjoxNjM4MTA2NzEyLCJleHAiOjE2MzgxMTAzMTJ9.JVsSbkP94wuczb4QryQbAke3ysBDIL5ou8fWsbt_ebg";
        Claims claims = parseJWT(token); // 解析 JWT
        System.out.println(claims); // 打印解析结果
    }
}