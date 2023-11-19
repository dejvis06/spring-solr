package com.example.demo.models;

public class TechProductSearchConfig extends TechProduct {

    protected String name;

    public TechProductSearchConfig(String id, String name) {
        super(null, name);
        this.name = name;
    }

    @Override
    public String toString() {
        return "TechProductSearchConfig{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", manu='" + manu + '\'' +
                ", cat='" + cat + '\'' +
                ", price=" + price +
                '}';
    }
}
