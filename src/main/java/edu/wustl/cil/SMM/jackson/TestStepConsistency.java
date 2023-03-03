package edu.wustl.cil.SMM.jackson;

import edu.wustl.cil.SMM.Reporting.LineItem;
import edu.wustl.cil.SMM.Reporting.TestReport;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;

import static edu.wustl.cil.SMM.Reporting.LineItem.ItemType.*;

public class TestStepConsistency extends TestStep {
    private String source = "";
    private String fileExtractPath = "";
    private String selector = "";
    private String mimeType = "";

    public TestStepConsistency(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
        stepDescription = "Execute: Consistency";
    }

    public TestStepConsistency() {
        super();
        stepDescription = "Execute: Consistency";
    }

    @Override
    public void executeStep() throws Exception {
        printStepMetadata("\n" + stepDescription);

        File parentFolder = (new File(testPath).getParentFile());
        File consistencyRequirementsFile = new File(parentFolder.getAbsolutePath() + "/" + testStepBean.getConsistencyRequirements());
        List<FieldConsistencyItem> fieldConsistencyItems = Utility.readFieldConsistencyItems(consistencyRequirementsFile);

        for (FieldConsistencyItem fieldConsistencyItem: fieldConsistencyItems) {
            executeComparison(fieldConsistencyItem);
        }
    }

    protected String getStringFromFieldItem(FieldConsistencyItem fieldConsistencyItem, String selector, String mimeType, String fieldExpression) throws Exception {
        URI expressionURI;
        String scheme = "";
        String path;
        try {
            expressionURI = new URI(fieldExpression);
            scheme = expressionURI.getScheme();
            if (scheme == null) {
                scheme = "";
                path = fieldExpression;
            } else {
                path = expressionURI.getSchemeSpecificPart().substring(2);
            }
        } catch (Exception e) {
            // If there is no scheme, we just set the path to the full fieldExpression
            scheme = "";
            path = fieldExpression;
        }

        String rtn = "";
        try {
            String fileExtractPath = testStepBean.getInputFolder() + "/file_extract.xml";
            FileExtract fileExtract = (FileExtract) Utility.readXML(Utility.expandVariable(this, fileExtractPath), FileExtract.class);
            String metadataPath = fileExtract.getMetadata();

            if (scheme.equals("HL7V2") && selector.equals("document")) {
                String hl7V2Path = edu.wustl.cil.SMM.XDS.Factory.getExtrinsicObjectFilePath(metadataPath, mimeType);
                rtn = UtilityHL7.getField(hl7V2Path, path);
            } else if (scheme.equals("HL7V2")) {
                throw new Exception("HL7V2 scheme but selector is not document: " + selector);

            } else {
                Node node = getNodeForTest(fieldConsistencyItem, selector, mimeType);
                rtn = Utility.evaluateXPathToString(node, path);
            }
        } catch (Exception e) {

        }

        return rtn;
    }

    protected Node getNodeForTest(FieldConsistencyItem fieldConsistencyItem, String selector, String mimeType, String nodeName) throws Exception {
        Node node = getNodeForTest(fieldConsistencyItem, selector, mimeType);
        while (! nodeName.equals(node.getNodeName())) {
            node = node.getNextSibling();

            if (node == null) {
                throw new Exception("Was not able to find a node with name: " + nodeName);
            }
        }
        return node;

    }

