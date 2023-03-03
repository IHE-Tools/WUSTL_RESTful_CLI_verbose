package edu.wustl.cil.SMM.jackson;

import edu.wustl.cil.SMM.Reporting.ReportingUtility;
import edu.wustl.cil.SMM.Reporting.TestReport;
import edu.wustl.cil.SMM.jackson.archive.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.wustl.cil.SMM.jackson.TestStepBean.Action.*;

public class LinkedTransactionTestEngine {
    private String testPath = null;
    private String inputPath = null;
    private String explodedInputPath = null;
    private String outputPath = null;
    private TestBean testBean = null;
    private TestReport testReport = null;

    public static void main(String[] args) {
        checkArgs(args);

        try {
            LinkedTransactionTestEngine testEngine = new LinkedTransactionTestEngine();

            testEngine.initialize(args[0], args[1], args[2]);
            testEngine.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkArgs(String[] args) {
        if (args.length != 3) {
            System.err.println("This module expects exactly 3 arguments; you supplied: " + args.length);
            printUsageAndDie();
        }
        File f = new File(args[0]);
        if (! f.exists()) {
            System.err.println("The rules file specified does not exist: " + args[0]);
            printUsageAndDie();
        }

        f = new File(args[1]);
        if (! f.isDirectory()) {
            System.err.println("The input folder specified does not exist: " + args[1]);
            printUsageAndDie();
        }
    }

    public static void printUsageAndDie() {
        System.err.println(
          "Arguments: <XML Test Specification> <Input Folder> <Output Folder>"
        );
        System.exit(1);
    }
    public LinkedTransactionTestEngine() {

    }

    public void initialize(String testPath, String inputPath, String outputPath) throws Exception {
        this.testPath = testPath;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.testBean = TestBean.readTestBean(this.testPath);
        this.testReport = new TestReport();

        new File(outputPath).mkdirs();
    }

    public void initializeExplodedVersion(String testPath, String explodedInputPath, String outputPath) throws Exception {
        this.testPath = testPath;
        this.explodedInputPath = explodedInputPath;
        this.outputPath = outputPath;
        this.testBean = TestBean.readTestBean(this.testPath);
        this.testReport = new TestReport();

        new File(outputPath).mkdirs();
    }

    public void execute() throws Exception {
        List<TestStepBean> testStepBeans = testBean.getTestStepBeanArrayList();
        for (TestStepBean bean: testStepBeans) {
            TestStep step = createTestStep(bean);
            step.executeStep();
        }
        writeTestReport();
    }

    public TestStep createTestStep(TestStepBean bean) throws Exception {
        TestStepBean.Action action = TestStepBean.parseAction(bean.getAction());

        TestStep step = null;

        Map<TestStepBean.Action, Class<? extends TestStep>> stepMap = new HashMap<>();
        stepMap.put(COMPARE, TestStepCompare.class);
        stepMap.put(EXTRACT_XDM, TestStepExtractXDM.class);
        stepMap.put(CONFORMANCE, TestStepConformance.class);
        stepMap.put(CONSISTENCY,  TestStepConsistency.class);

        Class c = stepMap.get(action);
        if (c != null) {
            step = (TestStep)(c.newInstance());
            step.initialize(bean,testPath,inputPath,outputPath,testReport);
            return step;
        }

        switch (action) {
            case COMPARE:
                step = new TestStepCompare(bean, testPath, inputPath, outputPath, testReport);
                break;
            case COMPARE_METADATA:
                step = new TestStepCompareMetadata(bean, testPath, inputPath, outputPath, testReport);
                break;
            case COMPARE_METADATA_FIXED:
                step = new TestStepCompareMetadataFixed(bean, testPath, inputPath, outputPath, testReport);
                break;
            case EXTRACT_360X_SUBMISSION:
                step = new TestStepExtract360XSubmission(bean, testPath, inputPath, outputPath, testReport);
                break;
            case EXTRACT_XDM:
                step = new TestStepExtractXDM(bean, testPath, inputPath, outputPath, testReport);
                break;
            case EXTRACT_XDR:
                step = new TestStepExtractXDR(bean, testPath, inputPath, outputPath, testReport);
                break;
            case INDEX_EXPLODED_FILES:
                step = new TestStepIndexExplodedFiles(bean, testPath, explodedInputPath, outputPath, testReport);
                break;
            case METADATA_CONSISTENCY:
                step = new TestStepMetadataConsistency(bean, testPath, inputPath, outputPath, testReport);
                break;
            case SUBMISSION_SET_CONSISTENCY:
                step= new TestStepSubmissionSetConsistency(bean, testPath, inputPath, outputPath, testReport);
                break;
            case SUBMISSION_SET_CONFORMANCE:
                step = new TestStepSubmissionSetConformance(bean, testPath, inputPath, outputPath, testReport);
                break;
            case DOCUMENT_ENTRY_LINKAGE:
                step = new TestStepDocumentEntryLinkage(bean, testPath, inputPath, outputPath, testReport);
                break;
            case DOCUMENT_ENTRY_CONFORMANCE_HL7V2:
                step = new TestStepDocumentEntryConformanceHL7V2(bean, testPath, inputPath, outputPath, testReport);
                break;
            case DOCUMENT_CONFORMANCE_HL7V2:
                step = new TestStepDocumentConformanceHL7V2(bean, testPath, inputPath, outputPath, testReport);
                break;
            case DOC_ENTRY_DOCUMENT_CONSISTENCY_HL7V2:
                step = new TestStepDocumentEntryDocumentConsistencyHL7V2(bean, testPath, inputPath, outputPath, testReport);
                break;
            case DOCUMENT_ENTRY_CONFORMANCE_CDA:
                step = new TestStepDocumentEntryConformanceCDA(bean, testPath, inputPath, outputPath, testReport);
                break;
            case CONFORMANCE:
                step = new TestStepConformance(bean, testPath, inputPath, outputPath, testReport);
                break;
            case CONSISTENCY:
                step = new TestStepConsistency(bean, testPath, inputPath, outputPath, testReport);
                break;
            case UNDEFINED:
            default:
                throw new Exception("Unable to map the string (" + bean.getAction() + ") to one of the known actions");
        }
        return step;
    }

    private void writeTestReport() throws Exception {
        ReportingUtility.writeTestReportXML(testReport, outputPath + "/test_report.xml");
        ReportingUtility.writeTestReportXLS(testReport, outputPath + "/test_report.xlsx");
    }
}
