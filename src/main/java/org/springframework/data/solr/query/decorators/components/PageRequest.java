package org.springframework.data.solr.query.decorators.components;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.solr.query.QueryParser;
import org.springframework.data.solr.query.SolrQueryBuilder;
import org.springframework.data.solr.query.annotations.Page;
import org.springframework.data.solr.query.annotations.Sort;
import org.springframework.data.solr.query.decorators.SolrQueryDecorator;

public class PageRequest extends SolrQueryDecorator {

    static final Logger logger = LoggerFactory.getLogger(PageRequest.class);

    private String field;
    private ORDER order;
    private Integer start, rows;

    public PageRequest() {
    }

    public PageRequest(SolrQueryBuilder solrQueryBuilder) {
        this.solrQueryBuilder = solrQueryBuilder;
    }

    public PageRequest(SolrQueryBuilder solrQueryBuilder, Sort sort, Page page) throws QueryParser.SolrQueryException {
        this.solrQueryBuilder = solrQueryBuilder;
        this.sort(sort.field(), sort.order());
        this.start = page.start();
        this.rows = page.rows();
    }

    public PageRequest sort(String field, ORDER order) throws QueryParser.SolrQueryException {
        if (field.isEmpty()) {
            throw new QueryParser.SolrQueryException("Sort field cannot be empty");
        }
        this.field = field;
        this.order = order;
        return this;
    }

    public PageRequest page(Integer start, Integer rows) {
        this.start = start;
        this.rows = rows;
        return this;
    }

    @Override
    public SolrQuery build() {
        logger.info("Adding sort and page {}", this);
        return solrQueryBuilder.build()
                .addSort(new SolrQuery.SortClause(field, order))
                .setStart(start)
                .setRows(rows);
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "field='" + field + '\'' +
                ", order=" + order +
                ", start=" + start +
                ", rows=" + rows +
                '}';
    }
}
