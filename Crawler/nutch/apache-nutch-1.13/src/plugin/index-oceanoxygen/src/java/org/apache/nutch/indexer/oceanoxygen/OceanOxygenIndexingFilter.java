/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nutch.indexer.oceanoxygen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.nutch.metadata.Metadata;

import org.apache.nutch.net.protocols.Response;

import org.apache.nutch.parse.Parse;

import org.apache.nutch.indexer.IndexingFilter;
import org.apache.nutch.indexer.IndexingException;
import org.apache.nutch.indexer.NutchDocument;

import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.Inlinks;
import org.apache.nutch.parse.ParseData;
import org.apache.tika.Tika;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import java.text.ParseException;

import java.lang.invoke.MethodHandles;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.*;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

/**
 * Add (or reset) a few metaData properties as respective fields (if they are
 * available), so that they can be accurately used within the search index.
 * 
 * 'lastModifed' is indexed to support query by date, 'contentLength' obtains
 * content length from the HTTP header, 'type' field is indexed to support query
 * by type and finally the 'title' field is an attempt to reset the title if a
 * content-disposition hint exists. The logic is that such a presence is
 * indicative that the content provider wants the filename therein to be used as
 * the title.
 * 
 * Still need to make content-length searchable!
 * 
 * @author John Xing
 */

public class OceanOxygenIndexingFilter implements IndexingFilter {
  private static final Logger LOG = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());


  public NutchDocument filter(NutchDocument doc, Parse parse, Text url,
      CrawlDatum datum, Inlinks inlinks) throws IndexingException {

    String url_s = url.toString();

	addDocumentScore(doc, parse.getData(), url_s, datum);	
		
    return doc;
  }
  
  private NutchDocument addDocumentScore(NutchDocument doc, ParseData data, 
  	String url, CrawlDatum datum) {
  	
	doc.add("documentScore", datum.getScore());
  	
  	return doc;	
  }
  
  private Configuration conf;

  /**
   * handles conf assignment and pulls the value assignment from the
   * "" property
   */
  public void setConf(Configuration conf) {
   	this.conf = conf;

    if (conf == null)
      return;

    // urlMetaTags = conf.getStrings(CONF_PROPERTY);
  }

  public Configuration getConf() {
    return this.conf;
  }
}
