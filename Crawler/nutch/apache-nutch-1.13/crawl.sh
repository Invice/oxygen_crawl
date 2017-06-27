#!/bin/bash

NAME=nature_crawl
URLLOC=urls-2
CORENAME=OxygenCrawl2
SOLRURL="-D solr.server.url=http://localhost:8983/solr/$CORENAME"

bin/nutch inject $NAME/crawldb $URLLOC

bin/nutch generate $NAME/crawldb $NAME/segments
s1=`ls -d $NAME/segments/2* | tail -1`
bin/nutch fetch $s1 -threads 3
bin/nutch parse $s1
bin/nutch updatedb $NAME/crawldb $s1

bin/nutch invertlinks $NAME/linkdb -dir $NAME/segments
bin/nutch dedup $NAME/crawldb/
bin/nutch index $SOLRURL  $NAME/crawldb/ -linkdb $NAME/linkdb $s1
bin/nutch clean $SOLRURL $NAME/crawldb/

bin/nutch generate $NAME/crawldb $NAME/segments
s2=`ls -d $NAME/segments/2* | tail -1`
bin/nutch fetch $s2 -threads 3
bin/nutch parse $s2
bin/nutch updatedb $NAME/crawldb $s2

bin/nutch invertlinks $NAME/linkdb -dir $NAME/segments
bin/nutch dedup $NAME/crawldb/
bin/nutch index $SOLRURL  $NAME/crawldb/ -linkdb $NAME/linkdb $s2 
bin/nutch clean $SOLRURL $NAME/crawldb/

#bin/nutch generate $NAME/crawldb $NAME/segments 
#s2=`ls -d $NAME/segments/2* | tail -1`
#bin/nutch fetch $s2
#bin/nutch parse $s2
#bin/nutch updatedb $NAME/crawldb $s2
#bin/nutch invertlinks $NAME/linkdb -dir $NAME/segments
#bin/nutch dedup $NAME/crawldb/
#bin/nutch index $SOLRURL $NAME/crawldb/ -linkdb $NAME/linkdb $s2
#bin/nutch clean $SOLRURL crawl/crawldb/

#bin/nutch generate $NAME/crawldb $NAME/segments
#s3=`ls -d $NAME/segments/2* | tail -1`
#bin/nutch fetch $s3
#bin/nutch parse $s3
#bin/nutch updatedb $NAME/crawldb $s3
#bin/nutch invertlinks $NAME/linkdb -dir $NAME/segments
#bin/nutch dedup $NAME/crawldb/
#bin/nutch index $SOLRURL $NAME/crawldb/ -linkdb $NAME/linkdb $s3
#bin/nutch clean $SOLRURL $NAME/crawldb/
