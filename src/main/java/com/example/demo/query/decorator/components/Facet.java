package com.example.demo.query.decorator.components;

import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.decorator.SolrQueryDecorator;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: This case can be expanded to handle all the cases of the Facets, for the moment only facet-fields
public class Facet extends SolrQueryDecorator {

    static final Logger logger = LoggerFactory.getLogger(Facet.class);

    private final String[] fields;

    public Facet(SolrQueryBuilder solrQueryBuilder, String... fields) {
        this.solrQueryBuilder = solrQueryBuilder;
        this.fields = fields;
    }

    @Override
    public SolrQuery build() {
        logger.info("Adding facet fields: {}", fields);
        SolrQuery solrQuery = solrQueryBuilder.build();
        for (String field : fields) {
            solrQuery = solrQuery.addFacetField(field);
        }
        return solrQuery;
    }
}
