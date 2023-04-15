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

    /**
     * 业务：新增帖子
     * @param post New post
     */
    void addDiscussPost(DiscussPost post);

    /**
     * 根据ID查询帖子
     * @param id Post's id
     * @return Post
     */
    DiscussPost findDiscussPostById(int id);

    /**
     * 业务：更新帖子的评论数量
     * @param id Post's id
     * @param commentCount New count of comment
     */
    void updateCommentCount(int id, int commentCount);

    /**
     * 业务：更新帖子的类型
     * @param id Post's id
     * @param type 0-普通; 1-置顶
     * @return 1-success; 0-fail
     */
    int updateType(int id, int type);

    /**
     * 业务：更新帖子的状态
     * @param id Post's id
     * @param status 0-正常; 1-精华; 2-拉黑
     * @return 1-success; 0-fail
     */
    int updateStatus(int id, int status);
}
