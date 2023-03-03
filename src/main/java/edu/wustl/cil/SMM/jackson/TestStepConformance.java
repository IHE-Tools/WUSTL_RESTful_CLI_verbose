package edu.wustl.cil.SMM.jackson;

import edu.wustl.cil.SMM.Reporting.LineItem;
import edu.wustl.cil.SMM.Reporting.TestReport;
import org.w3c.dom.Node;

import java.io.File;
import java.util.List;

import static edu.wustl.cil.SMM.Reporting.LineItem.ItemType.*;

public class TestStepConformance extends TestStep {
    private String source = "";
    private String fileExtractPath = "";
    private String selector = "";
    private String mimeType = "";

    public TestStepConformance(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
        stepDescription = "Execute: Conformance";
    }

    public TestStepConformance() {
        super();
        stepDescription = "Execute: Conformance";
    }



    @Override
    public void executeStep() throws Exception {
        printStepMetadata("\n" + stepDescription);

        File parentFolder = (new File(testPath).getParentFile());
        File conformanceRequirementsFile = new File(parentFolder.getAbsolutePath() + "/" + testStepBean.getConformanceRequirements());
        List<FieldConformanceItem> fieldConformanceItems = Utility.readFieldConformanceItems(conformanceRequirementsFile);

        int index = 0;
        for (FieldConformanceItem fieldConformanceItem: fieldConformanceItems) {
            if (!executeComparison(fieldConformanceItem)) {
                break;
            };
            index++;
        }
    }

    protected Node getNodeForConformanceTest(FieldConformanceItem fieldConformanceItem) throws Exception {
        Node node = null;

        String selector = fieldConformanceItem.getSelector();
        String mimeType = fieldConformanceItem.getMimeType();
        final boolean isHL7V2 = mimeType.equals("x-application/hl7-v2+er7");
        final boolean isCDA   = mimeType.equals("text/xml");

        try {
            String fileExtractPath = testStepBean.getInputFolder() + "/file_extract.xml";
            FileExtract fileExtract = (FileExtract) Utility.readXML(Utility.expandVariable(this, fileExtractPath), FileExtract.class);
            String metadataPath = fileExtract.getMetadata();

            switch (selector) {
                case "submissionSet":
                    node = edu.wustl.cil.SMM.XDS.Factory.getRegistryPackage(metadataPath);
                    break;
                case "documentEntry":
                    node = edu.wustl.cil.SMM.XDS.Factory.getExtrinsicObject(metadataPath, mimeType);
                    break;
                case "document":
                    if (isHL7V2) {
                        String hl7V2Path = edu.wustl.cil.SMM.XDS.Factory.getExtrinsicObjectFilePath(metadataPath, mimeType);
                        node = UtilityHL7.transformHL7V2FileToNode(hl7V2Path);
                    } else if (isCDA) {

                    } else {

                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            testReport.addLineItem(ERROR, fieldConformanceItem.getLabel(),
                    "", "", "Exception occurred when trying to determine the XML Node for a conformance test",
                    fieldConformanceItem.getReferenceValue(), "",
                    fieldConformanceItem.getDocReference(), fieldConformanceItem.getComments());
            node = null;
        }
        return node;
    }


    protected boolean executeComparison(FieldConformanceItem fieldConformanceItem) throws Exception {

        String label = fieldConformanceItem.getLabel();
        String referenceValue = fieldConformanceItem.getReferenceValue();

        LineItem.ItemType itemType = ERROR;

        String docReference   = fieldConformanceItem.getDocReference();
        String comments       = fieldConformanceItem.getComments();
        String optionality    = fieldConformanceItem.getOptionality();
        String severity       = fieldConformanceItem.getSeverity();
        String comparisonOp   = fieldConformanceItem.getComparisonOperator();

        String assertion      = "Conformance test, " + label + " operator = " + comparisonOp + ", optionality = " + optionality;
        String context        = fieldConformanceItem.getxPath();
        String message        = "";

        if (optionality.equalsIgnoreCase("ABORT")) {
            return false;
        }

        Node node = getNodeForConformanceTest(fieldConformanceItem);
        String submittedValue = Utility.evaluateXPathToString(node, fieldConformanceItem.getxPath()).replace('\n', ' ').trim();
        if (submittedValue == null) {
            String x = fieldConformanceItem.getxPath();
            String z = "foo";
        }
        boolean flag = doStringsMatch(referenceValue, submittedValue, comparisonOp);

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
        return true;
    }
}
