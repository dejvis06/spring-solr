package com.example.demo.query;

import com.example.demo.query.annotations.Query;
import com.example.demo.query.decorators.BasicDecorations;
import com.example.demo.query.decorators.SolrQueryDecorator;
import com.example.demo.query.decorators.components.FacetField;
import com.example.demo.query.decorators.components.FieldList;
import com.example.demo.query.decorators.components.PageRequest;
import com.example.demo.query.decorators.components.QQuery;
import org.apache.solr.client.solrj.SolrQuery;

public final class QueryParser {

    public static SolrQuery parse(Query query, SolrQueryBuilder[] params) throws SolrQueryException {
        SolrQueryBuilder solrQueryBuilder = new BasicDecorations(new QQuery(query.q()))
                .add(new PageRequest()
                        .sort(query.sort().field(), query.sort().order())
                        .page(query.page().start(), query.page().rows()))
                .add(new FieldList(query.fl().selected()))
                .add(new FacetField().fields(query.facet().facetField().selected()))
                .get();

        for (SolrQueryBuilder param : emptyIfNull(params)) {
            if (param != null) {
                solrQueryBuilder = ((SolrQueryDecorator) param).setSolrQueryBuilder(solrQueryBuilder);
            }
        }
        return solrQueryBuilder.build();
    }

    private static SolrQueryBuilder[] emptyIfNull(SolrQueryBuilder[] params) {
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
