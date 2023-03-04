package com.nowcoder.community.service;

import java.util.List;
import java.util.Map;

public interface FollowService {

    /**
     * 业务：关注
     * @param userId Current user's id
     * @param entityType The type of entity waited to be followed
     * @param entityId The id of entity waited to be followed
     */
    void follow(int userId, int entityType, int entityId);

    /**
     * 业务：取消关注
     * @param userId Current user's id
     * @param entityType The type of entity waited to be unfollowed
     * @param entityId The id of entity waited to be unfollowed
     */
    void unfollow(int userId, int entityType, int entityId);

    /**
     * 业务：查询关注的实体数量
     * @param userId User's id
     * @param entityType Entity type
     * @return Number of followee
     */
    long findFolloweeCount(int userId, int entityType);

    /**
     * 业务：查询实体的粉丝数量
     * @param entityType Entity type
     * @param entityId Entity id
     * @return Number of follower
     */
    long findFollowerCount(int entityType, int entityId);

    /**
     * 业务：查询是否已关注某实体
     * @param userId User's id
     * @param entityType Entity type
     * @param entityId Entity id
     * @return Followed or not
     */
    boolean hasFollowed(int userId, int entityType, int entityId);

    /**
     * 业务：查询某用户关注的人
     * @param userId User's id
     * @param offset Page offset
     * @param limit Page limit
     * @return List of followee
     */
    List<Map<String, Object>> findFollowee(int userId, int offset, int limit);

    /**
     * 业务：查询某用户的粉丝
     * @param userId User's id
     * @param offset Page offset
     * @param limit Page limit
     * @return List of follower
     */
    List<Map<String, Object>> findFollower(int userId, int offset, int limit);
}
