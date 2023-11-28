package com.example.demo.query;

import com.example.demo.query.annotations.Query;
import com.example.demo.query.decorators.components.PageRequest;
import com.example.demo.query.decorators.components.QQuery;
import org.apache.solr.client.solrj.SolrQuery;

public final class QueryParser {

    public static SolrQuery parse(Query query, SolrQueryBuilder[] params) throws SolrQueryException {
        SolrQueryBuilder solrQueryBuilder = new QQuery(query.q());
        solrQueryBuilder = new PageRequest(solrQueryBuilder, query.sort(), query.page());

        for (SolrQueryBuilder param : handleNullParams(params)) {
            if (param != null) {
                solrQueryBuilder = param;
            }
        }
        return solrQueryBuilder.build();
    }

    private static SolrQueryBuilder[] handleNullParams(SolrQueryBuilder[] params) {
        if (params == null) {
            return new SolrQueryBuilder[]{};
        }
        return params;
    }

    public static class SolrQueryException extends Exception {
        public SolrQueryException(String message) {
            super(message);
        }
    }
}
