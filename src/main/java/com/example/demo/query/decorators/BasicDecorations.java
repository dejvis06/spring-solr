package com.example.demo.query.decorators;

import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.decorators.components.QQuery;

public final class BasicDecorations {

    private SolrQueryBuilder solrQueryBuilder;

    public BasicDecorations(QQuery q) {
        solrQueryBuilder = q;
    }

    public BasicDecorations add(SolrQueryDecorator decorator) {
        solrQueryBuilder = decorator.setSolrQueryBuilder(solrQueryBuilder);
        return this;
    }

    public SolrQueryBuilder get() {
        return solrQueryBuilder;
    }
}
