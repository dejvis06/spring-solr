package com.example.demo.query.static_;

import com.example.demo.query.SolrQueryBuilder;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSolrQuery implements SolrQueryBuilder {

    static final Logger logger = LoggerFactory.getLogger(DefaultSolrQuery.class);

    private static final String q = "*:*";

    @Override
    public SolrQuery build() {
        logger.info("Adding q: {}", q);
        return new SolrQuery(q);
    }
}
