package com.example.demo.repositories.impl;

import com.example.demo.models.SolrResponseRest;
import com.example.demo.models.TechProduct;
import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.repositories.ISolrRepository;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

public class SimpleSolrRepository<T, ID> implements ISolrRepository<T, ID> {

    private final SolrClient solrClient;

    public SimpleSolrRepository(SolrClient solrClient) {
        this.solrClient = solrClient;
    }

    @Override
    public <S extends T> S save(S entity) {
        try {
            solrClient.addBean(entity);
            // TODO converters SolrDocument -> <T>
            return entity;
        } catch (IOException | SolrServerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<T> findById(ID id) throws SolrServerException, IOException {
        SolrDocument solrDocument = solrClient.getById(String.valueOf(id));
        // TODO converters SolrDocument -> <T>
        return Optional.empty();
    }

    @Override
    public void deleteById(ID id) throws SolrServerException, IOException {
        solrClient.deleteById(String.valueOf(id));
    }

    @Override
    public SolrResponseRest<T> findAll(SolrQueryBuilder... solrQueryBuilder) {
        return null;
    }

    public static SimpleSolrRepository instantiate(Type parameterizedType, SolrClient solrClient) {
        if (parameterizedType.equals(TechProduct.class)) {
            return new SimpleSolrRepository<TechProduct, UUID>(solrClient);
        } else {
            return null;
        }
    }
}
