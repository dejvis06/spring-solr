# Spring Boot + Apache Solr 9.4.0

###  Build and Run:
- bash build-and-run.sh

### Connect to the Docker terminal (solr): 
- docker exec -t -i solr_v9 /bin/bash

#### Run Solr scripts:
1. bin/solr create -c films -s 2 -rf 2  (creates core)
2. bin/post -c films example/films/films.json (indexes the file)

## Description

It's a demo project build for learning about solrj.

### Versions

This project uses:
1. Spring Boot v3.1.0
2. Apache Solr 9.4.0
3. Java 17

## Spring Solr Configuration
This Spring-based configuration class facilitates the integration of Spring Solr with Apache Solr. It manages the initialization of the SolrClient, scans for Solr repositories annotated with @SolrRepository, and dynamically creates proxies for these repositories.

Features
1. SolrClient Initialization: Initializes the SolrClient using the provided configuration properties (solrUrl, solrCore).
2. Repository Scanning and Registration: Utilizes Spring's ApplicationRunner to scan for Solr repositories during the application's runtime. It dynamically registers these repositories as singletons in the Spring application context.
3. Dynamic Proxies: Uses reflection to discover classes annotated with @SolrRepository and creates dynamic proxies to handle repository invocations.

## SolrRepositoryInvocationHandler
The SolrRepositoryInvocationHandler is a dynamic proxy invocation handler responsible for handling method invocations on Solr repository proxies. It allows for custom query processing, providing flexibility in interacting with Solr repositories.

Features
Custom Query Processing: Enables custom query processing using the @Query annotation. <br/>
Solr Query Execution: Executes Solr queries based on the provided query metadata. <br/>
Result Handling: Returns the result as a structured SolrResponseRest object. <br/>
Usage

Annotation: Annotate your repository methods with @Query to enable custom query processing.

    @Query(
            fl = @FieldList(selected = {"name", "id"}),
            sort = @Sort(field = "id", order = SolrQuery.ORDER.desc),
            page = @Page(rows = 5),
            facet = @Facet(
                    facetField = @FacetField(selected = "name")
            )
    )
    SolrResponseRest<TechProduct> findAll(SolrQueryBuilder... solrQueryBuilder) throws SolrServerException, IOException;



