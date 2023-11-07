# Spring Boot + Apache Solr 9.4.0

###  Build and Run:
- bash build-and-run.sh

### Connect to the Docker terminal (solr): 
- docker exec -t -i solr_v8 /bin/bash

### Run Solr scripts:
1. bin/solr create -c films -s 2 -rf 2  (creates core)
2. bin/post -c films example/films/films.json (indexes the file)

## Description

### Versions

This project uses:
1. Spring Boot v3.1.0
2. Apache Solr 9.4.0
3. Java 17

It's a demo project build for learning about solrj.