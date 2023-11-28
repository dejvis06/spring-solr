package com.example.demo.query.decorators;

import com.example.demo.query.SolrQueryBuilder;

public abstract class SolrQueryDecorator implements SolrQueryBuilder {

    protected SolrQueryBuilder solrQueryBuilder;

    public SolrQueryDecorator setSolrQueryBuilder(SolrQueryBuilder solrQueryBuilder) {
        this.solrQueryBuilder = solrQueryBuilder;
        return this;
    }
}
