package edu.wustl.cil.SMM.jackson;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

public class FileExtract {
    private String metadata;
    @JacksonXmlElementWrapper(localName="documentEntries")
    @JacksonXmlProperty(localName="documentEntry")
    private List<DocumentEntry> documentEntries = new ArrayList<>();

    public FileExtract() {
    }

    public void addDocumentEntry(final DocumentEntry documentEntry) {
        documentEntries.add(documentEntry);
    }

    public DocumentEntry getDocumentEntry(String mimeType) throws Exception {
        DocumentEntry returnEntry = null;
        for (DocumentEntry documentEntry: documentEntries) {
            if (documentEntry.getMimeType().equals(mimeType)) {
                if (returnEntry != null) {
                    throw new Exception("Found a second document entry for this mimeType: " + mimeType);
                }
                returnEntry = documentEntry;
            }
        }
        return returnEntry;
    }

    // Standard getters and setters

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public List<DocumentEntry> getDocumentEntries() {
        return documentEntries;
    }

    public void setDocumentEntries(List<DocumentEntry> documentEntries) {
        this.documentEntries = documentEntries;
    }
}
