package edu.wustl.cil.SMM.Reporting;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

public class TestReport {
    private Metadata metadata      = new Metadata();
    @JacksonXmlElementWrapper(localName="sections")
    @JacksonXmlProperty(localName="section")
    private List<Section> sectionList = new ArrayList<>();

    // Methods below will set values at various levels in the report
    public void setTitle(String title) {
        metadata.setTitle(title);
    }

    public void setTimeStamp(String timeStamp) {
        metadata.setTimeStamp(timeStamp);
    }

    public void addSection(String title) {
        sectionList.add(new Section(title));
    }

    public void addLineItem(LineItem.ItemType itemType, String label, String assertion, String context, String message, String referenceValue, String submittedValue, String docReference, String comments) throws Exception {
        LineItem lineItem = new LineItem(itemType, label, assertion, context, message, referenceValue, submittedValue, docReference, comments);
        sectionList.get(sectionList.size()-1).addLineItem(lineItem);
    }

    public void addSectionMetadata(String key, String value) {
        SectionMetadata sectionMetadata = new SectionMetadata(key, value);
        sectionList.get(sectionList.size()-1).addMetadata(sectionMetadata);
    }

    // Standard getters and setters

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSections(List<Section> sectionList) {
        this.sectionList = sectionList;
    }
}
