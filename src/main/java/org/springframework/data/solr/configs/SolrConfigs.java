package org.springframework.data.solr.configs;

import org.springframework.data.solr.configs.annotations.SolrRepository;
import org.springframework.data.solr.repositories.SolrRepositoryInvocationHandler;
import org.springframework.data.solr.repositories.impl.SimpleSolrRepository;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.reflections.Reflections;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Set;

@Configuration
class SolrConfigs {
    public static final String BASE_PACKAGE = "org.springframework.data.solr.repositories";
    static final String SOLR_URL = "http://localhost:8983/solr";

    /*@Bean
    TechProductRepository techProductRepository() {
        return (TechProductRepository) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{TechProductRepository.class},
                new MyInvocationHandler(new SimpleSolrRepository<>()));
    }*/

    @Bean
    SolrClient solrClient() {
        return new Http2SolrClient.Builder(SOLR_URL)
                .build();
    }

    @Bean
    ApplicationRunner solrRepositoriesScanner(ConfigurableBeanFactory beanFactory) {
        return (args) -> {
            findSolrRepositories().forEach(solrRepository -> {
                beanFactory.registerSingleton(solrRepository.getSimpleName(), generateProxy(solrRepository));
            });
        };
    }

    private Object generateProxy(Class<?> clazz) {
        return Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{(clazz)},
                new SolrRepositoryInvocationHandler(SimpleSolrRepository.instantiate(getParameterizedType(clazz), solrClient()), solrClient()));
    }

    private Set<Class<?>> findSolrRepositories() {
        return new Reflections(BASE_PACKAGE).getTypesAnnotatedWith(SolrRepository.class);
    }


    private static Type getParameterizedType(Class<?> target) {
        Type[] types = target.getGenericInterfaces();
        if (types.length > 0) {
            return ((ParameterizedType) types[0]).getActualTypeArguments()[0];
        }
        throw new RuntimeException("SolrRepository Interface not implemented");
    }
}
