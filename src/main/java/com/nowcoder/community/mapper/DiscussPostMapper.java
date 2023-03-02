package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    /**
     * 根据用户ID查询帖子，当用户ID为0时默认显示所有帖子
     * @param userId User's ID
     * @param offset Start row number of a page
     * @param limit Number of posts in one page
     * @return List of posts
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    /**
     * 根据用户ID查询帖子总数，当用户ID为0时默认查询所有帖子
     * @param userId User's ID
     * @return Number of rows
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    /**
     * 新增帖子到数据库中
     * @param post New post
     * @return 1-success; 0-fail
     */
    int insertDiscussPost(DiscussPost post);

    /**
     * 根据帖子的ID查询帖子
     * @param id Post's id
     * @return Post
     */
    DiscussPost selectDiscussPostById(int id);

    /**
     * 更新帖子的评论数量
     * @param id Post's id
     * @param commentCount New count of comment
     * @return 1-success; 0-fail
     */
    int updateCommentCount(int id, int commentCount);
}
