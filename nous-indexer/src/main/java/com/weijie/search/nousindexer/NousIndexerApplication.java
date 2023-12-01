package com.weijie.search.nousindexer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class NousIndexerApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(NousIndexerApplication.class, args);

        // Initialize Lucene Indexer
        LuceneIndexer indexer = new LuceneIndexer();
        indexer.createIndex();

        // Initialize S3 Uploader
        S3Uploader uploader = new S3Uploader();
        uploader.uploadIndex(System.getProperty("user.home") + "/indexDirectory");
    }

}
