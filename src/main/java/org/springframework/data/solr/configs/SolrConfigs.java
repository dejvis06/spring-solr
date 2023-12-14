package org.springframework.data.solr.configs;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.reflections.Reflections;
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

@Configuration
class SolrConfigs {

    @Value("${solr.url}")
    private String solrUrl;
    @Value("${solr.core}")
    private String solrCore;
    @Value("${solr.base.package}")
    private String solrBasePackage;

    @Bean
    SolrClient solrClient() {
        return new Http2SolrClient.Builder(solrUrl)
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
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{(clazz)},
                new SolrRepositoryInvocationHandler(parameterizedSolrRepository(clazz), solrClient(), solrCore));
    }

    private SimpleSolrRepository parameterizedSolrRepository(Class<?> clazz) {
        return SimpleSolrRepository.instantiateByParameterizedType(getParameterizedType(clazz), solrClient(), solrCore);
    }

    private Set<Class<?>> findSolrRepositories() {
        return new Reflections(solrBasePackage).getTypesAnnotatedWith(SolrRepository.class);
    }


    private static Type getParameterizedType(Class<?> target) {
        Type[] types = target.getGenericInterfaces();
        if (types.length > 0) {
            return ((ParameterizedType) types[0]).getActualTypeArguments()[0];
        }
        throw new RuntimeException("SolrRepository Interface not implemented");
    }
}
