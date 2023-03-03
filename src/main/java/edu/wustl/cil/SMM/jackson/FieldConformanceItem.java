package edu.wustl.cil.SMM.jackson;

public class FieldConformanceItem {
    private String optionality = "";
    private String severity = "";
    private String iteratedOperation = "";
    private String comparisonOperator = "";
    private String label = "";
    private String source = "";
    private String selector = "";
    private String mimeType = "";
    private String xPath = "";
    private String referenceValue = "";
    private String docReference = "";
    private String comments = "";

    public String getOptionality() {
        return optionality;
    }

    public void setOptionality(String optionality) {
        this.optionality = optionality;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getIteratedOperation() {
        return iteratedOperation;
    }

    public void setIteratedOperation(String iteratedOperation) {
        this.iteratedOperation = iteratedOperation;
    }

    public String getComparisonOperator() {
        return comparisonOperator;
    }

    public void setComparisonOperator(String comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getxPath() {
        return xPath;
    }

    public void setxPath(String xPath) {
        this.xPath = xPath;
    }

    public String getReferenceValue() {
        return referenceValue;
    }

    public void setReferenceValue(String referenceValue) {
        this.referenceValue = referenceValue;
    }

    public String getDocReference() {
        return docReference;
    }

    public void setDocReference(String docReference) {
        this.docReference = docReference;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
