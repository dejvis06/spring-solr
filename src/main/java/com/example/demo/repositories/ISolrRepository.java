package com.example.demo.repositories;

import com.example.demo.models.SolrResponseRest;
import com.example.demo.query.SolrQueryBuilder;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.Optional;

public interface ISolrRepository<T, ID> {

    <S extends T> S save(S entity);

    Optional<T> findById(ID id) throws SolrServerException, IOException;

    void deleteById(ID id) throws SolrServerException, IOException;

    SolrResponseRest<T> findAll(SolrQueryBuilder... solrQueryBuilder);
}
