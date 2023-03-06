package com.nowcoder.community.entity;

import java.util.List;

public class ElasticsearchResult {
    private List<DiscussPost> discussPosts;
    private Long total;

    @Override
    public String toString() {
        return "ElasticsearchResult{" +
                "discussPosts=" + discussPosts +
                ", total=" + total +
                '}';
    }

    public List<DiscussPost> getDiscussPosts() {
        return discussPosts;
    }

    public void setDiscussPosts(List<DiscussPost> discussPosts) {
        this.discussPosts = discussPosts;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
