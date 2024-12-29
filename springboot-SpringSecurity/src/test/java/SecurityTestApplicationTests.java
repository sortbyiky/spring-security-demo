
import com.scholar.securitytest.SecurityTestApplication;
import com.scholar.securitytest.domain.User;
import com.scholar.securitytest.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = SecurityTestApplication.class) // 指定主配置类
class SecurityTestApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        // 查询所有用户
        List<User> users = userMapper.selectList(null);

        // 打印用户信息
        users.forEach(System.out::println);
    }
}