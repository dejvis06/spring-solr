package org.springframework.data.solr.repository;

import org.springframework.data.solr.example.models.SolrResponseRest;
import org.springframework.data.solr.query.SolrQueryBuilder;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.Optional;

public interface ISolrRepository<T, ID> {

    <S extends T> S save(S entity) throws SolrServerException, IOException;

    Optional<T> findById(ID id) throws SolrServerException, IOException;

    void deleteById(ID id) throws SolrServerException, IOException;

    SolrResponseRest<T> findAll(SolrQueryBuilder... solrQueryBuilder) throws SolrServerException, IOException;
}
