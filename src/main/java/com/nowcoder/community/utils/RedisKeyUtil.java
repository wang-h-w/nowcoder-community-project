package com.nowcoder.community.utils;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";
    private static final String PREFIX_KAPTCHA = "kaptcha";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";

    /**
     * 生成key，表达某个实体收到的赞
     * like:entity:entityType:entityId -> set(userId)
     * @param entityType Entity type
     * @param entityId Entity id
     * @return Key
     */
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 生成key，表达某个用户收到的赞
     * like:user:userId -> #likes
     * @param userId User's id
     * @return Key
     */
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    /**
     * 生成key，表达某个用户关注的实体
     * followee:userId:entityType -> zset(entityId, now)
     * @param userId User's id
     * @param entityType Type of followee
     * @return Key
     */
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    /**
     * 生成key，表达某个实体拥有的粉丝
     * follower:entityType:entityId -> zset(userId, now)
     * @param entityType Entity type
     * @param entityId Entity id
     * @return Key
     */
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 生成key，表达验证码
     * @param owner Random string for representing the relation between login user and kaptcha
     * @return Key
     */
    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    /**
     * 生成key，表达登录凭证
     * @param ticket Login ticket
     * @return Key
     */
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    /**
     * 生成key，表达用户
     * @param userId User's id
     * @return Key
     */
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }
}
