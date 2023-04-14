package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.LoginTicketMapper;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.MailClient;
import com.nowcoder.community.utils.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.nowcoder.community.utils.CommunityConstant.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    // @Autowired
    // private LoginTicketMapper loginTicketMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine; // 手动封装Model，代替Controller功能
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public User findUserById(int id) {
        // return userMapper.selectById(id);
        User user = getCache(id);
        if (user == null) user = initCache(id);
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    @Override
    public User findUserByName(String name) {
        return userMapper.selectByName(name);
    }

    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 非空验证
        if (user == null) throw new IllegalArgumentException("参数不能为空！");
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空！");
            return map;
        }

        // 存在性验证
        if (userMapper.selectByName(user.getUsername()) != null) {
            map.put("usernameMsg", "用户名已存在！");
            return map;
        }
        if (userMapper.selectByEmail(user.getEmail()) != null) {
            map.put("emailMsg", "邮箱已存在！");
            return map;
        }

        // 注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 生成验证邮件的动态HTML页面
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        String url = domain + contextPath + "/activation/" + user.getUsername() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation.html", context);
        mailClient.sendMail(user.getEmail(), "Nowcoder: Please activate your account!", content);

        return map;
    }

    @Override
    public int activation(String username, String activationCode) {
        User user = userMapper.selectByName(username);
        if (user.getStatus() == 1) return ACTIVATION_REPEAT;
        else if (user.getActivationCode().equals(activationCode)) {
            userMapper.updateStatus(user.getId(), 1);
            clearCache(user.getId());
            return ACTIVATION_SUCCESS;
        }
        else return ACTIVATION_FAILURE;
    }

    @Override
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 非空验证
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "用户名不能为空！");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在！");
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            map.put("statusMsg", "该账号未激活！");
            return map;
        }

        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确！");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000L * expiredSeconds));
        // loginTicketMapper.insertLoginTicket(loginTicket);

        // 将登录凭证存储到Redis
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);

        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    @Override
    public void logout(String ticket) {
        // loginTicketMapper.updateStatus(ticket, 1);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        if (loginTicket != null) {
            loginTicket.setStatus(1);
            redisTemplate.opsForValue().set(redisKey, loginTicket);
        }
    }

    @Override
    public LoginTicket findLoginTicket(String ticket) {
        // return loginTicketMapper.selectByTicket(ticket);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public void updateHeader(int userId, String headerUrl) {
        userMapper.updateHeader(userId, headerUrl);
        clearCache(userId);
    }

    @Override
    public Map<String, Object> sendForgetCode(String email) {
        Map<String, Object> map = new HashMap<>();

        // 非空验证
        if (StringUtils.isBlank(email)) {
            map.put("emailMsg", "邮箱不能为空！");
            return map;
        }

        // 存在性验证
        User user = userMapper.selectByEmail(email);
        if (user == null) {
            map.put("emailMsg", "邮箱不存在！");
            return map;
        }

        // 生成激活码
        String code = CommunityUtil.generateUUID().substring(0, 6);
        String codeInSession = CommunityUtil.md5(email + code);
        System.out.println(codeInSession);
        map.put("codeInSession", codeInSession);

        // 发送验证码邮件
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("code", code);
        String content = templateEngine.process("/mail/forget.html", context);
        mailClient.sendMail(email, "Nowcoder: This is your forget code!", content);
        map.put("sent", "验证码已发送至邮箱！");

        return map;
    }

    @Override
    public void updatePassword(String email, String password) {
        User user = userMapper.selectByEmail(email);
        password = CommunityUtil.md5(password + user.getSalt());
        userMapper.updatePassword(user.getId(), password);
        clearCache(user.getId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = findUserById(userId);
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1:
                        return AUTHORITY_ADMIN;
                    case 2:
                        return AUTHORITY_MODERATOR;
                    default:
                        return AUTHORITY_USER;
                }
            }
        });
        return list;
    }

    // Redis缓存：优先从缓存中取值
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    // Redis缓存：如果从缓存中无法取到，则初始化缓存数据
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, USER_CACHE_EXPIRED_SECOND, TimeUnit.SECONDS);
        return user;
    }

    // Redis缓存：当用户数据变更时，删除缓存（下一次取不到则会创建新的缓存数据）
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }
}
