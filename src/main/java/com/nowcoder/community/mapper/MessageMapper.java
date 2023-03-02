package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    /**
     * 查询当前用户的会话列表，每个会话只返回最新的一条私信
     * @param userId User's id
     * @param offset Page offset
     * @param limit Page limit
     * @return List of conversations (with newest letter)
     */
    List<Message> selectConversations(int userId, int offset, int limit);

    /**
     * 查询当前用户的会话总数
     * @param userId User's id
     * @return Count of conversations
     */
    int selectConversationCount(int userId);

    /**
     * 查询某个会话所包含的私信列表
     * @param conversationId Conversation's id
     * @param offset Page offset
     * @param limit Page limit
     * @return List of letters
     */
    List<Message> selectLetters(String conversationId, int offset, int limit);

    /**
     * 查询某个会话所包含的私信数量
     * @param conversationId Conversation's id
     * @return Count of letters
     */
    int selectLetterCount(String conversationId);

    /**
     * 查询当前用户未读私信的数量（可以是总数，也可以是某个会话下的）
     * @param userId User's id
     * @param conversationId Conversation's id
     * @return Count of unread letters
     */
    int selectUnreadLetterCount(int userId, String conversationId);

    /**
     * 新增消息
     * @param message New message
     * @return 1-success; 0-fail
     */
    int insertMessage(Message message);

    /**
     * 修改消息的状态
     * @param ids List of ids
     * @param status New status
     * @return 1-success; 0-fail
     */
    int updateStatus(List<Integer> ids, int status);
}
