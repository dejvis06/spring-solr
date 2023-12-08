package org.springframework.data.solr.query;

import org.springframework.data.solr.query.annotations.Query;
import org.springframework.data.solr.query.decorators.SolrQueryDecorator;
import org.springframework.data.solr.query.decorators.components.FacetField;
import org.springframework.data.solr.query.decorators.components.FieldList;
import org.springframework.data.solr.query.decorators.components.PageRequest;
import org.springframework.data.solr.query.decorators.components.QQuery;
import org.apache.solr.client.solrj.SolrQuery;

public final class QueryParser {

    public static SolrQuery parse(Query query, SolrQueryBuilder... params) throws SolrQueryException {
        SolrQueryBuilder solrQueryBuilder = new SearchConfigs(new QQuery(query.q()))
                .add(new PageRequest()
                        .sort(query.sort().field(), query.sort().order())
                        .page(query.page().start(), query.page().rows()))
                .add(new FieldList(query.fl().selected()))
                .add(new FacetField()
                        .fields(query.facet().facetField().selected()))
                .get();

        for (SolrQueryBuilder param : emptyIfNull(params)) {
            if (param != null) {
                solrQueryBuilder = ((SolrQueryDecorator) param).setSolrQueryBuilder(solrQueryBuilder);
            }
        }
        return solrQueryBuilder.build();
    }

    private static SolrQueryBuilder[] emptyIfNull(SolrQueryBuilder... params) {
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

    private static class SearchConfigs {

        private SolrQueryBuilder solrQueryBuilder;

        SearchConfigs(QQuery query) {
            solrQueryBuilder = query;
        }

        public SolrQueryBuilder get() {
            return solrQueryBuilder;
        }

        private SearchConfigs add(SolrQueryDecorator decorator) {
            solrQueryBuilder = decorator.setSolrQueryBuilder(solrQueryBuilder);
            return this;
        }
    }
}
