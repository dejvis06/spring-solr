package com.example.demo.query.annotations;

import org.apache.solr.client.solrj.SolrQuery;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Sort {

    String field();
    SolrQuery.ORDER order();
}
