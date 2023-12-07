package com.example.demo.repositories;

import com.example.demo.configs.annotations.SolrRepository;
import com.example.demo.models.SolrResponseRest;
import com.example.demo.models.TechProduct;
import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.annotations.*;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

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
