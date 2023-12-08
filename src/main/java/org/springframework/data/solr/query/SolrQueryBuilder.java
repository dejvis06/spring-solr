package org.springframework.data.solr.query;

import org.apache.solr.client.solrj.SolrQuery;

public interface SolrQueryBuilder {

    SolrQuery build();
}
