package edu.wustl.cil.SMM.jackson.archive;

import edu.wustl.cil.SMM.Reporting.TestReport;
import edu.wustl.cil.SMM.jackson.TestStepBean;
import edu.wustl.cil.SMM.jackson.TestStepDocumentEntryConformanceBase;
import org.w3c.dom.Node;

public class TestStepDocumentEntryConformanceCDA extends TestStepDocumentEntryConformanceBase {

    public TestStepDocumentEntryConformanceCDA(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
        conformanceStepDescription = "Execute: DocumentEntry Metadata Conformance HL7V2";
    }

    @Override
    protected Node getDocumentEntryNode(String metadataPath) throws Exception {
        return edu.wustl.cil.SMM.XDS.Factory.getExtrinsicObject(metadataPath, "text/xml");
    }

}
