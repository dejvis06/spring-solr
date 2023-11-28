package com.example.demo.repositories;

import com.example.demo.configs.annotations.SolrRepository;
import com.example.demo.models.TechProduct;
import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.annotations.FieldList;
import com.example.demo.query.annotations.Page;
import com.example.demo.query.annotations.Query;
import com.example.demo.query.annotations.Sort;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.UUID;

@SolrRepository
public interface TechProductRepository extends ISolrRepository<TechProduct, UUID> {

    @Query(
            sort = @Sort(field = "id", order = SolrQuery.ORDER.desc),
            page = @Page(rows = 5),
            fl = @FieldList(selected = {"name_ss"}))
    QueryResponse findAll(SolrQueryBuilder... solrQueryBuilder);
}
