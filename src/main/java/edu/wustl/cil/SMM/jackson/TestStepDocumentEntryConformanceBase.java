package edu.wustl.cil.SMM.jackson;

import edu.wustl.cil.SMM.Reporting.LineItem;
import edu.wustl.cil.SMM.Reporting.TestReport;
import org.w3c.dom.Node;

import java.io.File;
import java.util.List;

import static edu.wustl.cil.SMM.Reporting.LineItem.ItemType.*;

public class TestStepDocumentEntryConformanceBase extends TestStep {
    protected String conformanceStepDescription = "TestStepDocumentEntryConformanceBase. If you see this in the output, something went wrong.";

    public TestStepDocumentEntryConformanceBase(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
    }

    @Override
    public void executeStep() throws Exception {
        printStepMetadata("\n" + conformanceStepDescription);

        File parentFolder = (new File(testPath).getParentFile());
        File conformanceRequirementsFile = new File(parentFolder.getAbsolutePath() + "/" + testStepBean.getConformanceRequirements());
        String fileExtractPath = testStepBean.getInputFolder() + "/file_extract.xml";

        List<FieldValidationItem> fieldValidationItems = Utility.readFieldValidationItems(conformanceRequirementsFile);
        FileExtract fileExtract = (FileExtract) Utility.readXML(Utility.expandVariable(this, fileExtractPath), FileExtract.class);
        String metadataPath = fileExtract.getMetadata();
        Node extrinsicObject = this.getDocumentEntryNode(metadataPath);

        for (FieldValidationItem fieldValidationItem: fieldValidationItems) {
            executeComparison(fieldValidationItem, extrinsicObject);
        }
    }

    protected void executeComparison(FieldValidationItem fieldValidationItem, Node extrinsicObject) throws Exception {
        String label = fieldValidationItem.getLabel();
        String referenceValue = fieldValidationItem.getReferenceValue();
        String submittedValue = Utility.evaluateXPathToString(extrinsicObject, fieldValidationItem.getxPath()).replace('\n', ' ').trim();

        String optionality    = fieldValidationItem.getOptionality();
        String severity       = fieldValidationItem.getSeverity();
        String comparisonOp   = fieldValidationItem.getComparisonOperator();
        String docReference   = fieldValidationItem.getDocReference();
        String comments       = fieldValidationItem.getComments();

        boolean flag = doStringsMatch(referenceValue, submittedValue, comparisonOp);

        LineItem.ItemType itemType = ERROR;
        String assertion = "Fixed value test, " + label + " operator = " + comparisonOp + ", optionality = " + optionality;
        String context = fieldValidationItem.getxPath();
        String message;

        if (optionality.equals("O") && (submittedValue == null || submittedValue.isEmpty())) {
            itemType = WARNING;
            message = "Optional value not included in submitted object.";
        } else if (flag) {
            itemType = SUCCESS;
            message = "Successful comparison.";
        } else {
            // If we reach here, we know the value retrieved did not match the reference value
            // and requires either a warning or error message.
            itemType = LineItem.translate(severity);
            message = "Comparison failed.";
        }
        testReport.addLineItem(itemType, label, assertion, context, message, referenceValue, submittedValue, docReference, comments);
    }

    protected Node getDocumentEntryNode(String metadataPath) throws Exception {
        throw new Exception("There is a coding error that landed you in the TestStepDocumentEntryConformanceBase::getDocumentEntryNode method. The software should only execute this method in derived classes");
    }
}
