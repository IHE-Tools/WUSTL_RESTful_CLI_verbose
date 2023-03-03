package edu.wustl.cil.SMM.jackson;

public class FieldValidationItem {
    private String optionality = "";
    private String severity = "";
    private String iteratedOperation = "";
    private String comparisonOperator = "";
    private String label = "";
    private String xPath = "";
    private String referenceValue = "";
    private String docReference = "";
    private String comments = "";

    @Override
    public String toString() {
        return String.format("FieldValidationItem: Optionality %s, Severity %s, Iterated %s ComparisonOP %s, Label %s, XPath %s, ReferenceValue %s, DocRef %s, Comments %s",
                optionality,
                severity,
                iteratedOperation,
                comparisonOperator,
                label,
                xPath,
                referenceValue,
                docReference,
                comments);
    }

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
