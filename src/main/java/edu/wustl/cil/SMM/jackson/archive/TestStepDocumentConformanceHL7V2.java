package edu.wustl.cil.SMM.jackson.archive;

import edu.wustl.cil.SMM.Reporting.TestReport;
import edu.wustl.cil.SMM.jackson.TestStepBean;
import edu.wustl.cil.SMM.jackson.TestStepDocumentEntryConformanceBase;
import edu.wustl.cil.SMM.jackson.UtilityHL7;
import org.w3c.dom.Node;

public class TestStepDocumentConformanceHL7V2 extends TestStepDocumentEntryConformanceBase {

    public TestStepDocumentConformanceHL7V2(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
        conformanceStepDescription = "Execute: Document Conformance HL7V2";
    }

    @Override
    protected Node getDocumentEntryNode(String metadataPath) throws Exception {
        String hl7V2Path = edu.wustl.cil.SMM.XDS.Factory.getExtrinsicObjectFilePath(metadataPath, "x-application/hl7-v2+er7");
        Node n = UtilityHL7.transformHL7V2FileToNode(hl7V2Path);
        return n;
//        return edu.wustl.cil.SMM.XDS.Factory.getExtrinsicObject(metadataPath, "x-application/hl7-v2+er7");
    }

}
