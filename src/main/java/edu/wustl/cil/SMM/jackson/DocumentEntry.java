package edu.wustl.cil.SMM.jackson;

public class DocumentEntry {
    private String mimeType;
    private String identifier;
    private String documentPath;

    public DocumentEntry(String mimeType, String identifier, String documentPath) {
        this.mimeType     = mimeType;
        this.identifier   = identifier;
        this.documentPath = documentPath;
    }

    public DocumentEntry() {}

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }
}
