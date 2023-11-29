package com.example.demo.query.decorators.components;

import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.decorators.SolrQueryDecorator;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacetField extends SolrQueryDecorator {

    static final Logger logger = LoggerFactory.getLogger(FacetField.class);

    private String[] fields;

    public FacetField(SolrQueryBuilder solrQueryBuilder, String... fields) {
        this.solrQueryBuilder = solrQueryBuilder;
        this.fields = fields;
    }

    public FacetField() {
    }

    public FacetField fields(String... fields) {
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
