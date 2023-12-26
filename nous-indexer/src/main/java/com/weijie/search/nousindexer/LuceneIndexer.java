package com.weijie.search.nousindexer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LuceneIndexer {

    public void createIndex(List<MindMapNode> mindMapNodes) throws IOException {
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
        /*
        * The code snippet you provided uses a try-with-resources statement in Java,
        * which automatically closes resources that implement the AutoCloseable
        * interface at the end of the statement. The IndexWriter class in Lucene
        * implements AutoCloseable, so it gets automatically closed when the
        * try-with-resources block is exited.
        */
        try (IndexWriter indexWriter = new IndexWriter(directory, config)) {
            for (MindMapNode mindMapNode : mindMapNodes) {
                addDocuments(mindMapNode, new ArrayList<>(), indexWriter);
            }

            // Commit and close are handled in the try-with-resources statement
            indexWriter.commit();
        } catch (IOException e) {
            // Proper exception handling goes here
            e.printStackTrace();
        }
    }

    private void addDocuments(
            MindMapNode mindMapNode,
            List<MindMapNode> parents, IndexWriter indexWriter) throws IOException {

        if (mindMapNode == null) {
            return;
        }

        parents.add(mindMapNode);
        for (MindMapNode child : mindMapNode.getChilds()) {
            addDocuments(child, parents, indexWriter);
        }
        parents.remove(mindMapNode);

        Document doc = new Document();
        doc.add(new StringField("id", mindMapNode.getId(), Field.Store.YES));
        doc.add(new TextField("text", mindMapNode.getText(), Field.Store.YES));
        doc.add(new TextField("note", mindMapNode.getNote(), Field.Store.YES));
        doc.add(new TextField("catchAll", mindMapNode.getText() + mindMapNode.getNote(), Field.Store.NO));

        for (MindMapNode child : mindMapNode.getChilds()) {
            doc.add(new StringField("childId", child.getId(), Field.Store.YES));
        }
        indexWriter.addDocument(doc);
    }
}
