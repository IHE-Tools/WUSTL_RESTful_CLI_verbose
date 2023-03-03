package edu.wustl.cil.SMM.XDS;

import org.w3c.dom.Node;

import java.util.Map;
import java.util.TreeMap;

public class ExtrinsicObject  extends BaseObject {
    private String mimeType = "";

    public ExtrinsicObject(Node node, String mimeType, String identifier) {
        this.node = node;
        this.identifier = identifier;
        this.mimeType = mimeType;
        this.keyValueMap = new TreeMap<>();
    }

    // Standard getter/setters

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
