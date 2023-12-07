package com.example.demo;

import com.example.demo.models.SolrResponseRest;
import com.example.demo.models.TechProduct;
import com.example.demo.repositories.TechProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReflectionTests {

    @Autowired
    TechProductRepository techProductRepository;

    @Test
    void test() {
        SolrResponseRest<TechProduct> response = techProductRepository.findAll(null);
    }
}
