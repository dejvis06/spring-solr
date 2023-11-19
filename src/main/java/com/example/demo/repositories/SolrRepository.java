package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;

public interface SolrRepository<T, ID> extends CrudRepository<T, ID> {

}
