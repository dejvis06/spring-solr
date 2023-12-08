package org.springframework.data.solr.repositories.impl;

import org.springframework.data.solr.models.SolrResponseRest;
import org.springframework.data.solr.models.TechProduct;
import org.springframework.data.solr.query.SolrQueryBuilder;
import org.springframework.data.solr.query.decorators.components.QQuery;
import org.springframework.data.solr.repositories.ISolrRepository;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
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
    public SolrResponseRest<T> findAll(SolrQueryBuilder... params) throws SolrServerException, IOException {
        SolrQueryBuilder solrQueryBuilder = new QQuery("*:*");
        for (SolrQueryBuilder param : emptyIfNull(params)) {
            solrQueryBuilder = param;
        }
        QueryResponse solrResponse = solrClient.query("solr_core", solrQueryBuilder.build());
        return (SolrResponseRest<T>) SolrResponseRest.instantiate(this.getClass())
                .facets(solrResponse.getFacetFields())
                .results(solrResponse.getResults());
    }

    private static SolrQueryBuilder[] emptyIfNull(SolrQueryBuilder... params) {
        if (params == null) {
            return new SolrQueryBuilder[]{};
        }
        return params;
    }

    public static SimpleSolrRepository instantiate(Type parameterizedType, SolrClient solrClient) {
        if (parameterizedType.equals(TechProduct.class)) {
            return new SimpleSolrRepository<TechProduct, UUID>(solrClient);
        } else {
            return null;
        }
    }
}
