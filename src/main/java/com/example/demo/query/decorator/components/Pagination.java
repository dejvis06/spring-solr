package com.example.demo.query.decorator.components;

import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.decorator.SolrQueryDecorator;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pagination extends SolrQueryDecorator {

    static final Logger logger = LoggerFactory.getLogger(Pagination.class);

    private Integer start, rows;

    public Pagination(SolrQueryBuilder solrQueryBuilder, Integer start, Integer rows) {
        this.solrQueryBuilder = solrQueryBuilder;
        this.start = start;
        this.rows = rows;
    }

    @Override
    public SolrQuery build() {
        logger.info("Setting start: {} & rows: {}", start, rows);
        return this.solrQueryBuilder.build().setStart(start).setRows(rows);
    }
}
