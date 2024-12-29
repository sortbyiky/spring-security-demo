# Spring Security 快速入门

## 1. 简介

[Spring Security 中文文档](https://springdoc.cn/spring-security/)

Spring Security 是一个功能强大的 Java 安全框架，用于保护应用程序的安全性。它提供了全面的安全解决方案，包括：

- **身份验证（Authentication）**：验证用户身份，确认用户是否登录及其身份信息。
- **授权（Authorization）**：控制用户是否有权限访问特定资源。
- **防御攻击**：包括 CSRF、会话固定攻击等。
- **灵活扩展**：支持集成第三方身份验证提供商（如 OAuth2、JWT）。

Spring Security 基于 **过滤器链** 的概念，通过一组可配置的过滤器实现安全功能，可以无缝集成到基于 Spring 的应用程序中，是现代应用程序开发中的关键安全组件。

**认证与授权**

- **认证**：验证当前访问系统的用户是否是合法用户，并确认具体是谁。
- **授权**：在用户通过认证后，判断其是否有权限执行某个操作。

Spring Security 的核心功能正是围绕 **认证** 和 **授权** 展开的。

------

## 2. 快速入门

以下将展示如何快速集成 Spring Security 到 Spring Boot 项目中，并测试其默认功能。

------

### **2.1 添加依赖**

在 `pom.xml` 中添加以下依赖：

```xml
<!-- Spring Security 核心依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Spring Web 核心依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

> **说明**：
>
> - `spring-boot-starter-security`：用于引入 Spring Security 的核心功能。
> - `spring-boot-starter-web`：用于构建 Web 应用程序。

------

### **2.2 启动类**

创建或修改 Spring Boot 项目的主启动类。

```java
@SpringBootApplication
public class SanGengSecurityApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot 应用程序
        ConfigurableApplicationContext run = SpringApplication.run(SanGengSecurityApplication.class, args);
        
        // 打印启动参数（用于调试）
        System.out.println("args = " + args);
    }
}
```

> **说明**：
>
> - 默认生成的主启动类已基本满足需求，此处仅额外添加打印功能方便调试。

------

### **2.3 创建一个简单的 Controller**

新增一个简单的 Controller 类，用于测试 Spring Security 的默认功能。

```java
@RestController
@RequestMapping("/hello")
public class HelloController {

    // 定义一个简单的 GET 请求，返回 "hello"
    @GetMapping
    public String hello() {
        return "hello";
    }
}
```

> **说明**：
>
> - 使用 `@RestController` 创建一个简单的 RESTful API。
> - 定义了一个 `/hello` 的接口，直接返回字符串 `hello`。

------

### **2.4 启动项目并测试**

**步骤 1：启动项目**

运行项目后，默认端口为 `8080`，访问 `http://localhost:8080/`，浏览器会重定向到 Spring Security 提供的默认登录页面。

![image-20241222220834671](https://web-183.oss-cn-beijing.aliyuncs.com/typora/202412222208269.png)

------

**步骤 2：查看控制台默认用户名和密码**

Spring Security 默认生成一个用户名为 `user` 的账号，随机密码会在控制台打印，如下所示：

```plaintext
Using generated security password: 12345678-abcd-1234-efgh-5678ijklmnop
```

------

**步骤 3：登录系统**

在登录页面：

- **用户名**：`user`
- **密码**：控制台生成的随机密码（如上）。

登录成功后，将被重定向到原始请求的页面。如果直接访问 `http://localhost:8080/hello`，将显示：`hello`

![image-20241222220912379](https://web-183.oss-cn-beijing.aliyuncs.com/typora/202412222209984.png)

------

**步骤 4：测试注销功能**

访问 `http://localhost:8080/logout`，系统将提示是否确认注销。确认后，用户会被注销，返回到登录页面。

![image-20241222221126153](https://web-183.oss-cn-beijing.aliyuncs.com/typora/202412282238014.png)



