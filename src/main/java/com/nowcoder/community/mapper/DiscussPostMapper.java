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
}
