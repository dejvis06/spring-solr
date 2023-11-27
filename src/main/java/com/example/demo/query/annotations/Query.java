package com.example.demo.query.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Query {

    String q();
    Sort sort();
    Page page();
}
