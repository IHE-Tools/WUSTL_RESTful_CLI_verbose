package edu.wustl.cil.SMM.jackson;

import edu.wustl.cil.SMM.Reporting.LineItem;
import edu.wustl.cil.SMM.Reporting.TestReport;
import edu.wustl.cil.SMM.XDS.BaseObject;
import edu.wustl.cil.SMM.XDS.ExtrinsicObject;
import edu.wustl.cil.SMM.XDS.Factory;
import edu.wustl.cil.SMM.XDS.RegistryPackage;

import java.io.File;
//import java.lang.reflect.Field;
import java.util.List;

import static edu.wustl.cil.SMM.Reporting.LineItem.ItemType.*;

public class TestStepCompareMetadataFixed extends TestStep {

    public TestStepCompareMetadataFixed(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
    }

    @Override
    public void executeStep() throws Exception {
        printStepMetadata("\nExecute: CompareMetadataFixed");

        File parentFolder = (new File(testPath).getParentFile());
        File fieldNamesValuesFile = new File(parentFolder.getAbsolutePath() + "/" + testStepBean.getFieldNamesValues());
        List<FieldValidationItem> fieldValidationItems = Utility.readFieldValidationItems(fieldNamesValuesFile);

        String pathToCSVFile = Utility.expandVariable(this, testStepBean.getInputToTest());
        RegistryPackage registryPackage = factory.deserializeRegistryPackage(pathToCSVFile);
        for (FieldValidationItem fieldValidationItem: fieldValidationItems) {
            String label          = fieldValidationItem.getLabel();
//            String referenceValue = fieldValidationItem.getReferenceValue();
//            String optionality    = fieldValidationItem.getOptionality();
//            String valueToTest    = registryPackage.getValue(label);

            executeComparison(fieldValidationItem, registryPackage);
        }
    }

    private String pathSubstitution(String input) {
        if (input.startsWith("$OUTPUT")) {
            input = outputPath + input.substring(7);
        }
        return input;
    }

    private void executeComparison(FieldValidationItem fieldValidationItem, BaseObject baseObject) throws Exception {
        String label = fieldValidationItem.getLabel();
        String referenceValue = fieldValidationItem.getReferenceValue();
        String valueToTest    = baseObject.getValue(label);

        String optionality    = fieldValidationItem.getOptionality();
        String severity       = fieldValidationItem.getSeverity();
        String comparisonOp   = fieldValidationItem.getComparisonOperator();
        String docReference   = fieldValidationItem.getDocReference();
        String comments       = fieldValidationItem.getComments();

        boolean flag = doStringsMatch(referenceValue, valueToTest, comparisonOp);

        LineItem.ItemType itemType;
        String assertion = "Fixed value test, " + label + " operator = " + comparisonOp + ", optionality = " + optionality;
        String context = fieldValidationItem.getxPath();
        String message;

        if (optionality.equals("O") && (valueToTest == null || valueToTest.isEmpty())) {
            itemType = WARNING;
            message = "Optional value not included in tested object.";
        } else if (flag) {
            itemType = SUCCESS;
            message = "Successful comparison.";
        } else {
            // If we reach here, we know the value retrieved did not match the reference value
            // and requires either a warning or error message.
            itemType = LineItem.translate(severity);
            message = "Comparison failed.";
        }
        testReport.addLineItem(itemType, label, assertion, context, message, referenceValue, valueToTest, docReference, comments);
    }
/*
    private boolean doStringsMatch(String referenceValue, String valueToTest, String comparisonOperator) throws Exception {
        if (valueToTest == null) {
            return false;
        }
        boolean flag = true;
        switch (comparisonOperator) {
            case "EQ":
                flag = valueToTest.equals(referenceValue);
                break;
            case "EQCase":
                flag = valueToTest.equalsIgnoreCase(referenceValue);
                break;
            case "NE":
                flag = ! valueToTest.equals(referenceValue);
                break;
            case "STARTS":
                flag = valueToTest.startsWith(referenceValue);
                break;
            case "CONTAINS":
                flag = valueToTest.contains(referenceValue);
                break;
            case "NOTNULL":
                flag = ! valueToTest.isEmpty();
                break;
            default:
                throw new Exception("Unrecognized comparison operator: " + comparisonOperator + ", Reference Value: " + referenceValue);
        }
        return flag;
    }

 */
}
