package com.nowcoder.community.utils;

public interface CommunityConstant {

    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态下的登录凭证超时时间：12小时
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住状态下的登录凭证超时时间：30天
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 30;

    /**
     * 敏感词替换为***
     */
    String REPLACEMENT = "***";

    /**
     * 实体类型：帖子
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型：评论
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
     * 实体类型：用户
     */
    int ENTITY_TYPE_USER = 3;

    /**
     * 登录界面验证码有效时长（秒）
     */
    int KAPTCHA_EXPIRED_SECOND = 60;

    /**
     * 用户缓存有效时长（秒）
     */
    int USER_CACHE_EXPIRED_SECOND = 3600;

    /**
     * Kafka主题：评论
     */
    String TOPIC_COMMENT = "comment";

    /**
     * Kafka主题：点赞
     */
    String TOPIC_LIKE = "like";

    /**
     * Kafka主题：关注
     */
    String TOPIC_FOLLOW = "follow";

    /**
     * Kafka主题：发帖
     */
    String TOPIC_PUBLISH = "publish";

    /**
     * Kafka主题：修改帖子
     */
    String TOPIC_MODIFY = "modify";

    /**
     * Kafka主题：删帖
     */
    String TOPIC_DELETE = "delete";

    /**
     * 系统的用户ID
     */
    int SYSTEM_USER_ID = 1;

    /**
     * 权限：普通用户
     */
    String AUTHORITY_USER = "user";

    /**
     * 权限：管理员
     */
    String AUTHORITY_ADMIN = "admin";

    /**
     * 权限：版主
     */
    String AUTHORITY_MODERATOR = "moderator";
}
