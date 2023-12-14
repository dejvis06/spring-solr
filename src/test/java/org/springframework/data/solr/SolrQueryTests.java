package org.springframework.data.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.solr.query.Operators;
import org.springframework.data.solr.query.QueryParser;
import org.springframework.data.solr.query.SolrQueryBuilder;
import org.springframework.data.solr.query.decorators.components.*;

import java.io.IOException;

@SpringBootTest
public class SolrQueryTests {

    @Autowired
    SolrClient solrClient;
    @Value("${solr.core}")
    private String solrCore;

    @Test
    void defaultQueryTest() throws SolrServerException, IOException, QueryParser.SolrQueryException {

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

        QueryResponse response = solrClient.query(solrCore, solrQuery);
        response.getResults().forEach(result -> System.out.println(result.toString()));
    }

    @Test
    void facetTest() throws SolrServerException, IOException {

        SolrQueryBuilder solrQueryBuilder = new QQuery("*:*");
        solrQueryBuilder = new FacetField(solrQueryBuilder)
                .fields("name_ss");

        SolrQuery solrQuery = solrQueryBuilder.build();

        QueryResponse response = solrClient.query(solrCore, solrQuery);
        response.getFacetFields().forEach(result -> System.out.println(result.toString()));
    }

}
