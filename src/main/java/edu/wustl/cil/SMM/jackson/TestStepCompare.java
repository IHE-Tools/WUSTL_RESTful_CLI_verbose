package edu.wustl.cil.SMM.jackson;

import edu.wustl.cil.SMM.Reporting.TestReport;

public class TestStepCompare extends TestStep {

    public TestStepCompare(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
    }

    @Override
    public void executeStep() throws Exception {
//        System.out.println("\nExecute: Compare");
        printStepMetadata("\nExecute: Compare");
    }
}
