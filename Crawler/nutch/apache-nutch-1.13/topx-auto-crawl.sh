#!/bin/bash


#Using the bin/crawl script from Nutch destribution

NAME=oxygenCrawl
URLLOC="urls"
CORENAME=OxygenCrawl
SOLRURL="-D solr.server.url=http://localhost:8983/solr/$CORENAME"


if ! [[ "$1" =~ ^[0-9]+$ ]]
  then echo Please enter the number of crawl rounds as parameter.
  else NUMROUNDS=$1
       $NUTCH_RUNTIME_HOME/bin/crawl-topx -i $SOLRURL $URLLOC $NAME $NUMROUNDS
fi
