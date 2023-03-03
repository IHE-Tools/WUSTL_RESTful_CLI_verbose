package edu.wustl.cil.SMM.jackson;

import edu.wustl.cil.SMM.Reporting.LineItem;
import edu.wustl.cil.SMM.Reporting.TestReport;
import edu.wustl.cil.SMM.XDS.BaseObject;
import edu.wustl.cil.SMM.XDS.RegistryPackage;
import org.w3c.dom.Node;

import java.io.File;
import java.util.List;

import static edu.wustl.cil.SMM.Reporting.LineItem.ItemType.*;

//import java.lang.reflect.Field;

public class TestStepSubmissionSetConformance extends TestStep {

    public TestStepSubmissionSetConformance(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
    }

    public TestStepSubmissionSetConformance() {
        super();
    }

    @Override
    public void executeStep() throws Exception {
        printStepMetadata("\nExecute: CompareMetadataFixed");

        File parentFolder = (new File(testPath).getParentFile());
        File conformanceRequirementsFile = new File(parentFolder.getAbsolutePath() + "/" + testStepBean.getConformanceRequirements());
        String fileExtractPath = testStepBean.getInputFolder() + "/file_extract.xml";

        List<FieldValidationItem> fieldValidationItems = Utility.readFieldValidationItems(conformanceRequirementsFile);
        FileExtract fileExtract = (FileExtract) Utility.readXML(Utility.expandVariable(this, fileExtractPath), FileExtract.class);
        String metadataPath = fileExtract.getMetadata();
        Node registryPackageNode = edu.wustl.cil.SMM.XDS.Factory.getRegistryPackage(metadataPath);

        for (FieldValidationItem fieldValidationItem: fieldValidationItems) {
//            String label = fieldValidationItem.getLabel();
//            String xPath = fieldValidationItem.getXPath();

            executeComparison(fieldValidationItem, registryPackageNode);
        }
    }

    private void executeComparison(FieldValidationItem fieldValidationItem, Node registryObjectNode) throws Exception {
        String label = fieldValidationItem.getLabel();
        String referenceValue = fieldValidationItem.getReferenceValue();
        String submittedValue;

        String optionality    = fieldValidationItem.getOptionality();
        String severity       = fieldValidationItem.getSeverity();
        String comparisonOp   = fieldValidationItem.getComparisonOperator();
        String docReference   = fieldValidationItem.getDocReference();
        String comments       = fieldValidationItem.getComments();

        LineItem.ItemType itemType = ERROR;
        String assertion = "Fixed value test, " + label + " operator = " + comparisonOp + ", optionality = " + optionality;
        String context = fieldValidationItem.getxPath();
        String message;

        try {
            if (fieldValidationItem.getIteratedOperation().equals("1")) {
                submittedValue = Utility.evaluateXPathFindElementRegex(registryObjectNode, fieldValidationItem.getxPath(), referenceValue);
            } else {
                submittedValue = Utility.evaluateXPathToString(registryObjectNode, fieldValidationItem.getxPath());
            }

            boolean flag = doStringsMatch(referenceValue, submittedValue, comparisonOp);



            if (optionality.equals("O") && (submittedValue == null || submittedValue.isEmpty())) {
                itemType = WARNING;
                message = "Submitted object does not contain value with optionality O.";
            } else if (optionality.equals("R2") && (submittedValue == null || submittedValue.isEmpty())) {
                itemType = WARNING;
                message = "Submitted object does not contain value with optionality R2.";
            } else if (flag) {
                itemType = SUCCESS;
                message = "Successful comparison.";
            } else {
                // If we reach here, we know the value retrieved did not match the reference value
                // and requires either a warning or error message.
                itemType = LineItem.translate(severity);
                message = "Comparison failed.";
            }
        } catch (Exception e) {
            System.out.println("Ran into an exception when processing: " + fieldValidationItem.toString());
            throw e;
        }
        testReport.addLineItem(itemType, label, assertion, context, message, referenceValue, submittedValue, docReference, comments);
    }

}
