package org.springframework.data.solr.repository;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.solr.example.models.SolrResponseRest;
import org.springframework.data.solr.query.SolrQueryBuilder;
import org.springframework.data.solr.query.annotations.Query;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

import static org.springframework.data.solr.query.QueryParser.parse;

/**
 * Handles method invocations on Solr repository proxies, allowing custom query processing.
 */
public class SolrRepositoryInvocationHandler implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(SolrRepositoryInvocationHandler.class);
    private static final String MISSING_SOLR_QUERY = "No Solr Query found";

    private final Object target;
    private final SolrClient solrClient;

    private final String solrCore;

    public SolrRepositoryInvocationHandler(Object target, SolrClient solrClient, String solrCore) {
        this.target = Objects.requireNonNull(target, "Target must not be null");
        this.solrClient = Objects.requireNonNull(solrClient, "SolrClient must not be null");

        if (!StringUtils.hasText(solrCore)) {
            throw new IllegalArgumentException("SolrCore must not be blank or null");
        }
        this.solrCore = solrCore;

    }

    /**
     * Invokes the Solr repository method, processing custom queries if applicable.
     *
     * <p>This method is responsible for handling method invocations on Solr repository proxies. If the invoked
     * method belongs to the Solr repository interface (e.g., CRUD operations), it delegates the call to the
     * actual repository implementation. If the method includes a {@code @Query} annotation, it parses the
     * query metadata, executes the corresponding Solr query, and returns the result as a structured
     * {@link SolrResponseRest} object.</p>
     *
     * @param proxy  the proxy instance representing the Solr repository
     * @param method the method being invoked, either a standard repository method or a custom query method
     * @param args   the arguments to the method, including SolrQueryBuilder objects for custom queries
     * @return the result of the method invocation, typically a {@link SolrResponseRest} object
     * @throws IllegalArgumentException if no {@code @Query} annotation is found for custom queries
     * @throws Throwable                if an error occurs during method invocation or query execution
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("Query invoked: {}, for Repository: {}", method.getName(), method.getDeclaringClass().getSimpleName());
        if (isInstance(method.getDeclaringClass(), target.getClass())) {
            log.info("Proceeding with default behaviour");
            return method.invoke(target, args);
        }
        Query query = method.getAnnotation(Query.class);
        if (query == null)
            throw new IllegalArgumentException(MISSING_SOLR_QUERY);

        log.info("Parsing query metadata: {}", query);
        try {
            SolrQuery solrQuery = parse(query, args.length > 0 ? (SolrQueryBuilder[]) args[0] : null);

            QueryResponse solrResponse = solrClient.query(solrCore, solrQuery);

            return SolrResponseRest.instantiateByParameterizedType(getParameterizedType(method))
                    .facets(solrResponse.getFacetFields())
                    .results(solrResponse.getResults());
        } catch (Exception e) {
            log.error("Error invoking query:", e);
            return new SolrResponseRest<>(e.getMessage());
        }
    }

    private static Class<?> getParameterizedType(Method method) {
        ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
        Type[] actualTypeArguments = type.getActualTypeArguments();
        if (actualTypeArguments.length == 1)
            return (Class<?>) actualTypeArguments[0];
        else
            throw new RuntimeException("Multiple Type arguments not handled");
    }

    private boolean isInstance(Class<?> declaringClass, Class<?> targetClass) {
        return declaringClass.isAssignableFrom(targetClass);
    }
}
