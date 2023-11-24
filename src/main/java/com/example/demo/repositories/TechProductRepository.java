package com.example.demo.repositories;

import com.example.demo.configs.annotations.SolrRepository;
import com.example.demo.models.TechProduct;
import com.example.demo.query.annotations.Query;

import java.util.List;
import java.util.UUID;

@SolrRepository
public interface TechProductRepository extends ISolrRepository<TechProduct, UUID> {

    @Query(q = "techproduct")
    List<TechProduct> findAll();
}
