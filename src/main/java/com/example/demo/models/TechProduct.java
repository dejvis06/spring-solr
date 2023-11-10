package com.example.demo.models;

import org.apache.solr.client.solrj.beans.Field;

public class TechProduct {
    @Field
    public String id;
    @Field
    public String name;
    @Field
    public String manu;
    @Field
    public String cat;
    @Field
    public Float price;

    public TechProduct(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public TechProduct() {
    }
}
