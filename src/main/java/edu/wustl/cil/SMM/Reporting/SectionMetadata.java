package edu.wustl.cil.SMM.Reporting;

public class SectionMetadata {
    private String key = "";
    private String value = "";

    public SectionMetadata(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public SectionMetadata() {
    }

    public SectionMetadata(SectionMetadata sectionMetadata) {
        this.key = sectionMetadata.key;
        this.value = sectionMetadata.value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
