package com.example.demo.query.decorator;

import com.example.demo.query.SolrQueryBuilder;

public abstract class SolrQueryDecorator implements SolrQueryBuilder {

    protected SolrQueryBuilder solrQueryBuilder;
}
