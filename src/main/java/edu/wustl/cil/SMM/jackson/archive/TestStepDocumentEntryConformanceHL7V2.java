package edu.wustl.cil.SMM.jackson.archive;

import edu.wustl.cil.SMM.Reporting.LineItem;
import edu.wustl.cil.SMM.Reporting.TestReport;
import edu.wustl.cil.SMM.jackson.TestStepBean;
import edu.wustl.cil.SMM.jackson.TestStepDocumentEntryConformanceBase;
import org.w3c.dom.Node;

import java.io.File;
import java.util.List;

import static edu.wustl.cil.SMM.Reporting.LineItem.ItemType.*;

public class TestStepDocumentEntryConformanceHL7V2 extends TestStepDocumentEntryConformanceBase {

    public TestStepDocumentEntryConformanceHL7V2(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
        conformanceStepDescription = "Execute: DocumentEntry Metadata Conformance HL7V2";
    }

    @Override
    protected Node getDocumentEntryNode(String metadataPath) throws Exception {
        return edu.wustl.cil.SMM.XDS.Factory.getExtrinsicObject(metadataPath, "x-application/hl7-v2+er7");
    }

}