    protected Node getNodeForTest(FieldConsistencyItem fieldConsistencyItem, String selector, String mimeType) throws Exception {
        Node node = null;

//        String selector = fieldConformanceItem.getSelector();
//        String mimeType = fieldConformanceItem.getMimeType();
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
                        String cdaPath = edu.wustl.cil.SMM.XDS.Factory.getExtrinsicObjectFilePath(metadataPath, mimeType);
                        Document document = edu.wustl.cil.SMM.XDS.Utility.parseDocument(cdaPath);
                        node = document.getFirstChild();
                        while ( ! node.getNodeName().equals("ClinicalDocument")) {
                            node = node.getNextSibling();
                            if (node == null) {
                                throw new Exception("In what we think is a CDA, could not find node 'ClinicalDocument' " + cdaPath);
                            }
                        }
                    } else {

                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            testReport.addLineItem(ERROR, fieldConsistencyItem.getLabel(),
                    "", fieldConsistencyItem.getLabel(),
                    "Exception occurred when trying to determine the XML Node for a consistency test " + selector + " " + mimeType,
                    "", "",
                    fieldConsistencyItem.getDocReference(), fieldConsistencyItem.getComments());
            node = null;
        }
        return node;
    }


    protected void executeComparison(FieldConsistencyItem fieldConsistencyItem) throws Exception {

        String label = fieldConsistencyItem.getLabel();

        LineItem.ItemType itemType = ERROR;

        String docReference   = fieldConsistencyItem.getDocReference();
        String comments       = fieldConsistencyItem.getComments();
        String optionality    = fieldConsistencyItem.getOptionality();
        String severity       = fieldConsistencyItem.getSeverity();
        String comparisonOp   = fieldConsistencyItem.getComparisonOperator();

        String assertion      = "Conformance test, " + label + " operator = " + comparisonOp + ", optionality = " + optionality;

        String context        =
                fieldConsistencyItem.getInitial()        + " /" +
                fieldConsistencyItem.getInitialSelector() + "/" +
                fieldConsistencyItem.getInitialMimeType() + "/" +
                fieldConsistencyItem.getInitialXPath() + "    " +
                fieldConsistencyItem.getDerived()         + "/" +
                fieldConsistencyItem.getDerivedSelector() + "/" +
                fieldConsistencyItem.getDerivedMimeType() + "/" +
                fieldConsistencyItem.getDerivedXPath();

        String message   = "";
        String initialString = getStringFromFieldItem(fieldConsistencyItem,
                fieldConsistencyItem.getInitialSelector(),
                fieldConsistencyItem.getInitialMimeType(),
                fieldConsistencyItem.getInitialXPath().replace('\n', ' ').trim());
        String derivedString = getStringFromFieldItem(fieldConsistencyItem,
                fieldConsistencyItem.getDerivedSelector(),
                fieldConsistencyItem.getDerivedMimeType(),
                fieldConsistencyItem.getDerivedXPath().replace('\n', ' ').trim());

/*
        String foo = getStringFromFieldItem(fieldConsistencyItem, fieldConsistencyItem.getDerivedSelector(), fieldConsistencyItem.getDerivedMimeType(),
                fieldConsistencyItem.getDerivedXPath().replace('\n', ' ').trim());

        Node initialNode = getNodeForTest(fieldConsistencyItem, fieldConsistencyItem.getInitialSelector(), fieldConsistencyItem.getInitialMimeType());
        Node derivedNode = getNodeForTest(fieldConsistencyItem, fieldConsistencyItem.getDerivedSelector(), fieldConsistencyItem.getDerivedMimeType());

        String initialString = Utility.evaluateXPathToString(initialNode, fieldConsistencyItem.getInitialXPath().replace('\n', ' ').trim());
        String derivedString = Utility.evaluateXPathToString(derivedNode, fieldConsistencyItem.getDerivedXPath().replace('\n', ' ').trim());
*/





        boolean flag = doStringsMatch(initialString, derivedString, comparisonOp);
        if (optionality.equals("O") && (derivedString == null || derivedString.isEmpty())) {
            itemType = WARNING;
            message = "Optional value not included in derived object.";
        } else if (flag) {
            itemType = SUCCESS;
            message = "Successful comparison.";
        } else {
            // If we reach here, we know the value retrieved did not match the reference value
            // and requires either a warning or error message.
            itemType = LineItem.translate(severity);
            message = "Comparison failed.";
        }
        testReport.addLineItem(itemType, label, assertion, context, message, initialString, derivedString, docReference, comments);
    }
}
