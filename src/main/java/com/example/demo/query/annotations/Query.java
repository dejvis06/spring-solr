package com.example.demo.query.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Query {

    String q() default "*:*";

    Sort sort();

    Page page() default @Page;

    FieldList fl() default @FieldList;
}
