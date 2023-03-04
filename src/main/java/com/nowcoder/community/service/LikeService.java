package com.nowcoder.community.service;

public interface LikeService {

    /**
     * 业务：点赞
     * @param userId Who sent the like
     * @param entityType Like on what type of entity
     * @param entityId Like on which entity
     * @param entityUserId Which user create this entity
     */
    void like(int userId, int entityType, int entityId, int entityUserId);

    /**
     * 业务：查询某个实体的被点赞数量
     * @param entityType Entity type
     * @param entityId Entity id
     * @return Number of likes
     */
    long findEntityLikeCount(int entityType, int entityId);

    /**
     * 业务：查询某用户对某实体的点赞状态
     * @param userId User's id
     * @param entityType Entity type
     * @param entityId Entity id
     * @return Like status
     */
    int findEntityLikeStatus(int userId, int entityType, int entityId);

    /**
     * 业务：查询某个用户获得的赞
     * @param userId User's id
     * @return Number of likes
     */
    int findUserLikeCount(int userId);
}
