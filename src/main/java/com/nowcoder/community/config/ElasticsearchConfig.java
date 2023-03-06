package com.nowcoder.community.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
    
    @Value("${spring.elasticsearch.uris}")
    private String host;
    
    @Bean
    public ElasticsearchClient elasticsearchClient(){
        String[] splitHost = host.split(":");
        String hostName = splitHost[1].substring(2);
        int port = Integer.parseInt(splitHost[2]);

        System.out.println(hostName);
        System.out.println(port);

        RestClient client = RestClient.builder(new HttpHost(hostName, port, "http")).build();
        ElasticsearchTransport transport = new RestClientTransport(client, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
}
