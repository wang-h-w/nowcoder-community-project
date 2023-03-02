package com.nowcoder.community;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.mapper.LoginTicketMapper;
import com.nowcoder.community.mapper.MessageMapper;
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
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Autowired
    private MessageMapper messageMapper;

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

    @Test
    public void testLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(123);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        int cnt = loginTicketMapper.insertLoginTicket(loginTicket);
        System.out.println(cnt == 1 ? "Success." : "Fail.");
        System.out.println(loginTicketMapper.selectByTicket("abc"));
        cnt = loginTicketMapper.updateStatus("abc", 1);
        System.out.println(cnt == 1 ? "Success." : "Fail.");
        System.out.println(loginTicketMapper.selectByTicket("abc"));
    }

    @Test
    public void testMessage() {
        List<Message> messages = messageMapper.selectConversations(111, 0, 20);
        for (Message message : messages) System.out.println(message);

        int count1 = messageMapper.selectConversationCount(111);
        System.out.println(count1);

        List<Message> letters = messageMapper.selectLetters("111_112", 0, 20);
        for (Message message : letters) System.out.println(message);

        int count2 = messageMapper.selectLetterCount("111_112");
        System.out.println(count2);

        int count3 = messageMapper.selectUnreadLetterCount(131, "111_131");
        System.out.println(count3);
    }
}
