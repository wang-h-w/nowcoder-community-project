package com.nowcoder.community;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class MapperTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelect() {
        User user = userMapper.selectById(22);
        System.out.println(user);

        user = userMapper.selectByName("bbb");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder146@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("123");
        user.setSalt("abc");
        user.setEmail("test@nowcoder.com");
        user.setHeaderUrl("http://images.nowcoder.com/head/146t.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows == 1 ? "Success." : "Fail.");
        System.out.println(user.getId());
    }

    @Test
    public void testUpdate() {
        int rows = userMapper.updatePassword(150, "123456");
        rows += userMapper.updateStatus(150, 1);
        System.out.println(rows == 2 ? "Success." : "Fail.");
    }

    @Test
    public void testSelectPosts() {
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(111, 0, 10);
        for (DiscussPost discussPost : discussPosts) System.out.println(discussPost);
        System.out.println(discussPostMapper.selectDiscussPostRows(111));
    }
}
