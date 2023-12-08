package org.springframework.data.solr;

import org.springframework.data.solr.models.TechProduct;
import org.springframework.data.solr.repositories.TechProductRepository;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ReflectionTests {

    @Autowired
    TechProductRepository techProductRepository;

    @Test
    void test() throws SolrServerException, IOException {
        techProductRepository.findAll(null);
    }
}
