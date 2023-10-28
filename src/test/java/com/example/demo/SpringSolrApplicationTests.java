package com.example.demo;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.UUID;

@SpringBootTest
class SpringSolrApplicationTests {

	final String solrUrl = "http://localhost:8983/solr";

	@Test
    void indexBySolrInputDocument() throws SolrServerException, IOException {
		final SolrClient client = getSolrClient();
		final SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", UUID.randomUUID().toString());
		doc.addField("name", "Amazon Kindle Paperwhite");

		final UpdateResponse updateResponse = client.add("solr_core", doc);
		// Indexed documents must be committed
		client.commit("solr_core");
    }

	@Test
	void indexByBean() throws SolrServerException, IOException {
		final SolrClient client = getSolrClient();
		final TechProduct kindle = new TechProduct("kindle-id-4", "Amazon Kindle Paperwhite");
		final UpdateResponse response = client.addBean("solr_core", kindle);

		// Indexed documents must be committed
		client.commit("solr_core");
	}

	private HttpSolrClient getSolrClient() {
		return new HttpSolrClient.Builder(solrUrl)
				.withConnectionTimeout(10000)
				.withSocketTimeout(60000)
				.build();
	}

	private static class TechProduct {
		@Field public String id;
		@Field public String name;

		public TechProduct(String id, String name) {
			this.id = id;  this.name = name;
		}

		public TechProduct() {}
	}
}
