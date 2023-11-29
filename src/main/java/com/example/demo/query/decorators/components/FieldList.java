package com.example.demo.query.decorators.components;

import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.decorators.SolrQueryDecorator;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldList extends SolrQueryDecorator {

    static final Logger logger = LoggerFactory.getLogger(FieldList.class);

    private final String[] fields;

    public FieldList(SolrQueryBuilder solrQueryBuilder, String... fields) {
        this.solrQueryBuilder = solrQueryBuilder;
        this.fields = fields;
    }

    public FieldList(String... fields) {
        this.fields = fields;
    }

    @Override
    public SolrQuery build() {
        logger.info("Adding fields: {}", fields);
        SolrQuery solrQuery = solrQueryBuilder.build();
        for (String field : fields) {
            solrQuery = solrQuery.addField(field);
        }
        return solrQuery;
    }
}
