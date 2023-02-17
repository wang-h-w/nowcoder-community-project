package com.nowcoder.community;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class ServiceTests {

    @Autowired
    UserService userService;

    @Test
    public void testRegister() {
        User user = new User();
        user.setUsername("Develop testing");
        user.setPassword("abcabcabc");
        user.setEmail("wang.h.w@outlook.com");
        Map<String, Object> msg = userService.register(user);
        if (msg.isEmpty()) System.out.println("一切正常");
        else {
            for (String key : msg.keySet()) {
                System.out.println(key + ": " + msg.get(key));
            }
        }
    }
}
