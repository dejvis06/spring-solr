package com.example.demo.query.static_.mapping;

import com.example.demo.query.static_.mapping.handlers.QueryHandler;

import java.lang.reflect.Method;

public abstract class SolrQueryMetadata {

    private Method method;
    private QueryHandler queryHandler;

    public SolrQueryMetadata(Method method) {
        this.method = method;
    }


}
