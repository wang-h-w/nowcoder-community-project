package com.nowcoder.community.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.ElasticsearchResult;
import com.nowcoder.community.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    private ElasticsearchClient client;

    @Override
    public void saveDiscussPost(DiscussPost post) {
        try {
            client.create(c -> c.index("discusspost").id(String.valueOf(post.getId())).document(post));
        } catch (IOException e) {
            throw new RuntimeException("Elasticsearch文档插入索引失败！");
        }
    }

    @Override
    public void deleteDiscussPost(int id) {
        try {
            client.delete(d -> d.index("discusspost").id(String.valueOf(id)));
        } catch (IOException e) {
            throw new RuntimeException("Elasticsearch删除文档失败！");
        }
    }

    @Override
    public ElasticsearchResult searchDiscussPost(String keyword, int current, int limit) {
        // 设置高亮
        Map<String, HighlightField> map = new HashMap<>();
        map.put("title", new HighlightField.Builder().preTags("<em>").postTags("</em>").build());
        map.put("content", new HighlightField.Builder().preTags("<em>").postTags("</em>").build());

        // 查询并获得查询响应
        SearchResponse<DiscussPost> search = null;
        try {
            search = client.search(
                s -> s.index("discusspost")
                    .query(
                        q -> q.bool(
                            b -> b.should(
                                should -> should.match(
                                    m -> m.field("title")
                                        .query(keyword)
                                )
                            ).should(
                                should -> should.match(
                                    m -> m.field("content")
                                        .query(keyword)
                                )
                            )
                        )
                    )
                    .sort(
                        sort -> sort.field(
                            f -> f.field("type")
                                .order(SortOrder.Desc)
                        )
                    )
                    .sort(
                        sort -> sort.field(
                            f -> f.field("score")
                                .order(SortOrder.Desc)
                        )
                    )
                    .sort(
                        sort -> sort.field(
                            f -> f.field("createTime")
                                .order(SortOrder.Desc)
                        )
                    )
                    .from(current)
                    .size(limit)
                    .highlight(
                        h -> h.fields(map)
                    )
                , DiscussPost.class);
        } catch (IOException e) {
            throw new RuntimeException("Elasticsearch查询索引失败！");
        }

        List<DiscussPost> highlightPost = new ArrayList<>();
        Long total = search.hits().total().value();
        search.hits().hits().forEach(e -> {
            DiscussPost source = e.source();
            if (source != null) {
                Map<String, List<String>> highlight = e.highlight();
                if (highlight != null) {
                    for (Map.Entry<String, List<String>> entry : highlight.entrySet()) {
                        if (entry.getKey().equals("title")) {
                            source.setTitle(entry.getValue().get(0));
                        }
                        if (entry.getKey().equals("content")) {
                            source.setContent(entry.getValue().get(0));
                        }
                    }
                }
            }
            highlightPost.add(source);
        });

        ElasticsearchResult result = new ElasticsearchResult();
        result.setDiscussPosts(highlightPost);
        result.setTotal(total);

        return result;
    }
}
