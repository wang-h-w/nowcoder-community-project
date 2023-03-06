package com.nowcoder.community;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.CreateOperation;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import com.alibaba.fastjson2.JSONObject;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.mapper.elasticsearch.DiscussPostRepository;
import org.apache.kafka.common.security.auth.Login;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ElasticsearchTests {

    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private DiscussPostRepository discussPostRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private ElasticsearchClient client;

    @Test
    public void testInsert() {
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(241));
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(242));
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(243));
    }

    @Test
    public void testInsertList() {
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(101, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(102, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(103, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(111, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(112, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(131, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(132, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(133, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(134, 0, 100));
    }

    @Test
    public void testUpdate() {
        DiscussPost discussPost = discussPostMapper.selectDiscussPostById(231);
        discussPost.setContent("啦啦啦啦啦啦");
        discussPostRepository.save(discussPost);
    }

    @Test
    public void testDelete() {
        discussPostRepository.deleteById(231);
    }

    @Test
    public void testNewCreate() throws IOException {
        final ElasticsearchIndicesClient indices = client.indices(); // 用来操作索引

        // 构建器创建方式
        // CreateIndexRequest request = new CreateIndexRequest.Builder().index("testing").build();
        // CreateIndexResponse createIndexResponse = indices.create(request);
        // System.out.println("创建索引的响应对象" + createIndexResponse);

        // 采用lambda表达式简化
        CreateIndexResponse testing = indices.create(c -> c.index("testing")); // 这里的c其实就是Builder
        System.out.println(testing);

        // 删除索引
        DeleteIndexResponse delete = indices.delete(d -> d.index("testing"));
        System.out.println("索引删除成功：" + delete);
    }

    @Test
    public void testNewDocument() throws Exception{
        // 批量添加
        List<BulkOperation> opts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LoginTicket loginTicket = new LoginTicket();
            loginTicket.setUserId(i);
            loginTicket.setTicket("Ticket: " + i);
            // 对应到Java的实体类
            CreateOperation<LoginTicket> optObj = new CreateOperation.Builder<LoginTicket>()
                    .index("testing")
                    .id("200" + i)
                    .document(loginTicket) // 文档即为一个Java对象
                    .build();
            BulkOperation opt = new BulkOperation.Builder().create(optObj).build(); // 每个operation明确为create
            opts.add(opt);
        }
        BulkRequest bulkRequest = new BulkRequest.Builder().operations(opts).build(); // bulk表示批量
        BulkResponse bulk = client.bulk(bulkRequest);
        System.out.println("批量添加的响应" + bulk);

        // 删除
        DeleteRequest request = new DeleteRequest.Builder()
                .index("testing")
                .id("2003") // 只删除一个
                .build();
        DeleteResponse delete = client.delete(request);
        System.out.println(delete);
    }

    @Test
    public void testNewDocumentLambda() throws Exception{
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(3000);
        loginTicket.setTicket("Ticket: " + 3000);
        CreateResponse testing = client.create(
                c -> c.index("testing")
                        .id("3000")
                        .document(loginTicket)
        );
        System.out.println(testing);
    }

    @Test
    public void testNewSearch() throws Exception {
        MatchQuery matchQuery = new MatchQuery.Builder().field("userId").query(4).build();
        Query query = new Query.Builder().match(matchQuery).build();
        SearchRequest searchRequest = new SearchRequest.Builder().query(query).build();
        SearchResponse<LoginTicket> search = client.search(searchRequest, LoginTicket.class);
        System.out.println(search);
    }

    @Test
    public void testInsertDiscussPost() throws Exception {
        List<List<DiscussPost>> list = new ArrayList<>();
        list.add(discussPostMapper.selectDiscussPosts(101, 0, 100));
        list.add(discussPostMapper.selectDiscussPosts(102, 0, 100));
        list.add(discussPostMapper.selectDiscussPosts(103, 0, 100));
        list.add(discussPostMapper.selectDiscussPosts(111, 0, 100));
        list.add(discussPostMapper.selectDiscussPosts(112, 0, 100));
        list.add(discussPostMapper.selectDiscussPosts(131, 0, 100));
        list.add(discussPostMapper.selectDiscussPosts(132, 0, 100));
        list.add(discussPostMapper.selectDiscussPosts(133, 0, 100));
        list.add(discussPostMapper.selectDiscussPosts(134, 0, 100));

        client.bulk(
                b -> {
                    list.forEach(
                            user -> {
                                user.forEach(
                                        post -> {
                                            b.operations(
                                                    o -> o.create(
                                                            c -> c.index("discusspost")
                                                                    .document(post)
                                                                    .id(String.valueOf(post.getId()))
                                                    )
                                            );
                                        }
                                );
                            }
                    );
                    return b;
                }
        );
    }

    @Test
    public void testSearch() throws Exception {
        SearchResponse<DiscussPost> search = client.search(
                s -> s.index("discusspost")
                        .query(
                                q -> q.bool(
                                        b -> b.should(
                                                should -> should.match(
                                                        m -> m.field("title")
                                                                .query("互联网寒冬")
                                                )
                                        ).should(
                                                should -> should.match(
                                                        m -> m.field("content")
                                                                .query("互联网寒冬")
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
                        .from(0)
                        .size(10)
                , DiscussPost.class);
        search.hits().hits().forEach(e -> System.out.println(e.source()));
    }

    @Test
    public void testHighlight() throws Exception {
        Map<String, HighlightField> map = new HashMap<>();
        map.put("title", new HighlightField.Builder().preTags("<em>").postTags("</em>").build());
        map.put("content", new HighlightField.Builder().preTags("<em>").postTags("</em>").build());

        SearchResponse<DiscussPost> search = client.search(
            s -> s.index("discusspost")
                .query(
                    q -> q.bool(
                        b -> b.should(
                            should -> should.match(
                                m -> m.field("title")
                                    .query("互联网寒冬")
                            )
                        ).should(
                            should -> should.match(
                                m -> m.field("content")
                                    .query("互联网寒冬")
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
                .from(0)
                .size(10)
                .highlight(
                    h -> h.fields(map)
                )
            , DiscussPost.class);

        List<DiscussPost> highlightPost = new ArrayList<>();
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

        highlightPost.forEach(System.out::println);
    }
}
