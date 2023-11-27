package com.example.demo.query.decorators.components;

import com.example.demo.query.SolrQueryBuilder;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QQuery implements SolrQueryBuilder {

    static final Logger logger = LoggerFactory.getLogger(QQuery.class);

    private final String q;

    public QQuery(String q){
        this.q = q;
    }

    @Override
    public SolrQuery build() {
        logger.info("Adding q: {}", q);
        return new SolrQuery(q);
    }
}
