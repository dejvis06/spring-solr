package com.example.demo.query.decorator.components;

import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.decorator.SolrQueryDecorator;
import com.example.demo.query.operations.Operators;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterQuery extends SolrQueryDecorator {

    static final Logger logger = LoggerFactory.getLogger(FieldList.class);

    private final String[] fq;

    public FilterQuery(SolrQueryBuilder solrQueryBuilder, String... fq) {
        this.solrQueryBuilder = solrQueryBuilder;
        this.fq = fq;
    }

    @Override
    public SolrQuery build() {
        logger.info("Adding filter queries: {}", fq);
        SolrQuery solrQuery = solrQueryBuilder.build();
        for (String field : fq) {
            solrQuery = solrQuery.addFilterQuery(field);
        }
        return solrQuery;
    }

    public static class FilterQueryBuilder {
        private String operator;
        private String field;
        private String value;
        private String from;
        private String to;

        public FilterQueryBuilder operator(Operators operator) {
            this.operator = operator.value;
            return this;
        }

        public FilterQueryBuilder field(String field) {
            this.field = field;
            return this;
        }

        public FilterQueryBuilder value(String value) {
            this.value = value;
            return this;
        }

        public FilterQueryBuilder from(String from) {
            this.from = from;
            return this;
        }

        public FilterQueryBuilder to(String to) {
            this.to = to;
            return this;
        }

        public String build() {
            String fq = operator;
            if (this.field != null) {
                fq = fq.replace("{field}", field);
            }
            if (this.value != null) {
                fq = fq.replace("{value}", value);
            }
            if (this.from != null) {
                fq = fq.replace("{from}", from);
            }
            if (this.to != null) {
                fq = fq.replace("{to}", to);
            }
            return fq;
        }
    }
}
