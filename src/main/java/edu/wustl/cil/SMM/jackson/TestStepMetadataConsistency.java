package edu.wustl.cil.SMM.jackson;

import edu.wustl.cil.SMM.Reporting.TestReport;

public class TestStepMetadataConsistency extends TestStep {

    public TestStepMetadataConsistency(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
    }

    @Override
    public void executeStep() throws Exception {
        printStepMetadata("\nExecute: MetadataConsistency");
    }
}
