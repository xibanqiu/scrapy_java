
package com.github.crawler.plugin.raw;


import com.github.crawler.api.CrawlCandidate;

import java.util.HashMap;


public class RamDB {
    
    protected HashMap<String, CrawlCandidate> crawlDB = new HashMap<>();
    protected HashMap<String, CrawlCandidate> fetchDB = new HashMap<>();
    protected HashMap<String, CrawlCandidate> linkDB = new HashMap<>();
    protected HashMap<String, String> redirectDB = new HashMap<String, String>();
}
