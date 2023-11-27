package com.example.demo.query;

import com.example.demo.query.annotations.Query;
import com.example.demo.query.decorators.components.PageRequest;
import com.example.demo.query.decorators.components.QQuery;
import org.apache.solr.client.solrj.SolrQuery;

public final class QueryParser {

    public static SolrQuery parse(Query query, SolrQueryBuilder... params) {
        // TODO: handle args & other parts of the query
        SolrQueryBuilder solrQueryBuilder = new QQuery(query.q());
        solrQueryBuilder = new PageRequest(solrQueryBuilder, query.sort(), query.page());
        return solrQueryBuilder.build();
    }
}
