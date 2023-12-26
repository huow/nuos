package com.weijie.search.nousindexer;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MindMapNode {
    @Getter @Setter
    private String id;
    // The text in the Node
    @Getter @Setter
    private String text;
    // The attached note for the Node
    @Getter @Setter
    private String note;
    // Links to child nodes
    @Getter @Setter
    private List<MindMapNode> childs;

    public MindMapNode(String id, String text, String note, List<MindMapNode> childs) {
        this.id = id;
        this.text = text;
        this.note = note;
        this.childs = childs;
    }
}
