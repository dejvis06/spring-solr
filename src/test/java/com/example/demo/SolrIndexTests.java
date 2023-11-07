package com.example.demo;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.UUID;

@SpringBootTest
class SolrIndexTests {

    static final String SOLR_URL = "http://localhost:8983/solr";

    @Test
    void indexBySolrInputDocument() throws SolrServerException, IOException {
        final SolrClient client = getSolrClient();
        final SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", UUID.randomUUID().toString());
        doc.addField("name", "Amazon Kindle Paperwhite");

        client.add("solr_core", doc);

        // Indexed documents must be committed
        client.commit("solr_core");
    }

    @Test
    void indexByBean() throws SolrServerException, IOException {
        final SolrClient client = getSolrClient();
        final TechProduct kindle = new TechProduct("kindle-id-4", "Amazon Kindle Paperwhite");
        client.addBean("solr_core", kindle);

        // Indexed documents must be committed
        client.commit("solr_core");
    }

    private SolrClient getSolrClient() {
        return new Http2SolrClient.Builder(SOLR_URL)
                .build();
    }

    private static class TechProduct {
        @Field
        public String id;
        @Field
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
