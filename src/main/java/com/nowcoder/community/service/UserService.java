package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;

import java.util.Map;

public interface UserService {

    /**
     * 业务：根据用户ID获得用户实例
     * @param id User's id
     * @return User instance
     */
    User findUserById(int id);

    /**
     * 业务：注册用户
     * @param user User waited to be registered
     * @return Result info
     */
    Map<String, Object> register(User user);

    /**
     * 业务：根据用户名验证用户激活码
     * @param username
     * @param activationCode
     * @return
     */
    int activation(String username, String activationCode);
}
