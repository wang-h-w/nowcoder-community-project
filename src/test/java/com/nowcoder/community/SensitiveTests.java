package com.nowcoder.community;

import com.nowcoder.community.utils.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SensitiveTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testFilter() {
        String text = "这里用来测试敏感词汇，我们不可以赌博，不可以--吸----毒-，也不可以嫖！！！！娼。";
        System.out.println(sensitiveFilter.filter(text));
    }
}
