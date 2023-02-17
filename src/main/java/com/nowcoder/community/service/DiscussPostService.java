package com.nowcoder.community.service;

import com.nowcoder.community.entity.DiscussPost;
import java.util.List;

public interface DiscussPostService {

    /**
     * 业务：根据用户ID查询帖子，当用户ID为0时默认显示所有帖子
     * @param userId User's ID
     * @param offset Start row number of a page
     * @param limit Number of posts in one page
     * @return List of posts
     */
    List<DiscussPost> findDiscussPosts(int userId, int offset, int limit);

    /**
     * 业务：根据用户ID查询帖子总数，当用户ID为0时默认查询所有帖子
     * @param userId User's ID
     * @return Number of rows
     */
    int findDiscussPostRows(int userId);
}
