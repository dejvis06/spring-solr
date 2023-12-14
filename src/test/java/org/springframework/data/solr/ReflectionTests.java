package org.springframework.data.solr;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.solr.example.models.TechProduct;
import org.springframework.data.solr.example.repository.TechProductRepository;

import java.io.IOException;
import java.util.UUID;

@SpringBootTest
public class ReflectionTests {

    @Autowired
    TechProductRepository techProductRepository;

    @Test
    void test() throws SolrServerException, IOException {
        final TechProduct kindle = new TechProduct(UUID.randomUUID(), "Amazon Kindle Paperwhite bean");
        techProductRepository.save(kindle);
    }
}
