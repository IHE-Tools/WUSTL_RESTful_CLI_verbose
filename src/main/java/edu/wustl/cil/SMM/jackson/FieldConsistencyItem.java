package edu.wustl.cil.SMM.jackson;

public class FieldConsistencyItem {
    private String optionality = "";
    private String severity = "";
    private String iteratedOperation = "";
    private String comparisonOperator = "";
    private String label = "";
    private String initial = "";
    private String initialSelector = "";
    private String initialMimeType = "";
    private String initialXPath    = "";
    private String derived         = "";
    private String derivedSelector = "";
    private String derivedMimeType = "";
    private String derivedXPath    = "";
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

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getInitialSelector() {
        return initialSelector;
    }

    public void setInitialSelector(String initialSelector) {
        this.initialSelector = initialSelector;
    }

    public String getInitialMimeType() {
        return initialMimeType;
    }

    public void setInitialMimeType(String initialMimeType) {
        this.initialMimeType = initialMimeType;
    }

    public String getInitialXPath() {
        return initialXPath;
    }

    public void setInitialXPath(String initialXPath) {
        this.initialXPath = initialXPath;
    }

    public String getDerived() {
        return derived;
    }

    public void setDerived(String derived) {
        this.derived = derived;
    }

    public String getDerivedSelector() {
        return derivedSelector;
    }

    public void setDerivedSelector(String derivedSelector) {
        this.derivedSelector = derivedSelector;
    }

    public String getDerivedMimeType() {
        return derivedMimeType;
    }

    public void setDerivedMimeType(String derivedMimeType) {
        this.derivedMimeType = derivedMimeType;
    }

    public String getDerivedXPath() {
        return derivedXPath;
    }

    public void setDerivedXPath(String derivedXPath) {
        this.derivedXPath = derivedXPath;
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
