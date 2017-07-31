#!/bin/bash


#Using the bin/crawl script from Nutch destribution

NAME=oxygenCrawl_Bayes
URLLOC="urls"
CORENAME=OxygenCrawl_Bayes
SOLRURL="-D solr.server.url=http://localhost:8983/solr/$CORENAME"

if ! [[ "$1" =~ ^[0-9]+$ ]]
  then 
    echo Please enter the number of crawl rounds as parameter.
  elif [ ! -d "$NUTCH_RUNTIME_HOME"/"$NAME" ]
    then
      NUMROUNDS=$1
      echo "Starting Crawl with $1 rounds."
      $NUTCH_RUNTIME_HOME/bin/crawl -i $SOLRURL $URLLOC $NAME $NUMROUNDS
  else 
    Option to crawl without injecting new URLS
    echo "Starting crawl without injecting."  
    NUMROUNDS=$1
    echo "Starting Crawl with $1 rounds."
    $NUTCH_RUNTIME_HOME/bin/crawl_noinject -i $SOLRURL $URLLOC $NAME $NUMROUNDS
fi
