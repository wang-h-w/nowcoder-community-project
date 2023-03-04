package com.nowcoder.community;

import com.nowcoder.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testStrings() {
        String redisKey = "test:count";
        redisTemplate.opsForValue().set(redisKey, 1);
        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    @Test
    public void testHashes() {
        String redisKey = "test:user";
        redisTemplate.opsForHash().put(redisKey, "id", 1);
        redisTemplate.opsForHash().put(redisKey, "name", "张三");
        System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey, "name"));
    }

    @Test
    public void testLists() {
        String redisKey = "test:ids";
        redisTemplate.opsForList().leftPush(redisKey, 1);
        redisTemplate.opsForList().leftPush(redisKey, 2);
        redisTemplate.opsForList().leftPush(redisKey, 3);
        redisTemplate.opsForList().rightPush(redisKey, 4);

        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(redisTemplate.opsForList().index(redisKey, 1));
        System.out.println(redisTemplate.opsForList().range(redisKey, 1, 3));
        System.out.println(redisTemplate.opsForList().rightPop(redisKey));
    }

    @Test
    public void testSets() {
        String redisKey = "test:teachers";
        redisTemplate.opsForSet().add(redisKey, "张三", "李四", "王五", "赵六");

        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
        System.out.println(redisTemplate.opsForSet().members(redisKey));
    }

    @Test
    public void testSortedSets() {
        String redisKey = "test:students";
        redisTemplate.opsForZSet().add(redisKey, "小明", 80);
        redisTemplate.opsForZSet().add(redisKey, "小红", 75);
        redisTemplate.opsForZSet().add(redisKey, "小李", 84);
        redisTemplate.opsForZSet().add(redisKey, "小张", 97);

        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
        System.out.println(redisTemplate.opsForZSet().score(redisKey, "小李"));
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey, "小红")); // 按倒序排列并取排名
        System.out.println(redisTemplate.opsForZSet().range(redisKey, 0, 4));
        System.out.println(redisTemplate.opsForZSet().reverseRange(redisKey, 0, 4));
    }

    @Test
    public void testKeys() {
        redisTemplate.delete("test:user");
        System.out.println(redisTemplate.hasKey("test:user"));

        redisTemplate.expire("test:count", 10, TimeUnit.SECONDS);
    }

    @Test
    public void testBoundOperations() {
        String redisKey = "test:students";
        BoundZSetOperations<String, Object> operations = redisTemplate.boundZSetOps(redisKey);
        operations.add("小王", 100);
    }

    @Test
    public void testTransactions() {
        Object obj = redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:user";
                operations.multi();
                operations.opsForHash().put(redisKey, "id", 1);
                operations.opsForHash().put(redisKey, "age", 23);
                return operations.exec();
            }
        });
        System.out.println(obj);
    }

    @Test
    public void testObjects() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("123");
        user.setSalt("abc");
        user.setEmail("test@nowcoder.com");
        user.setHeaderUrl("http://images.nowcoder.com/head/146t.png");
        user.setCreateTime(new Date());

        redisTemplate.opsForValue().set("test:object", user);
        Object o = redisTemplate.opsForValue().get("test:object");
        System.out.println(o);
        User u = (User) redisTemplate.opsForValue().get("test:object");
        System.out.println(u);
    }
}
