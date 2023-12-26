package com.weijie.search.nousindexer;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class MindMapNode {
    private String id;
    // The text in the Node
    private String text;
    // The attached note for the Node
    private String note;
    // Links to child nodes
    private List<MindMapNode> childs;

    public MindMapNode(String id, String text, String note, List<MindMapNode> childs) {
        this.id = id;
        this.text = text;
        this.note = note;
        this.childs = childs;
    }
}
