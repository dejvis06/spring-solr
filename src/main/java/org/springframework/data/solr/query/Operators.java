package org.springframework.data.solr.query;

public enum Operators {
    AND("AND"),
    OR("OR"),
    IS("{field}:{value}"),
    NOT("-{field}:{value}"),
    IS_NULL("-{field}:[* TO *]"),
    IS_NOT_NULL("{field}:[* TO *]"),
    BETWEEN("{field}:[{from} TO {to}]"),
    LESS_THAN("{field}:[* TO {to}}"),
    LESS_THAN_EQUAL("{field}:[* TO {to}]"),
    GREATER_THAN("{field}:{{from} TO *]"),
    GREATER_THAN_EQUAL("{field}:[{from} TO *]"),
    BEFORE("{field}:[* TO {to}}"),
    AFTER("{field}:{{from} TO *]"),
    LIKE("{field}:{value}*"),
    NOT_LIKE("-{field}:{value}*"),
    STARTING_WITH("{field}:{value}*"),
    ENDING_WITH("{field}:*{value}"),
    CONTAINING("{field}:*{value}*"),
    IN("{field}:({value}... )"),
    NOT_IN("-{field}:({value}...)"),
    TRUE("{field}:true"),
    FALSE("{field}:false");

    public final String value;

    Operators(String value) {
        this.value = value;
    }

    public static enum Replacements {
        FIELD,
        FIELD_VALUE,
        FIELD_FROM_TO,

    }
}
