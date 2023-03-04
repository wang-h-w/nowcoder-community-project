package com.nowcoder.community.service;

import com.nowcoder.community.entity.Comment;

import java.util.List;

public interface CommentService {

    /**
     * 业务：根据实体的类别及ID（帖子、评论……）筛选评论
     * @param entityType Comment to which entity type
     * @param entityId Entity id corresponding to its type
     * @param offset Start row
     * @param limit Number of comments in a page
     * @return List of comments
     */
    List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit);

    /**
     * 业务：查询某个实体下的评论数目
     * @param entityType Comment to which entity type
     * @param entityId Entity id corresponding to its type
     * @return Number of comments
     */
    int findCommentCount(int entityType, int entityId);

    /**
     * 业务：新增评论
     * @param comment New comment
     */
    void addComment(Comment comment);

    /**
     * 业务：查询某个用户为某类型实体发出的评论
     * @param userId User's id
     * @param entityType Entity type
     * @param offset Start row
     * @param limit Number of comments in a page
     * @return List of comments
     */
    List<Comment> findCommentsByUser(int userId, int entityType, int offset, int limit);

    /**
     * 业务：查询某个用户为某类型实体发出的评论数量
     * @param userId User's id
     * @param entityType Entity type
     * @return Number of comments
     */
    int findCountByUser(int userId, int entityType);
}
