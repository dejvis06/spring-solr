package org.springframework.data.solr.configs;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.configs.annotations.SolrRepository;
import org.springframework.data.solr.repository.SolrRepositoryInvocationHandler;
import org.springframework.data.solr.repository.SimpleSolrRepository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Configuration class for integrating Spring with Apache Solr.
 * Manages the setup of SolrClient, scans for Solr repositories annotated with
 * {@code @SolrRepository}, and dynamically creates proxies for these repositories.
 * <p>
 * The class uses the provided configuration properties such as solrUrl, solrCore,
 * and solrBasePackage to configure and initialize the SolrClient, and it dynamically
 * registers Solr repositories during the application's runtime.
 * <p>
 * SolrConfigs leverages Spring's ApplicationRunner to execute repository scanning
 * and registration after the application context has been initialized. It utilizes
 * reflection for discovering classes annotated with {@code @SolrRepository}, and
 * creates dynamic proxies to handle repository invocations.
 * <p>
 * Note: This class assumes the existence of Solr repositories annotated with
 * {@code @SolrRepository} and interfaces extending Spring Data Solr's repository base
 * classes.
 *
 * @see SolrClient
 * @see Http2SolrClient
 * @see ApplicationRunner
 * @see SimpleSolrRepository
 * @see SolrRepositoryInvocationHandler
 * @see Reflections
 * @see Value
 * @see Bean
 * @see Configuration
 * @see SolrRepository
 */
@Configuration
class SolrConfigs {

    private static final Logger logger = LoggerFactory.getLogger(SolrConfigs.class);

    @Value("${solr.url}")
    private String solrUrl;
    @Value("${solr.core}")
    private String solrCore;
    @Value("${solr.base.package}")
    private String solrBasePackage;

    @Bean
    SolrClient solrClient() {
        logger.info("Initializing SolrClient with URL: {}", solrUrl);
        return new Http2SolrClient.Builder(solrUrl)
                .build();
    }

    @Bean
    ApplicationRunner solrRepositoriesScanner(ConfigurableBeanFactory beanFactory) {
        return (args) -> {
            logger.info("Scanning for Solr repositories and registering singletons...");
            findSolrRepositories().forEach(solrRepository -> {
                logger.info("Registering Solr repository: {}", solrRepository.getSimpleName());
                beanFactory.registerSingleton(solrRepository.getSimpleName(), generateProxy(solrRepository));
            });
        };
    }

    private Object generateProxy(Class<?> clazz) {
        logger.debug("Generating proxy for Solr repository: {}", clazz.getSimpleName());
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{(clazz)},
                new SolrRepositoryInvocationHandler(parameterizedSolrRepository(clazz), solrClient(), solrCore));
    }

    private SimpleSolrRepository<?, ?> parameterizedSolrRepository(Class<?> clazz) {
        logger.debug("Instantiating SimpleSolrRepository by parameterized type for class: {}", clazz.getSimpleName());
        return SimpleSolrRepository.instantiateByParameterizedType(getParameterizedType(clazz), solrClient(), solrCore);
    }

    private Set<Class<?>> findSolrRepositories() {
        logger.info("Scanning for classes annotated with @SolrRepository in package: {}", solrBasePackage);
        Set<Class<?>> solrRepositories = new Reflections(solrBasePackage).getTypesAnnotatedWith(SolrRepository.class);
        logger.info("Found {} Solr repositories.", solrRepositories.size());
        return solrRepositories;
    }

    private static Type getParameterizedType(Class<?> target) {
        Type[] types = target.getGenericInterfaces();
        if (types.length > 0) {
            Type parameterizedType = ((ParameterizedType) types[0]).getActualTypeArguments()[0];
            logger.debug("Parameterized type for SolrRepository: {}", parameterizedType.getTypeName());
            return parameterizedType;
        }
        throw new RuntimeException("SolrRepository Interface not implemented");
    }
}
