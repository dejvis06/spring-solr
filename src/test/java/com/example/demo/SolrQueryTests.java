package com.example.demo;

import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.decorator.components.Facet;
import com.example.demo.query.decorator.components.FieldList;
import com.example.demo.query.decorator.components.Pagination;
import com.example.demo.query.decorator.components.Sort;
import com.example.demo.query.queries.DefaultSolrQuery;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class SolrQueryTests {

    static final SolrClient solrClient = getSolrClient();
    static final String SOLR_URL = "http://localhost:8983/solr";

    @Test
    void defaultQueryTest() throws SolrServerException, IOException {

        SolrQueryBuilder solrQueryBuilder = new DefaultSolrQuery();
        solrQueryBuilder = new Sort(solrQueryBuilder, "id", SolrQuery.ORDER.desc);
        solrQueryBuilder = new Pagination(solrQueryBuilder, 0, 10);
        solrQueryBuilder = new FieldList(solrQueryBuilder, "name");

        SolrQuery solrQuery = solrQueryBuilder.build();

        QueryResponse response = solrClient.query("solr_core", solrQuery);
        response.getResults().forEach(result -> System.out.println(result.toString()));
    }

    @Test
    void facetTest() throws SolrServerException, IOException {

        SolrQueryBuilder solrQueryBuilder = new DefaultSolrQuery();
        solrQueryBuilder = new Facet(solrQueryBuilder, "name_ss");

        SolrQuery solrQuery = solrQueryBuilder.build();

        QueryResponse response = solrClient.query("solr_core", solrQuery);
        response.getFacetFields().forEach(result -> System.out.println(result.toString()));
    }

    private static SolrClient getSolrClient() {
        return new Http2SolrClient.Builder(SOLR_URL)
                .build();
    }
}
