package edu.wustl.cil.SMM.Reporting;

public class LineItem {
    public enum ItemType { SUCCESS, VERBOSE, INFO, WARNING, ERROR, TEXT, UNKNOWN };

    private String type      = "";
    private String label     = "";
    private String assertion = "";
    private String context   = "";
    private String message   = "";
    private String referenceValue = "";
    private String submittedValue = "";
    private String docReference = "";
    private String comments = "";

    public LineItem(LineItem lineItem) {
        this.type      = lineItem.type;
        this.label     = lineItem.label;
        this.assertion = lineItem.assertion;
        this.context   = lineItem.context;
        this.message   = lineItem.message;
        this.referenceValue = lineItem.referenceValue;
        this.submittedValue = lineItem.submittedValue;
        this.docReference = lineItem.docReference;
        this.comments     = lineItem.comments;
    }

    public LineItem(ItemType itemType, String label, String assertion, String context, String message, String referenceValue, String submittedValue, String docReference, String comments) {
        this(itemType);
        this.label     = label;
        this.assertion = assertion;
        this.context   = context;
        this.message   = message;
        this.referenceValue = referenceValue;
        this.submittedValue = submittedValue;
        this.docReference = docReference;
        this.comments     = comments;
    }

    public LineItem(ItemType itemType) {
        switch (itemType) {
            case SUCCESS:
                type = "SUCCESS";
                break;
            case VERBOSE:
                type = "VERBOSE";
                break;
            case INFO:
                type = "INFO";
                break;
            case WARNING:
                type = "WARNING";
                break;
            case ERROR:
                type = "ERROR";
                break;
            case TEXT:
                type = "TEXT";
                break;
            case UNKNOWN:
            default:
                type = "UNKNOWN";
                break;
        }
    }

    static public ItemType translate(final String input) {
        ItemType rtn = ItemType.UNKNOWN;
        switch (input) {
            case "SUCCESS":
                rtn = ItemType.SUCCESS;
                break;
            case "VERBOSE":
                rtn = ItemType.VERBOSE;
                break;
            case "INFO":
                rtn = ItemType.INFO;
                break;
            case "WARNING":
                rtn = ItemType.WARNING;
                break;
            case "ERROR":
                rtn = ItemType.ERROR;
                break;
            case "TEXT":
                rtn = ItemType.TEXT;
                break;
            case "UNKNOWN":
            default:
                rtn = ItemType.UNKNOWN;
                break;
        }
        return rtn;
    }


    // Standard getters and setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAssertion() {
        return assertion;
    }

    public void setAssertion(String assertion) {
        this.assertion = assertion;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReferenceValue() {
        return referenceValue;
    }

    public void setReferenceValue(String referenceValue) {
        this.referenceValue = referenceValue;
    }

    public String getSubmittedValue() {
        return submittedValue;
    }

    public void setSubmittedValue(String submittedValue) {
        this.submittedValue = submittedValue;
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
