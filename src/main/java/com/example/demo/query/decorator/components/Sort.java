package com.example.demo.query.decorator.components;

import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.decorator.SolrQueryDecorator;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sort extends SolrQueryDecorator {

    static final Logger logger = LoggerFactory.getLogger(Sort.class);

    private final String item;
    private final ORDER order;

    public Sort(SolrQueryBuilder solrQueryBuilder, String item, ORDER order) {
        this.solrQueryBuilder = solrQueryBuilder;
        this.item = item;
        this.order = order;
    }

    @Override
    public SolrQuery build() {
        logger.info("Adding sort: {}", this);
        return solrQueryBuilder.build().addSort(new SolrQuery.SortClause(item, order));
    }

    @Override
    public String toString() {
        return "Sort{" +
                "item='" + item + '\'' +
                ", order=" + order +
                '}';
    }
}
