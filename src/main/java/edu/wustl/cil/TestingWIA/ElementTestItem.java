package edu.wustl.cil.TestingWIA;

import org.nrg.xnat.dicom.model.DicomAttribute;

import java.io.PrintWriter;

public class ElementTestItem {
    private String[] elementTags;
    private String   testOperator;
    private String   testSeverity;

    public ElementTestItem(String tags, String testOperator, String testSeverity) {
        elementTags = tags.split("/");
        this.testOperator = testOperator;
        this.testSeverity = testSeverity;
    }

    public ElementTestItem(String[] tokens) {
        this(tokens[0], tokens[1], tokens[2]);
    }

    public void testValue(PrintWriter writer, TestInstance testInstance, String referenceString, String stringUnderTest) {
        switch (testOperator) {
            case "EQUAL":
                if (stringUnderTest == null) {
                    String msg =
                            "Tag:        " + elementTags[0] + "\n" +
                            "Condition:  Null value\n" +
                            "Severity:   " + this.testSeverity;
                    writer.println(msg);
                    if (this.testSeverity.equals("ERROR")) testInstance.addError(msg);
                    else                                   testInstance.addWarning(msg);
                } else if (! stringUnderTest.equals(referenceString)) {
                    String msg =
                            "Tag:        " + elementTags[0] + "\n" +
                            "Condition:  Values differ\n" +
                            "Reference:  " + referenceString + "\n" +
                            "Under Test: " + stringUnderTest + "\n" +
                            "Severity:   " + this.testSeverity;
                    writer.println(msg);
                    if (this.testSeverity.equals("ERROR")) testInstance.addError(msg);
                    else                                   testInstance.addWarning(msg);
                } else {
                    writer.println("" +
                            "Tag:        " + elementTags[0] + "\n" +
                            "Condition:  Successful comparison\n" +
                            "Reference:  " + referenceString);
                }
                break;
            case "NOTNULL":
                if (stringUnderTest == null) {
                    String msg =
                            "Tag:        " + elementTags[0] + "\n" +
                            "Condition:  Null value\n" +
                            "Severity:   " + this.testSeverity;
                    writer.println(msg);
                    if (this.testSeverity.equals("ERROR")) testInstance.addError(msg);
                    else                                   testInstance.addWarning(msg);
                } else {
                    writer.println("" +
                            "Tag:        " + elementTags[0] + "\n" +
                            "Condition:  Value not null as requested\n" +
                            "Value:      " + stringUnderTest);
                }
                break;
            case "NOTEQUAL":
                break;
        }
    }

    public void testAttribute(PrintWriter writer, TestInstance testInstance, DicomAttribute reference, DicomAttribute underTest) {
        switch (testOperator) {
            case "EQUAL":
                if (underTest == null) {
                    String msg =
                            "Tag:        " + elementTags[0] + "\n" +
                            "Condition:  Null value" +
                            "Severity:   " + this.testSeverity;
                    writer.println(msg);
                    if (this.testSeverity.equals("ERROR")) testInstance.addError(msg);
                    else                                   testInstance.addWarning(msg);
                } else if (! underTest.equals(reference)) {
                    String msg =
                            "Tag:        " + elementTags[0] + "\n" +
                            "Condition:  Values differ" +
                            "Reference:  " + reference.getSingletonStringValue() + "\n" +
                            "Under Test: " + underTest.getSingletonStringValue() + "\n" +
                            "Severity:   " + this.testSeverity;
                    writer.println(msg);
                    if (this.testSeverity.equals("ERROR")) testInstance.addError(msg);
                    else                                   testInstance.addWarning(msg);
                } else {
                    writer.println("" +
                            "Tag:        " + elementTags[0] + "\n" +
                            "Condition:  Successful comparison: " +
                            reference.getSingletonStringValue()
                    );
                }
                break;
            case "NOTNULL":
                if (underTest == null) {
                    String msg =
                            "Tag:        " + elementTags[0] + "\n" +
                            "Condition:  Null value" +
                            "Severity:   " + this.testSeverity;
                    writer.println(msg);
                    if (this.testSeverity.equals("ERROR")) testInstance.addError(msg);
                    else                                   testInstance.addWarning(msg);
                } else {
                    writer.println("" +
                            "Tag:        " + elementTags[0] + "\n" +
                            "Condition:  Value not null as requested\n");
                }
                break;
            case "NOTEQUAL":
                break;
            default:
                break;
        }
    }
    // Getters / setters below here

    public String[] getElementTags() {
        return elementTags;
    }

    public void setElementTags(String[] elementTags) {
        this.elementTags = elementTags;
    }

    public String getTestOperator() {
        return testOperator;
    }

    public void setTestOperator(String testOperator) {
        this.testOperator = testOperator;
    }

    public String getTestSeverity() {
        return testSeverity;
    }

    public void setTestSeverity(String testSeverity) {
        this.testSeverity = testSeverity;
    }
}
