package org.springframework.data.solr.repository;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.data.solr.example.models.SolrResponseRest;
import org.springframework.data.solr.example.models.TechProduct;
import org.springframework.data.solr.query.SolrQueryBuilder;
import org.springframework.data.solr.query.decorators.components.QQuery;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

public class SimpleSolrRepository<T, ID> implements ISolrRepository<T, ID> {

    private final SolrClient solrClient;
    private final String solrCore;

    public SimpleSolrRepository(SolrClient solrClient, String solrCore) {
        this.solrClient = solrClient;
        this.solrCore = solrCore;
    }


    @Override
    public <S extends T> S save(S entity) throws SolrServerException, IOException {
        solrClient.addBean(solrCore, entity);
        solrClient.commit(solrCore);
        // TODO converters SolrDocument -> <T>
        return entity;
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
        solrClient.commit();
    }

    @Override
    public SolrResponseRest<T> findAll(SolrQueryBuilder... params) throws SolrServerException, IOException {
        SolrQueryBuilder solrQueryBuilder = new QQuery("*:*");
        for (SolrQueryBuilder param : emptyIfNull(params)) {
            solrQueryBuilder = param;
        }
        QueryResponse solrResponse = solrClient.query(solrCore, solrQueryBuilder.build());
        return (SolrResponseRest<T>) SolrResponseRest.instantiateByParameterizedType(this.getClass())
                .facets(solrResponse.getFacetFields())
                .results(solrResponse.getResults());
    }

    private static SolrQueryBuilder[] emptyIfNull(SolrQueryBuilder... params) {
        if (params == null) {
            return new SolrQueryBuilder[]{};
        }
        return params;
    }

    public static SimpleSolrRepository instantiateByParameterizedType(Type parameterizedType, SolrClient solrClient, String solrCore) {
        if (parameterizedType.equals(TechProduct.class)) {
            return new SimpleSolrRepository<TechProduct, UUID>(solrClient, solrCore);
        } else {
            return null;
        }
    }
}
