package org.springframework.data.solr.query;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.data.solr.query.annotations.Query;
import org.springframework.data.solr.query.decorators.SolrQueryDecorator;
import org.springframework.data.solr.query.decorators.components.FacetField;
import org.springframework.data.solr.query.decorators.components.FieldList;
import org.springframework.data.solr.query.decorators.components.PageRequest;
import org.springframework.data.solr.query.decorators.components.QQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for parsing Spring Data Solr Query annotations into SolrQuery objects.
 */
public final class QueryParser {

    // Use a cache to store SolrQueryBuilder instances
    private static final Map<String, SolrQueryBuilder> queryBuilderCache = new HashMap<>();

    /**
     * Parses a Spring Data Solr Query annotation into a SolrQuery object.
     *
     * @param query   The Query annotation to parse.
     * @param params  Additional SolrQueryBuilder parameters to apply.
     * @return        The SolrQuery object representing the parsed query.
     * @throws SolrQueryException If an error occurs during the parsing process.
     */
    public static SolrQuery parse(Query query, SolrQueryBuilder... params) throws SolrQueryException {

        String queryKey = query.toString();
        SolrQueryBuilder cachedQueryBuilder = queryBuilderCache.get(queryKey);
        if (cachedQueryBuilder != null) {
            return cachedQueryBuilder.build();
        }

        SolrQueryBuilder solrQueryBuilder = new QueryBuilderConfigurator(new QQuery(query.q()))
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

        // Cache the SolrQueryBuilder instance
        queryBuilderCache.put(queryKey, solrQueryBuilder);

        return solrQueryBuilder.build();
    }

    private static SolrQueryBuilder[] emptyIfNull(SolrQueryBuilder... params) {
        return (params != null) ? params : new SolrQueryBuilder[]{};
    }

    public static class SolrQueryException extends Exception {
        public SolrQueryException(String message) {
            super(message);
        }
    }

    /**
     * Helper class for configuring and building SolrQueryBuilder instances.
     */
    private static class QueryBuilderConfigurator {

        private SolrQueryBuilder solrQueryBuilder;

        QueryBuilderConfigurator(SolrQueryBuilder queryBuilder) {
            this.solrQueryBuilder = queryBuilder;
        }

        public SolrQueryBuilder get() {
            return solrQueryBuilder;
        }

        private QueryBuilderConfigurator add(SolrQueryDecorator decorator) {
            solrQueryBuilder = decorator.setSolrQueryBuilder(solrQueryBuilder);
            return this;
        }
    }
}
