package edu.wustl.cil.SMM.jackson;

import edu.wustl.cil.SMM.Reporting.LineItem;
import edu.wustl.cil.SMM.Reporting.TestReport;
import org.w3c.dom.Node;

import java.io.File;
import java.util.List;

import static edu.wustl.cil.SMM.Reporting.LineItem.ItemType.*;

//import java.lang.reflect.Field;

public class TestStepDocumentEntryLinkage extends TestStep {

    public TestStepDocumentEntryLinkage(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
    }

    @Override
    public void executeStep() throws Exception {
        printStepMetadata("\nExecute: DocumentEntry-Linkage");

        File parentFolder = (new File(testPath).getParentFile());
        File conformanceRequirementsFile = new File(parentFolder.getAbsolutePath() + "/" + testStepBean.getConformanceRequirements());
        List<FieldValidationItem> fieldValidationItems = Utility.readFieldValidationItems(conformanceRequirementsFile);

        String p = testStepBean.getReferenceFolder() + "/file_extract.xml";
        String m = testStepBean.getMimeTypeReference();
        Node documentEntryReference = getDocumentEntryFromFileExtract(p,m);
        Node documentEntrySubmitted = getDocumentEntryFromFileExtract(testStepBean.getSubmittedFolder() + "/file_extract.xml",
                testStepBean.getMimeTypeToTest());

        for (FieldValidationItem fieldValidationItem: fieldValidationItems) {
//            String label = fieldValidationItem.getLabel();
//            String xPath = fieldValidationItem.getXPath();

            executeComparison(fieldValidationItem, documentEntryReference, documentEntrySubmitted);
        }
    }


    private void executeComparison(FieldValidationItem fieldValidationItem, Node documentEntryReference, Node documentEntrySubmitted) throws Exception {
        String label = fieldValidationItem.getLabel();
        String referenceValue = Utility.evaluateXPathToString(documentEntryReference, fieldValidationItem.getxPath());
        String submittedValue = Utility.evaluateXPathToString(documentEntrySubmitted, fieldValidationItem.getxPath());
        String optionality    = fieldValidationItem.getOptionality();
        String severity       = fieldValidationItem.getSeverity();
        String comparisonOp   = fieldValidationItem.getComparisonOperator();
        String docReference   = fieldValidationItem.getDocReference();
        String comments       = fieldValidationItem.getComments();
        String assertion = "DocumentEntry metadata comparison, " + label + " operator = " + comparisonOp + ", optionality = " + optionality;

        if (fieldValidationItem.getIteratedOperation().equals("1")) {
            referenceValue = Utility.evaluateXPathFindOneElement(documentEntryReference, fieldValidationItem.getxPath(), fieldValidationItem.getReferenceValue());
            submittedValue = Utility.evaluateXPathFindOneElement(documentEntrySubmitted, fieldValidationItem.getxPath(), fieldValidationItem.getReferenceValue());
            assertion      = "DocumentEntry metadata list element operation, " + label + ", operator = " + comparisonOp + ", optionality = " + optionality + ", value prefix = " + fieldValidationItem.getReferenceValue();
        } else {
            referenceValue = Utility.evaluateXPathToString(documentEntryReference, fieldValidationItem.getxPath());
            submittedValue = Utility.evaluateXPathToString(documentEntrySubmitted, fieldValidationItem.getxPath());
        }

        System.out.println("Reference:\n" + referenceValue);
        System.out.println("Submitted:\n" + submittedValue);

        boolean flag = doStringsMatch(referenceValue, submittedValue, comparisonOp);

        LineItem.ItemType itemType = ERROR;
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
}
