package com.example.demo.query.decorator.components;

import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.decorator.SolrQueryDecorator;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldList extends SolrQueryDecorator {

    static final Logger logger = LoggerFactory.getLogger(FieldList.class);

    private String[] fields;

    public FieldList(SolrQueryBuilder solrQueryBuilder, String... fields) {
        this.solrQueryBuilder = solrQueryBuilder;
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
