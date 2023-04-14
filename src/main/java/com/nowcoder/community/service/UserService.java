package com.nowcoder.community.service;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

public interface UserService {

    /**
     * 业务：根据用户ID获得用户实例
     * @param id User's id
     * @return User instance
     */
    User findUserById(int id);

    /**
     * 业务：根据用户邮箱获得用户实例
     * @param email User's email
     * @return User instance
     */
    User findUserByEmail(String email);

    /**
     * 业务：根据用户名获得用户实例
     * @param name User's name
     * @return User instance
     */
    User findUserByName(String name);

    /**
     * 业务：注册用户
     * @param user User waited to be registered
     * @return Result info
     */
    Map<String, Object> register(User user);

    /**
     * 业务：根据用户名验证用户激活码
     * @param username User's name
     * @param activationCode Activation code
     * @return Verification result
     */
    int activation(String username, String activationCode);

    /**
     * 业务：用户登录
     * @param username User's name
     * @param password User's password before MD5
     * @param expiredSeconds Expired after how many seconds
     * @return Result info
     */
    Map<String, Object> login(String username, String password, int expiredSeconds);

    /**
     * 业务：账户退出
     * @param ticket Ticket string
     */
    void logout(String ticket);

    /**
     * 业务：查询登录凭证
     * @param ticket Ticket string
     * @return Ticket
     */
    LoginTicket findLoginTicket(String ticket);

    /**
     * 业务：更新用户头像
     * @param userId User's id
     * @param headerUrl New header url
     */
    void updateHeader(int userId, String headerUrl);


    /**
     * 业务：忘记密码时发送验证码
     * @param email User's email
     */
    Map<String, Object> sendForgetCode(String email);

    /**
     * 业务：根据邮箱更新密码
     * @param email User's email
     * @param password New password
     */
    void updatePassword(String email, String password);

    /**
     * 业务：获得某用户的权限
     * @param userId User's id
     * @return The collection of GrantedAuthority
     */
    Collection<? extends GrantedAuthority> getAuthorities(int userId);
}
