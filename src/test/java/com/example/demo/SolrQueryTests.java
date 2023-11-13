package com.example.demo;

import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.decorator.components.Facet;
import com.example.demo.query.decorator.components.FieldList;
import com.example.demo.query.decorator.components.FilterQuery;
import com.example.demo.query.decorator.components.PageRequest;
import com.example.demo.query.operations.Operators;
import com.example.demo.query.static_.DefaultSolrQuery;
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
    void defaultQueryTest() throws SolrServerException, IOException {

        SolrQueryBuilder solrQueryBuilder = new DefaultSolrQuery();
        solrQueryBuilder = new PageRequest(solrQueryBuilder)
                .sort("id", SolrQuery.ORDER.desc)
                .page(0, 10);
        solrQueryBuilder = new FieldList(solrQueryBuilder, "id", "name");

        String id_fq = new FilterQuery.FilterQueryBuilder()
                .operator(Operators.IS)
                .field("id")
                .value("96474e52-37c4-4a1a-9734-fcc290620ef8")
                .build();
        solrQueryBuilder = new FilterQuery(solrQueryBuilder,  id_fq);

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
}
