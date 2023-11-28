package com.example.demo.models;

import org.apache.solr.client.solrj.beans.Field;

import java.util.UUID;

public class TechProduct {
    @Field
    public UUID id;
    @Field
    protected String name;
    @Field
    public String manu;
    @Field
    public String cat;
    @Field
    public Float price;
}
