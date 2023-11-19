package com.example.demo.query.static_.mapping.handlers;

public abstract class QueryHandler {

    private QueryHandler next;

    public QueryHandler(QueryHandler next) {
        this.next = next;
    }

    public abstract void handle();
}
