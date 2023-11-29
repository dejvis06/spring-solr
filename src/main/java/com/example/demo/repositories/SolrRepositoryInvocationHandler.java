package com.example.demo.repositories;

import com.example.demo.models.SolrResponseRest;
import com.example.demo.query.SolrQueryBuilder;
import com.example.demo.query.annotations.Query;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;

import static com.example.demo.query.QueryParser.parse;

public class SolrRepositoryInvocationHandler implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(SolrRepositoryInvocationHandler.class);
    private final static String INSTANCE_EXCEPTION = "object is not an instance of declaring class";
    public static final String NO_SOLR_QUERY_FOUND = "No Solr Query found";

    private final Object target;
    private final SolrClient solrClient;

    public SolrRepositoryInvocationHandler(Object target, SolrClient solrClient) {
        this.target = target;
        this.solrClient = solrClient;
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
                log.error("Error invoking proxy: {}", e.getMessage());
                throw e;
            }
        }
        Query query = method.getAnnotation(Query.class);
        if (query == null)
            throw new IllegalArgumentException(NO_SOLR_QUERY_FOUND);

        log.info("Parsing query: {}", query);
        try {
            SolrQuery solrQuery = parse(query, args.length > 0 ? (SolrQueryBuilder[]) args[0] : null);

            QueryResponse solrResponse = solrClient.query("solr_core", solrQuery);

            return SolrResponseRest.instantiate(getParameterizedType(method))
                    .facets(solrResponse.getFacetFields())
                    .results(solrResponse.getResults());
        } catch (Exception e) {
            log.error("Error invoking query: {}", e.getMessage());
            return new SolrResponseRest<>();
        }
    }

    private static Class<?> getParameterizedType(Method method) throws ClassNotFoundException {
        ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
        Type[] actualTypeArguments = type.getActualTypeArguments();
        return Class.forName(actualTypeArguments[0].getTypeName());
    }
}
