package com.example.demo;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolrConfiguration {

    static final String SOLR_URL = "http://localhost:8983/solr";

    @Bean
    SolrClient solrClient() {
        return new Http2SolrClient.Builder(SOLR_URL)
                .build();
    }
}
