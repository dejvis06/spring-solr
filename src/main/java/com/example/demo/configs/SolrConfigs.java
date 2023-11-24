package com.example.demo.configs;

import com.example.demo.MyInvocationHandler;
import com.example.demo.configs.annotations.SolrRepository;
import com.example.demo.repositories.SimpleSolrRepository;
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

    /*@Bean
    TechProductRepository techProductRepository() {
        return (TechProductRepository) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{TechProductRepository.class},
                new MyInvocationHandler(new SimpleSolrRepository<>()));
    }*/

    @Bean
    ApplicationRunner solrRepositoriesScanner(ConfigurableBeanFactory beanFactory) {
        return (args) -> {
            findSolrRepositories(BASE_PACKAGE).forEach(solrRepository -> {
                beanFactory.registerSingleton(solrRepository.getSimpleName(), generateProxy(solrRepository));
            });
        };
    }

    private Object generateProxy(Class clazz) {
        return Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{(clazz)},
                new MyInvocationHandler(new SimpleSolrRepository<>()));
    }

    private Set<Class<?>> findSolrRepositories(String basePackage) {
        return new Reflections(basePackage).getTypesAnnotatedWith(SolrRepository.class);
    }
}
