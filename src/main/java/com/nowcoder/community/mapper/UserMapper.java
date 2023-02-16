package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    /**
     * 根据用户ID查询用户
     * @param id User's id
     * @return User instance
     */
    User selectById(int id);

    /**
     * 根据用户名查询用户
     * @param username User's name
     * @return User instance
     */
    User selectByName(String username);

    /**
     * 根据邮箱查询用户
     * @param email User's email
     * @return User instance
     */
    User selectByEmail(String email);

    /**
     * 创建用户
     * @param user User instance
     * @return 1-success; 0-fail
     */
    int insertUser(User user);

    /**
     * 根据用户ID修改用户状态
     * @param id User's id
     * @param status New status
     * @return 1-success; 0-fail
     */
    int updateStatus(int id, int status);

    /**
     * 根据用户ID修改用户头像
     * @param id User's ID
     * @param headerUrl New header url
     * @return 1-success; 0-fail
     */
    int updateHeader(int id, String headerUrl);

    /**
     * 根据用户ID修改用户密码
     * @param id User's id
     * @param password New password
     * @return 1-success; 0-fail
     */
    int updatePassword(int id, String password);
}
