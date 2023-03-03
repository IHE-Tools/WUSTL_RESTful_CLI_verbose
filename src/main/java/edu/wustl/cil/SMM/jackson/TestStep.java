package edu.wustl.cil.SMM.jackson;

import edu.wustl.cil.SMM.Reporting.TestReport;
import edu.wustl.cil.SMM.XDS.Factory;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static edu.wustl.cil.SMM.Reporting.LineItem.ItemType.TEXT;

public class TestStep {
    protected TestStepBean testStepBean;
    protected String testPath;
    protected String inputPath;
    protected String outputPath;
    protected String stepDescription = "";

    protected Factory factory = new Factory();
    protected TestReport testReport;

    public TestStep(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        this.testStepBean = testStepBean;
        this.testPath   = testPath;
        this.inputPath  = inputPath;
        this.outputPath = outputPath;
        this.testReport = testReport;
    }

    public TestStep() {

    }

    public void initialize(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        this.testStepBean = testStepBean;
        this.testPath   = testPath;
        this.inputPath  = inputPath;
        this.outputPath = outputPath;
        this.testReport = testReport;
    }

    public void executeStep() throws Exception {
        throw new Exception("Should not reach base class: TestStep::executeStep");
    }

    public void printStepMetadata(String header) throws Exception {
        testReport.addSection(testStepBean.getDescription());

        testReport.addSectionMetadata("Step Identifier", testStepBean.getStepIdentifier());
        testReport.addSectionMetadata("Description", testStepBean.getDescription());
        testReport.addSectionMetadata("Input Path", inputPath);
        testReport.addSectionMetadata("Output Path", outputPath);

        //testReport.addLineItem(TEXT, "", "Step Identifier", testStepBean.getStepIdentifier(), "", "", "", "");
        //testReport.addLineItem(TEXT, "", "Description", testStepBean.getDescription(), "", "", "", "");
        //testReport.addLineItem(TEXT, "", "Input Path", inputPath, "", "", "", "");
        //testReport.addLineItem(TEXT, "", "Output Path", outputPath, "", "", "", "");
    }

    public List<File> listFilesForFolder(final File folder) {
        ArrayList<File> fileArrayList = new ArrayList<>();
        if (folder.isDirectory()) {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    System.out.println("Subfolder ignored: " + fileEntry.getAbsolutePath());
                } else {
                    fileArrayList.add(fileEntry);
                }
            }
        }
        return fileArrayList;
    }

    protected boolean doStringsMatch(String referenceValue, String submittedValue, String comparisonOperator) throws Exception {
        if (submittedValue == null) {
            return false;
        }
        boolean flag = true;
        switch (comparisonOperator) {
            case "EQ.PID":
                String submittedStripped = submittedValue.replaceFirst("ISO\\^PI$", "ISO");
                String referenceStripped = referenceValue.replaceFirst("ISO\\^PI$", "ISO");
                flag = submittedStripped.equals(referenceStripped);
                break;
            case "EQ":
                flag = submittedValue.equals(referenceValue);
                break;
            case "EQCase":
                flag = submittedValue.equalsIgnoreCase(referenceValue);
                break;
            case "NE":
                flag = ! submittedValue.equals(referenceValue);
                break;
            case "STARTS":
                flag = submittedValue.startsWith(referenceValue);
                break;
            case "CONTAINS":
                flag = submittedValue.contains(referenceValue);
                break;
            case "NOTNULL":
                flag = ! submittedValue.isEmpty();
                break;
            case "NULL":
                flag = submittedValue.isEmpty();
                break;
            case "FORMAT":
                flag = formatTest(referenceValue, submittedValue, comparisonOperator);
                break;
            // These next items for comment, warning or errors in the output
            case "COMMENT":
                flag = true;
                break;
            // Force a warning message to tell user to perform visual inspection.
            case "VISUAL":
            case "WARNING":
                flag = false;
                break;
            // Force an error
            case "ERROR":
                flag = false;
                break;
            default:
                throw new Exception("Unrecognized comparison operator: " + comparisonOperator + ", Reference Value: " + referenceValue);
        }
        return flag;
    }

    protected Node getSubmissionSetFromFileExtract(String fileExtractPath) throws Exception {
        FileExtract fileExtract = (FileExtract) Utility.readXML(fileExtractPath, FileExtract.class);
        return edu.wustl.cil.SMM.XDS.Factory.getRegistryPackage(fileExtract.getMetadata());
    }

    protected Node getDocumentEntryFromFileExtract(String fileExtractPath, String mimeType) throws Exception {
        FileExtract fileExtract = (FileExtract) Utility.readXML(
                Utility.expandVariable(this, fileExtractPath),
                FileExtract.class);
        return edu.wustl.cil.SMM.XDS.Factory.getExtrinsicObject(fileExtract.getMetadata(), mimeType);
    }

    protected boolean formatTest(String referenceValue, String submittedValue, String comparisonOperator) {
        // Todo
        System.out.println(String.format("Format test %s, %s, %s", referenceValue, submittedValue, comparisonOperator));
        return true;
    }

    // Standard getters and setters

    public TestStepBean getTestStepBean() {
        return testStepBean;
    }

    public String getTestPath() {
        return testPath;
    }

    public String getInputPath() {
        return inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getStepDescription() { return stepDescription; }
}
