package com.example.demo.configs;

import com.example.demo.repositories.SolrRepositoryInvocationHandler;
import com.example.demo.configs.annotations.SolrRepository;
import com.example.demo.repositories.impl.SimpleSolrRepository;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.reflections.Reflections;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;
import java.util.Set;

@Configuration
public class SolrConfigs {
    public static final String BASE_PACKAGE = "com.example.demo";
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
                new SolrRepositoryInvocationHandler(new SimpleSolrRepository<>()));
    }

    private Set<Class<?>> findSolrRepositories() {
        return new Reflections(BASE_PACKAGE).getTypesAnnotatedWith(SolrRepository.class);
    }
}
