package org.springframework.data.solr.query.decorators;

import org.springframework.data.solr.query.SolrQueryBuilder;

public abstract class SolrQueryDecorator implements SolrQueryBuilder {

    protected SolrQueryBuilder solrQueryBuilder;

    public SolrQueryDecorator setSolrQueryBuilder(SolrQueryBuilder solrQueryBuilder) {
        this.solrQueryBuilder = solrQueryBuilder;
        return this;
    }
}
