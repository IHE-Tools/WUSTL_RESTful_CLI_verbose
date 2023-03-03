package edu.wustl.cil.SMM.XDS;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

public class SOAPEnvelope {
    private String filePath;
    private Document document;
    private Node submitObjectsRequestNode;
    private NodeList documentNodeList;

    public SOAPEnvelope() {
    }

    public void initialize(String filePath) throws Exception {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Node getSubmitObjectsRequestNode() {
        return submitObjectsRequestNode;
    }

    public void setSubmitObjectsRequestNode(Node submitObjectsRequestNode) {
        this.submitObjectsRequestNode = submitObjectsRequestNode;
    }

    public NodeList getDocumentNodeList() {
        return documentNodeList;
    }

    public void setDocumentNodeList(NodeList documentNodeList) {
        this.documentNodeList = documentNodeList;
    }
}
