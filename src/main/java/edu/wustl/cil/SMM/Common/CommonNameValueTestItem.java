package edu.wustl.cil.SMM.Common;

import java.io.PrintWriter;

public class CommonNameValueTestItem {
    private String itemType;

    private String testOperator = null;
    private String testSeverity = null;
    private String testCaption = null;
    private String testPath = null;

    private String directive = null;
    private String[] args = null;

    private String testResult = null;
    private String reference  = null;
    private String underTest  = null;

    public CommonNameValueTestItem(String[] tokens) {
        if (tokens[0].equals("HEADING")) {
            itemType = "DIRECTIVE";
            directive  = tokens[0];
            args = new String[tokens.length-1];
            for (int i = 0; i < tokens.length-1; i++) {
                args[i] = tokens[i+1];
            }
        } else {
            itemType = "TEST";
            testOperator = tokens[0];
            testSeverity = tokens[1];
            testCaption  = tokens[2];
            testPath     = tokens[3];
        }
    }

    public CommonNameValueTestItem(CommonNameValueTestItem originalItem) {
        itemType = originalItem.itemType;
        testOperator = originalItem.testOperator;
        testSeverity = originalItem.testSeverity;
        testCaption  = originalItem.testCaption;
        testPath     = originalItem.testPath;
        directive    = originalItem.directive;
        testResult   = originalItem.testResult;
        reference    = originalItem.reference;
        underTest    = originalItem.underTest;

        if (originalItem.args != null) {
            int length = originalItem.args.length;
            args = new String[length];
            for (int i = 0; i < length; i++) {
                args[i] = originalItem.args[i];
            }
        }

    }

    public void testValue(PrintWriter writer, CommonTestInstance testInstance, String referenceString, String stringUnderTest) throws Exception {
        if (! itemType.equals("TEST")) {
            throw new Exception("In CommonNameValueTestItem::testValue this test item is of type: " + itemType);
        }
        reference = referenceString;
        underTest = stringUnderTest;
        switch (testOperator) {
            case "EQUAL":
                if (stringUnderTest == null) {
                    String msg = "\n" +
                            "Operator:   " + testOperator  + "\n" +
                            "Path:       " + testPath + "\n" +
                            "Result:     Null value\n" +
                            "Severity:   " + this.testSeverity;
                    testResult = this.testSeverity;
                    writer.println(msg);
                    if (this.testSeverity.equals("ERROR")) testInstance.addError(msg);
                    else                                   testInstance.addWarning(msg);
                } else if (! stringUnderTest.equals(referenceString)) {
                    String msg = "\n" +
                            "Operator:   " + testOperator  + "\n" +
                            "Path:       " + testPath + "\n" +
                            "Result:     Values differ\n" +
                            "Reference:  " + referenceString + "\n" +
                            "Under Test: " + stringUnderTest + "\n" +
                            "Severity:   " + this.testSeverity;
                    testResult = this.testSeverity;
                    writer.println(msg);
                    if (this.testSeverity.equals("ERROR")) testInstance.addError(msg);
                    else                                   testInstance.addWarning(msg);
                } else {
                    writer.println("\n" +
                            "Operator:   " + testOperator  + "\n" +
                            "Path:       " + testPath + "\n" +
                            "Result:     Successful comparison\n" +
                            "Reference:  " + referenceString);
                    testResult = "PASS";
                }
                break;
            case "NOTNULL":
                if (stringUnderTest == null) {
                    String msg = "\n" +
                            "Operator:   " + testOperator  + "\n" +
                            "Path:       " + testPath + "\n" +
                            "Result:     Null value\n" +
                            "Severity:   " + this.testSeverity;
                    testResult = this.testSeverity;
                    writer.println(msg);
                    if (this.testSeverity.equals("ERROR")) testInstance.addError(msg);
                    else                                   testInstance.addWarning(msg);
                } else {
                    testResult = "PASS";
                    writer.println("\n" +
                            "Operator:   " + testOperator + "\n" +
                            "Path:       " + testPath + "\n" +
                            "Result:     Value not null as requested\n" +
                            "Value:      " + stringUnderTest);
                }
                break;
            case "NOTEQUAL":
                if (stringUnderTest == null) {
                    String msg = "\n" +
                            "Operator:   " + testOperator  + "\n" +
                            "Path:       " + testPath + "\n" +
                            "Result:     Null value\n" +
                            "Severity:   " + this.testSeverity;
                    testResult = this.testSeverity;
                    writer.println(msg);
                    if (this.testSeverity.equals("ERROR")) testInstance.addError(msg);
                    else                                   testInstance.addWarning(msg);
                } else if (stringUnderTest.equals(referenceString)) {
                    String msg = "\n" +
                            "Operator:   " + testOperator  + "\n" +
                            "Path:       " + testPath + "\n" +
                            "Result:     Values should not be the same\n" +
                            "Reference:  " + referenceString + "\n" +
                            "Under Test: " + stringUnderTest + "\n" +
                            "Severity:   " + this.testSeverity;
                    testResult = this.testSeverity;
                    writer.println(msg);
                    if (this.testSeverity.equals("ERROR")) testInstance.addError(msg);
                    else                                   testInstance.addWarning(msg);
                } else {
                    writer.println("\n" +
                            "Operator:   " + testOperator  + "\n" +
                            "Path:       " + testPath + "\n" +
                            "Result:     Successful comparison (values differ as expected)\n" +
                            "Reference:  " + referenceString);
                    testResult = "PASS";
                }
                break;
        }
    }

    // Getters / setters below here


    public String getItemType() {
        return itemType;
    }

    public String getTestCaption() {
        return testCaption;
    }


    public String[] getPathArray() { return testPath.split("/");}

    public String getTestPath() {
        return testPath;
    }

    public String getTestOperator() {
        return testOperator;
    }


    public String getTestSeverity() {
        return testSeverity;
    }

    public String getArgument(int i) {
        if (args == null) return null;

        if (args.length == 0) return null;

        if (i > args.length) return null;

        return args[i];
    }

    public String getDirective() {
        return directive;
    }

    public String[] getArgs() {
        return args;
    }

    public String getTestResult() {
        return testResult;
    }

    public String getReference() {
        return reference;
    }

    public String getUnderTest() {
        return underTest;
    }
}
