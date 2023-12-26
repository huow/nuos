package com.weijie.search.nousindexer;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MindMapParser {

    private final String filePath;

    public MindMapParser(String filePath) {
        this.filePath = filePath;
    }

    public List<MindMapNode> parseXMLFile() {
        SAXBuilder saxBuilder = new SAXBuilder();
        try {
            // Parse the XML file
            Document document = saxBuilder.build(new File(filePath));
            Element rootNode = document.getRootElement();
            return processNode(rootNode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<MindMapNode> processNode(Element node) {
        List<MindMapNode> mindMapNodes = new ArrayList<>();
        StringBuilder note = new StringBuilder();

        // Recursively process child nodes
        List<Element> children = node.getChildren();
        for (Element child : children) {
            if (child.getName().equals("node")) {
                mindMapNodes.addAll(processNode(child));
            } else if (child.getName().equals("richcontent")) {
                note.append(getNote(child));
            }
        }

        if (node.getName().equals("node")) {
            String id = node.getAttributeValue("ID");
            String text = node.getAttributeValue("TEXT");
            return List.of(new MindMapNode(id, text, note.toString(), mindMapNodes));
        }
        return mindMapNodes;
    }

    private StringBuilder getNote(Element richcontentNode) {
        List<Element> paragraphs = richcontentNode.getChild("html").getChild("body").getChildren();
        StringBuilder note = new StringBuilder();
        for (Element p : paragraphs) {
            List<Element> spans = p.getChildren();
            if (spans.isEmpty()) {
                note.append(p.getText());
            } else {
                for (Element span : spans) {
                    note.append(span.getText());
                }
            }
        }
        return note;
    }
}
