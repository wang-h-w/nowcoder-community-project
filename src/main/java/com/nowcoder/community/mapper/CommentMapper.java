package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    /**
     * 根据实体的类别及ID（帖子、评论……）筛选评论
     * @param entityType Comment to which entity type
     * @param entityId Entity id corresponding to its type
     * @param offset Start row
     * @param limit Number of comments in a page
     * @return List of comments
     */
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    /**
     * 查询某个实体下的评论数目
     * @param entityType Comment to which entity type
     * @param entityId Entity id corresponding to its type
     * @return Number of comments
     */
    int selectCountByEntity(int entityType, int entityId);

    /**
     * 新增评论
     * @param comment New comment
     * @return 1-success; 0-fail
     */
    int insertComment(Comment comment);

    /**
     * 查询某个用户为某类型实体发出的评论
     * @param userId User's id
     * @param entityType Entity type
     * @param offset Start row
     * @param limit Number of comments in a page
     * @return List of comments
     */
    List<Comment> selectCommentsByUser(int userId, int entityType, int offset, int limit);

    /**
     * 查询某个用户为某类型实体发出的评论数量
     * @param userId User's id
     * @param entityType Entity type
     * @return Number of comments
     */
    int selectCountByUser(int userId, int entityType);
}
