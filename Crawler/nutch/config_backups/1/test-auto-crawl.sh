#!/bin/bash


#Using the bin/crawl script from Nutch destribution

NAME=nature_crawl
URLLOC="urls-2"
CORENAME=OxygenCrawl2
SOLRURL="-D solr.server.url=http://localhost:8983/solr/$CORENAME"
NUMROUNDS=2

$NUTCH_RUNTIME_HOME/bin/crawl $SOLRURL $URLLOC $NAME/crawldb $NUMROUNDS
