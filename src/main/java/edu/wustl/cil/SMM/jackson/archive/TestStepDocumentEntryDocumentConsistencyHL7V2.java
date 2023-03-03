package edu.wustl.cil.SMM.jackson.archive;

import edu.wustl.cil.SMM.Reporting.LineItem;
import edu.wustl.cil.SMM.Reporting.TestReport;
import edu.wustl.cil.SMM.jackson.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.util.List;

import static edu.wustl.cil.SMM.Reporting.LineItem.ItemType.*;

public class TestStepDocumentEntryDocumentConsistencyHL7V2 extends TestStep {

    public TestStepDocumentEntryDocumentConsistencyHL7V2(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
    }

    public void executeStep() throws Exception {
        printStepMetadata("\nExecute: DocumentEntry Metadata Document Consistency HL7V2");

        File parentFolder = (new File(testPath).getParentFile());
        File consistencyRequirementsFile = new File(parentFolder.getAbsolutePath() + "/" + testStepBean.getConsistencyRequirements());
        String fileExtractPath = testStepBean.getInputFolder() + "/file_extract.xml";

        List<FieldConsistencyItem> fieldConsistencyItems = Utility.readFieldConsistencyItems((consistencyRequirementsFile));
        FileExtract fileExtract = (FileExtract) Utility.readXML(Utility.expandVariable(this, fileExtractPath), FileExtract.class);
        String metadataPath = fileExtract.getMetadata();
        DocumentEntry documentEntry = fileExtract.getDocumentEntry("x-application/hl7-v2+er7");
        Node extrinsicObject = edu.wustl.cil.SMM.XDS.Factory.getExtrinsicObject(metadataPath, "x-application/hl7-v2+er7");
        if (extrinsicObject == null) {
            throw new Exception ("Did not find an HL7 V2 file");
        }
        Node hl7Node = UtilityHL7.transformHL7V2FileToNode(documentEntry.getDocumentPath());

        for (FieldConsistencyItem fieldConsistencyItem: fieldConsistencyItems) {
            String label = fieldConsistencyItem.getLabel();
            executeComparison(fieldConsistencyItem, extrinsicObject, hl7Node);
        }
    }

    protected void executeComparison(FieldConsistencyItem fieldConsistencyItem, Node extrinsicObject, Node hl7V2Node) throws Exception {
        throw new Exception("Deprecated");
        /*
        String label = fieldConsistencyItem.getLabel();
        String referenceValue = fieldConsistencyItem.getReferenceValue();
        String value1         = Utility.evaluateXPathToString(hl7V2Node,       fieldConsistencyItem.getxPathValue1()).replace('\n', ' ').trim();
        String value2         = Utility.evaluateXPathToString(extrinsicObject, fieldConsistencyItem.getxPathValue2()).replace('\n', ' ').trim();

        String optionality    = fieldConsistencyItem.getOptionality();
        String severity       = fieldConsistencyItem.getSeverity();
        String comparisonOp   = fieldConsistencyItem.getComparisonOperator();
        String docReference   = fieldConsistencyItem.getDocReference();
        String comments       = fieldConsistencyItem.getComments();

        boolean flag = doStringsMatch(value1, value2, comparisonOp);

        LineItem.ItemType itemType = ERROR;
        String assertion = "Consistency test, " + label + " operator = " + comparisonOp + ", optionality = " + optionality;
        String context = fieldConsistencyItem.getxPathValue1() + "  " + fieldConsistencyItem.getxPathValue2();
        String message;

        if (optionality.equals("O") && (value2 == null || value2.isEmpty())) {
            itemType = WARNING;
            message = "Optional value in metadata is not included (missing or empty).";
        } else if (flag) {
            itemType = SUCCESS;
            message = "Successful comparison.";
        } else {
            // If we reach here, we know the value retrieved did not match the reference value
            // and requires either a warning or error message.
            itemType = LineItem.translate(severity);
            message = "Comparison failed.";
        }
        testReport.addLineItem(itemType, label, assertion, context, message, value1, value2, docReference, comments);

         */
    }
}
