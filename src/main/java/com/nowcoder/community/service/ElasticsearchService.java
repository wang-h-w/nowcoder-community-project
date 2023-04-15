package com.nowcoder.community.service;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.ElasticsearchResult;


public interface ElasticsearchService {

    /**
     * 业务：存储一个帖子到索引中
     * @param post Discuss post
     */
    void saveDiscussPost(DiscussPost post);

    /**
     * 业务：修改索引中的一个帖子
     * @param post Discuss post
     */
    void updateDiscussPost(DiscussPost post);

    /**
     * 业务：从索引中删除一个帖子
     * @param id Discuss post's id
     */
    void deleteDiscussPost(int id);

    /**
     * 业务：高亮显示关键字的查询结果
     * @param keyword Keyword waited to be highlighted
     * @param current Current page
     * @param limit Page limit
     * @return Map contains list of discuss post and total number
     */
    ElasticsearchResult searchDiscussPost(String keyword, int current, int limit);
}
