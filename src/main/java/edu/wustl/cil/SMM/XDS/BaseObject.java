package edu.wustl.cil.SMM.XDS;

import org.w3c.dom.Node;

import java.util.Map;
import java.util.TreeMap;

public class BaseObject {
    protected Node node = null;
    protected String identifier = "";
    protected TreeMap<String, String> keyValueMap = new TreeMap<>();

    public BaseObject(Node node, String identifier) {
        this.node = node;
        this.identifier = identifier;
        this.keyValueMap = new TreeMap<>();
    }

    public BaseObject() { }

     public void putValue(String key, String value) {
        keyValueMap.put(key, value);
    }

    public String getValue(String key) {
        return keyValueMap.get(key);
    }

    // Standard getter/setters

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Map<String, String> getKeyValueMap() {
        return keyValueMap;
    }

    public void setKeyValueMap(TreeMap<String, String> keyValueMap) {
        this.keyValueMap = keyValueMap;
    }
}
