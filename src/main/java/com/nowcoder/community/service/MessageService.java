package com.nowcoder.community.service;

import com.nowcoder.community.entity.Message;
import com.nowcoder.community.mapper.MessageMapper;

import java.util.List;

public interface MessageService {
    /**
     * 业务：查询当前用户的会话列表，每个会话只返回最新的一条私信
     * @param userId User's id
     * @param offset Page offset
     * @param limit Page limit
     * @return List of conversations (with newest letter)
     */
    List<Message> findConversations(int userId, int offset, int limit);

    /**
     * 业务：查询当前用户的会话总数
     * @param userId User's id
     * @return Count of conversations
     */
    int findConversationCount(int userId);

    /**
     * 业务：查询某个会话所包含的私信列表
     * @param conversationId Conversation's id
     * @param offset Page offset
     * @param limit Page limit
     * @return List of letters
     */
    List<Message> findLetters(String conversationId, int offset, int limit);

    /**
     * 业务：查询某个会话所包含的私信数量
     * @param conversationId Conversation's id
     * @return Count of letters
     */
    int findLetterCount(String conversationId);

    /**
     * 业务：查询当前用户未读私信的数量（可以是总数，也可以是某个会话下的）
     * @param userId User's id
     * @param conversationId Conversation's id
     * @return Count of unread letters
     */
    int findUnreadLetterCount(int userId, String conversationId);

    /**
     * 业务：新增消息
     * @param message New message
     */
    void addMessage(Message message);

    /**
     * 业务：将消息设为已读
     * @param ids Read messages' id
     */
    void readMessage(List<Integer> ids);

    /**
     * 业务：删除消息
     * @param id Message's id
     */
    void deleteMessage(int id);
}
