package com.example.demo.configs;

import com.example.demo.MyInvocationHandler;
import com.example.demo.configs.annotations.SolrRepository;
import com.example.demo.repositories.SimpleSolrRepository;
import com.example.demo.repositories.TechProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import java.lang.reflect.Proxy;

@Configuration
@ComponentScan(basePackages = "org.example.demo",
        includeFilters = @Filter(type = FilterType.ANNOTATION, value = SolrRepository.class))
public class SolrConfigs {

    @Bean
    TechProductRepository techProductRepository() {
        return (TechProductRepository) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{TechProductRepository.class},
                new MyInvocationHandler(new SimpleSolrRepository<>()));
    }
}
