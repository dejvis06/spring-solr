package com.example.demo.repositories;

import com.example.demo.models.TechProduct;
import com.example.demo.query.annotations.Query;

import java.util.List;
import java.util.UUID;

@com.example.demo.configs.annotations.SolrRepository
public interface TechProductRepository extends SolrRepository<TechProduct, UUID> {

    @Query(q = "techproduct")
    List<TechProduct> findAll();
}
