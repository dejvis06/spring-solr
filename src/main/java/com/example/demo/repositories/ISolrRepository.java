package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;

public interface ISolrRepository<T, ID> extends CrudRepository<T, ID> {

}
