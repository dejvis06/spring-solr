package com.example.demo.configs;

import com.example.demo.MyInvocationHandler;
import com.example.demo.configs.annotations.SolrRepository;
import com.example.demo.repositories.SimpleSolrRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

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
            emptyIfNull(findSolrRepositories(BASE_PACKAGE)).forEach(solrRepository -> {
                beanFactory.registerSingleton(solrRepository.getSimpleName(), generateProxy(solrRepository));
            });
        };
    }

    private static List<Class> emptyIfNull(List<Class> solrRepositories) {
        if (solrRepositories != null)
            return solrRepositories;
        return new ArrayList<>();
    }

    private Object generateProxy(Class clazz) {
        return Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{(clazz)},
                new MyInvocationHandler(new SimpleSolrRepository<>()));
    }

    private List<Class> findSolrRepositories(String basePackage) throws IOException, ClassNotFoundException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        List<Class> solrRepositories = new ArrayList<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolveBasePackage(basePackage) + "/" + "**/*.class";
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                if (isSolrRepository(metadataReader)) {
                    solrRepositories.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                }
            }
        }
        return solrRepositories;
    }

    private String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    }

    private boolean isSolrRepository(MetadataReader metadataReader) {
        try {
            Class c = Class.forName(metadataReader.getClassMetadata().getClassName());
            if (c.getAnnotation(SolrRepository.class) != null) {
                return true;
            }
        } catch (Throwable e) {
        }
        return false;
    }
}
