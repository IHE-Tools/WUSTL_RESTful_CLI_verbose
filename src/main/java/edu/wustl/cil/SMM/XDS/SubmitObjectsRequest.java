package edu.wustl.cil.SMM.XDS;

import org.w3c.dom.Document;

import java.util.List;

public class SubmitObjectsRequest {
    private String filePath;
    private Document document;
    private List<ExtrinsicObject> extrinsicObjects = null;
    private RegistryPackage registryPackage = null;

    public SubmitObjectsRequest() {
    }

    public void initialize(String filePath) throws Exception {
        this.filePath = filePath;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public List<ExtrinsicObject> getExtrinsicObjects() {
        return extrinsicObjects;
    }

    public void setExtrinsicObjects(List<ExtrinsicObject> extrinsicObjects) {
        this.extrinsicObjects = extrinsicObjects;
    }

    public RegistryPackage getRegistryPackage() {
        return registryPackage;
    }

    public void setRegistryPackage(RegistryPackage registryPackage) {
        this.registryPackage = registryPackage;
    }
}
