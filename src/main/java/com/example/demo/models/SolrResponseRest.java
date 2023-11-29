package com.example.demo.models;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.common.SolrDocumentList;

import java.util.ArrayList;
import java.util.List;

//TODO create & handle converters
public class SolrResponseRest<T> {

    private Facets facets;
    private Results<T> objects;

    public static SolrResponseRest<?> instantiate(Class<?> parameterizedType) {
        if (parameterizedType.equals(TechProduct.class)) {
            return new SolrResponseRest<TechProduct>();
        } else {
            return new SolrResponseRest<>();
        }
    }

    public SolrResponseRest<T> facets(List<FacetField> facets) {
        this.facets = new Facets(facets);
        return this;
    }

    public SolrResponseRest<T> results(SolrDocumentList results) {
        this.objects = new Results<T>(results);
        return this;
    }

    private class Facets {
        private List<Facet> results;

        Facets(List<FacetField> facets) {
            results = new ArrayList<>();
            facets.forEach(facetField -> {
                facetField.getValues().forEach(value -> {
                    results.add(new Facet(value.getName(), value.getCount()));
                });
            });
        }

        private class Facet {
            private final String value;
            private final long count;

            Facet(String value, long count) {
                this.value = value;
                this.count = count;
            }
        }
    }

    private class Results<T> {

        List<T> results;

        Results(SolrDocumentList solrObjects) {
            results = new ArrayList<>();
            solrObjects.forEach(result -> {
                results.add((T) result);
            });
        }
    }
}
