package com.example.demo.models;

import org.apache.solr.client.solrj.beans.Field;

import java.util.UUID;

public class TechProduct {
    @Field //TODO: change to UUID;
    public UUID id;
    @Field
    protected String name;
    @Field
    public String manu;
    @Field
    public String cat;
    @Field
    public Float price;

    public TechProduct(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public TechProduct() {
    }
}
