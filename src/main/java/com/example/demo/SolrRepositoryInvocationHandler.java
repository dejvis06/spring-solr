package com.example.demo;

import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.annotations.Query;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.example.demo.query.QueryParser.parse;

public class SolrRepositoryInvocationHandler implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(SolrRepositoryInvocationHandler.class);
    private final static String INSTANCE_EXCEPTION = "object is not an instance of declaring class";
    public static final String NO_SOLR_QUERY_FOUND = "No Solr Query found";

    private Object target;

    public SolrRepositoryInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException e) {
            if (e.getMessage().equals(INSTANCE_EXCEPTION)) {
                log.info("Object instance error, proceeding with the custom query");
            } else {
                throw e;
            }
        }
        Query query = method.getAnnotation(Query.class);
        if (query == null)
            throw new IllegalArgumentException(NO_SOLR_QUERY_FOUND);

        // TODO: handle args
        log.info("Parsing query: {}", query);
        SolrQuery solrQuery = parse(query, (SolrQueryBuilder) args[0]);

        // TODO: handle solrQuery execution
        return result;
    }
}
