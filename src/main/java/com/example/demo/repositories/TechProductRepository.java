package com.example.demo.repositories;

import com.example.demo.configs.annotations.SolrRepository;
import com.example.demo.models.TechProduct;
import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.annotations.Page;
import com.example.demo.query.annotations.Query;
import com.example.demo.query.annotations.Sort;
import org.apache.solr.client.solrj.SolrQuery;

import java.util.List;
import java.util.UUID;

@SolrRepository
public interface TechProductRepository extends ISolrRepository<TechProduct, UUID> {

    @Query(q = "techproduct",
            sort = @Sort(field = "", order = SolrQuery.ORDER.desc),
            page = @Page(start = 0, rows = 10))
    List<TechProduct> findAll(SolrQueryBuilder solrQueryBuilder);
}
