package com.weijie.search.nousindexer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LuceneIndexer {

    public void createIndex() throws IOException {
        Path indexPath = Paths.get(System.getProperty("user.home") + "/indexDirectory");

        if (!Files.exists(indexPath)) {
            try {
                Files.createDirectories(indexPath);
            } catch (IOException e) {
                // Handle the exception appropriately
                e.printStackTrace();
            }
        }
        Directory directory = FSDirectory.open(indexPath);
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try (IndexWriter indexWriter = new IndexWriter(directory, config)) {
            // Process data and add documents to the index
            // Example of adding a document:
            Document document = new Document();
            document.add(new TextField("fieldName", "text to index", Field.Store.YES));
            indexWriter.addDocument(document);

            // Add more documents as needed
            // ...

            // Commit and close are handled in the try-with-resources statement
        } catch (IOException e) {
            // Proper exception handling goes here
            e.printStackTrace();
        }
    }
}
