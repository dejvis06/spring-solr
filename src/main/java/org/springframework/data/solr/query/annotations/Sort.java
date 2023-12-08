package org.springframework.data.solr.query.annotations;

import org.apache.solr.client.solrj.SolrQuery;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Sort {

    String field();
    SolrQuery.ORDER order() default SolrQuery.ORDER.desc;
}
