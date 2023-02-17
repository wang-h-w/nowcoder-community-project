package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;

public interface UserService {

    /**
     * 业务：根据用户ID获得用户实例
     * @param id User's id
     * @return User instance
     */
    User findUserById(int id);
}
