package org.springframework.data.solr.example.models;

import org.apache.solr.client.solrj.beans.Field;

import java.util.UUID;

public class TechProduct {
    @Field
    public String id;
    @Field
    protected String name;
    @Field
    public String manu;
    @Field
    public String cat;
    @Field
    public Float price;

    public TechProduct(UUID id, String name) {
        this.id = id.toString();
        this.name = name;
    }
}
