package org.springframework.data.solr.configs;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
class SimpleSolrRepositoryAspect {

    @Pointcut("within(com.example.demo.repositories..*)")
    public void inDataAccessLayer() {}

    // @After(value = "execution(* com.example.demo.repositories.impl.SimpleSolrRepository.*(..))")
    @After("execution(* com.example.demo.repositories.*.*(..))")
    public void commit() {
        System.err.println("Entered commit!");
    }
}
