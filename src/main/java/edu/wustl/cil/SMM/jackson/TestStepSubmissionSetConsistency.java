package edu.wustl.cil.SMM.jackson;

import edu.wustl.cil.SMM.Reporting.TestReport;

public class TestStepSubmissionSetConsistency extends TestStep {

    public TestStepSubmissionSetConsistency(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
    }

    public TestStepSubmissionSetConsistency() {
        super();
    }

    @Override
    public void executeStep() throws Exception {
        printStepMetadata("\nExecute: MetadataConsistency");
    }
}
