package org.springframework.data.solr.example.repository;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.data.solr.configs.annotations.SolrRepository;
import org.springframework.data.solr.example.models.SolrResponseRest;
import org.springframework.data.solr.example.models.TechProduct;
import org.springframework.data.solr.query.SolrQueryBuilder;
import org.springframework.data.solr.query.annotations.*;
import org.springframework.data.solr.repository.ISolrRepository;

import java.io.IOException;
import java.util.UUID;

@SolrRepository
public interface TechProductRepository extends ISolrRepository<TechProduct, UUID> {

    @Query(
            fl = @FieldList(selected = {"name", "id"}),
            sort = @Sort(field = "id", order = SolrQuery.ORDER.desc),
            page = @Page(rows = 5),
            facet = @Facet(
                    facetField = @FacetField(selected = "name")
            )
    )
    SolrResponseRest<TechProduct> findAll(SolrQueryBuilder... solrQueryBuilder) throws SolrServerException, IOException;
}
