package edu.wustl.cil.SMM.Reporting;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class Section {
    private String title = "";
    @JacksonXmlElementWrapper(localName="items")
    @JacksonXmlProperty(localName="lineItem")
    private List lineItems;
    @JacksonXmlElementWrapper(localName="sectionMetadataItems")
    @JacksonXmlProperty(localName="metadataItem")
    private List metadataItems;

    public Section(String title) {
        this.title = title;
        lineItems = new ArrayList<LineItem>();
        metadataItems = new ArrayList<SectionMetadata>();
    }

    public void addLineItem(LineItem lineItem) {
        lineItems.add(new LineItem(lineItem));
    }

    public void addMetadata(SectionMetadata metadata) {metadataItems.add(new SectionMetadata(metadata));}


    // Standard getter/setter


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public List getMetadataItems() {
        return metadataItems;
    }

    public void setMetadataItems(List metadataItems) {
        this.metadataItems = metadataItems;
    }
}
