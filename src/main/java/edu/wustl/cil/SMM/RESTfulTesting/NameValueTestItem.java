package edu.wustl.cil.SMM.RESTfulTesting;

import edu.wustl.cil.TestingWIA.TestInstance;
//import org.nrg.xnat.dicom.model.DicomAttribute;

import java.io.PrintWriter;

public class NameValueTestItem {
    private String path;
    private String testOperator;
    private String testSeverity;

    public NameValueTestItem(String testOperator, String testSeverity, String path) {
        this.path = path;
        this.testOperator = testOperator;
        this.testSeverity = testSeverity;
    }

    public NameValueTestItem(String[] tokens) {
        this(tokens[0], tokens[1], tokens[2]);
    }

    public void testValue(PrintWriter writer, RESTfulTestInstance testInstance, String referenceString, String stringUnderTest) {
        switch (testOperator) {
            case "EQUAL":
                if (stringUnderTest == null) {
                    String msg = "\n" +
                            "Operator:   " + testOperator  + "\n" +
                            "Path:       " + path + "\n" +
                            "Result:     Null value\n" +
                            "Severity:   " + this.testSeverity;
                    writer.println(msg);
                    if (this.testSeverity.equals("ERROR")) testInstance.addError(msg);
                    else                                   testInstance.addWarning(msg);
                } else if (! stringUnderTest.equals(referenceString)) {
                    String msg = "\n" +
                            "Operator:   " + testOperator  + "\n" +
                            "Path:       " + path + "\n" +
                            "Result:     Values differ\n" +
                            "Reference:  " + referenceString + "\n" +
                            "Under Test: " + stringUnderTest + "\n" +
                            "Severity:   " + this.testSeverity;
                    writer.println(msg);
                    if (this.testSeverity.equals("ERROR")) testInstance.addError(msg);
                    else                                   testInstance.addWarning(msg);
                } else {
                    writer.println("\n" +
                            "Operator:   " + testOperator  + "\n" +
                            "Path:       " + path + "\n" +
                            "Result:     Successful comparison\n" +
                            "Reference:  " + referenceString);
                }
                break;
            case "NOTNULL":
                if (stringUnderTest == null) {
                    String msg = "\n" +
                            "Operator:   " + testOperator  + "\n" +
                            "Path:       " + path + "\n" +
                            "Result:     Null value\n" +
                            "Severity:   " + this.testSeverity;
                    writer.println(msg);
                    if (this.testSeverity.equals("ERROR")) testInstance.addError(msg);
                    else                                   testInstance.addWarning(msg);
                } else {
                    writer.println("\n" +
                            "Operator:   " + testOperator + "\n" +
                            "Path:       " + path + "\n" +
                            "Result:     Value not null as requested\n" +
                            "Value:      " + stringUnderTest);
                }
                break;
            case "NOTEQUAL":
                break;
        }
    }

    // Getters / setters below here

    public String[] getPathArray() { return path.split("/");}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
