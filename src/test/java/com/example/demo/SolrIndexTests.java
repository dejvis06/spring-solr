package com.example.demo;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.UUID;

@SpringBootTest
class SolrIndexTests {

    @Autowired
    SolrClient solrClient;

    @Test
    void indexBySolrInputDocument() throws SolrServerException, IOException {
        final SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", UUID.randomUUID().toString());
        doc.addField("name", "Amazon Kindle Paperwhite");
        doc.addField("name_ss", "Amazon Kindle Paperwhite"); // index entire string
        solrClient.add("solr_core", doc);

        // Indexed documents must be committed
        solrClient.commit("solr_core");
    }

    @Test
    void indexByBean() throws SolrServerException, IOException {
        final TechProduct kindle = new TechProduct("kindle-id-4", "Amazon Kindle Paperwhite bean");
        solrClient.addBean("solr_core", kindle);

        // Indexed documents must be committed
        solrClient.commit("solr_core");
    }

    private static class TechProduct {
        @Field
        public String id;
        @Field(value = "name_ss") // index entire string
        public String name;
        @Field
        public String manu;
        @Field
        public String cat;
        @Field
        public Float price;

        public TechProduct(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public TechProduct() {
        }
    }
}
