package com.example.demo;

import com.example.demo.query.Operators;
import com.example.demo.query.QueryParser;
import com.example.demo.query.QueryParser.SolrQueryException;
import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.decorators.components.*;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class SolrQueryTests {

    @Autowired
    SolrClient solrClient;

    @Test
    void defaultQueryTest() throws SolrServerException, IOException, SolrQueryException {

        SolrQueryBuilder solrQueryBuilder = new QQuery("*:*");
        solrQueryBuilder = new PageRequest(solrQueryBuilder)
                .sort("id", SolrQuery.ORDER.desc)
                .page(0, 10);
        solrQueryBuilder = new FieldList(solrQueryBuilder, "id", "name_ss");

        String id_fq = new FilterQuery.FilterQueryBuilder()
                .operator(Operators.IS)
                .field("name_ss")
                .value("Amazon Kindle Paperwhite bean")
                .build();
        solrQueryBuilder = new FilterQuery(solrQueryBuilder, id_fq);

        SolrQuery solrQuery = solrQueryBuilder.build();

        QueryResponse response = solrClient.query("solr_core", solrQuery);
        response.getResults().forEach(result -> System.out.println(result.toString()));
    }

    @Test
    void facetTest() throws SolrServerException, IOException {

        SolrQueryBuilder solrQueryBuilder = new QQuery("*:*");
        solrQueryBuilder = new Facet(solrQueryBuilder)
                .fields("name_ss");

        SolrQuery solrQuery = solrQueryBuilder.build();

        QueryResponse response = solrClient.query("solr_core", solrQuery);
        response.getFacetFields().forEach(result -> System.out.println(result.toString()));
    }

}
