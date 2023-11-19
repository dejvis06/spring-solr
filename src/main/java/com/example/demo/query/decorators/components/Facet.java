package com.example.demo.query.decorators.components;

import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.decorators.SolrQueryDecorator;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: This case can be expanded to handle all the cases of the Facets, for the moment only facet-fields
public class Facet extends SolrQueryDecorator {

    static final Logger logger = LoggerFactory.getLogger(Facet.class);

    private String[] fields;

    public Facet(SolrQueryBuilder solrQueryBuilder, String... fields) {
        this.solrQueryBuilder = solrQueryBuilder;
        this.fields = fields;
    }

    public Facet fields(String... fields) {
        this.fields = fields;
        return this;
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
