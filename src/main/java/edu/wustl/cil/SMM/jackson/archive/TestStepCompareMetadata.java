package edu.wustl.cil.SMM.jackson.archive;

import edu.wustl.cil.SMM.Reporting.LineItem;
import edu.wustl.cil.SMM.Reporting.TestReport;
import edu.wustl.cil.SMM.XDS.ExtrinsicObject;
import edu.wustl.cil.SMM.XDS.Factory;
import edu.wustl.cil.SMM.jackson.TestStep;
import edu.wustl.cil.SMM.jackson.TestStepBean;
import sun.font.ExtendedTextLabel;

import static edu.wustl.cil.SMM.Reporting.LineItem.ItemType.*;

public class TestStepCompareMetadata extends TestStep {

    Factory factory = new Factory();

    public TestStepCompareMetadata(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
    }

    @Override
    public void executeStep() throws Exception {
        printStepMetadata("\nExecute: CompareMetadata");
        String inputReference = testStepBean.getInputReference();
        String inputToTest    = testStepBean.getInputToTest();

        testReport.addLineItem(VERBOSE, "Relative path", "", inputReference, "Input reference is name of the file with the data that is truth.", "", "", "", "");
        testReport.addLineItem(VERBOSE, "Relative path", "", inputToTest, "Input to test is the name of the file with data to compare to truth", "", "", "", "");

        inputReference = pathSubstitution(inputReference);
        inputToTest    = pathSubstitution(inputToTest);

        testReport.addLineItem(VERBOSE, "Absolute path", "", inputReference, "Input reference is name of the file with the data that is truth.", "", "", "", "");
        testReport.addLineItem(VERBOSE, "Absolute path", "", inputToTest, "Input to test is the name of the file with data to compare to truth.", "", "", "", "");

        String mimeTypeReference = testStepBean.getMimeTypeReference();
        String mimeTypeToTest    = testStepBean.getMimeTypeToTest();

        testReport.addLineItem(VERBOSE,"", "Mime type of the reference data", mimeTypeReference, "We identify and search for a single file by mime type", "", "", "", "");
        testReport.addLineItem(VERBOSE,"", "Mime type of the data to test (should be the same)", mimeTypeToTest, "We identify and search for a single file by mime type", "", "", "", "");

        ExtrinsicObject[] referenceObjects   = factory.deserializeExtrinsicObjects(inputReference);
        ExtrinsicObject[] inputToTestObjects = factory.deserializeExtrinsicObjects(inputToTest);

        ExtrinsicObject referenceHL7V2 = findSingleObject(referenceObjects,   mimeTypeReference);
        if (referenceHL7V2 == null) {
            testReport.addLineItem(ERROR, mimeTypeReference, "We assume exactly one REFERENCE file exists with this mime type.", mimeTypeReference, "We found 0 or more than 1 matching file.", "", "", "", "");
            return;
        }
        ExtrinsicObject toTestHL7V2    = findSingleObject(inputToTestObjects, mimeTypeToTest);
        if (toTestHL7V2 == null) {
            testReport.addLineItem(ERROR, mimeTypeReference, "We assume exactly one TO TEST file exists with this mime type.", mimeTypeReference, "We found 0 or more than 1 matching file.", "", "", "", "");
            return;
        }
        compareMetadata(referenceHL7V2, toTestHL7V2);
    }

    private String pathSubstitution(String input) {
        if (input.startsWith("$OUTPUT")) {
            input = outputPath + input.substring(7);
        }
        return input;
    }

    private ExtrinsicObject findSingleObject(ExtrinsicObject[] extrinsicObjects, String mimeType) throws Exception {
        ExtrinsicObject obj = null;
        for (int i = 0; i < extrinsicObjects.length; i++) {
            if (extrinsicObjects[i].getMimeType().equals(mimeType)) {
                if (obj != null) {
                    testReport.addLineItem(ERROR, mimeType,"We assume exactly one file exists with this mime type", mimeType, "We found two files and gave up.", "", "", "", "");
                    return null;
                }
                obj = extrinsicObjects[i];
            }
        }
        if (obj == null) {
            testReport.addLineItem(ERROR, mimeType,"We assume exactly one file exists with this mime type", mimeType, "We found zero files with this mime type.", "", "", "", "");
            return null;
        }
        return obj;
    }

    private void compareMetadata(ExtrinsicObject referenceObject, ExtrinsicObject testObject) throws Exception {
        String[] fields = {
                "mimeType",
                "objectType",
//                "sourcePatientInfo3",   // DOB
//                "sourcePatientInfo4"    // Sex
        };

        for (String field: fields) {
            String referenceString = referenceObject.getValue(field);
            String testString      = testObject.getValue(field);
            LineItem.ItemType itemType = SUCCESS;
            String message = "Submitted value matches reference value.";
            if (! testString.equals(referenceString)) {
                itemType = ERROR;
                message =  "Submitted value does NOT match reference value.";
            }
            testReport.addLineItem(itemType, field, "Submitted metadata should match reference value", field, message, referenceString, testString, "", "");
        }

        String[] patientInfo = {
                "PID-7|",    // DOB
                "PID-8|",    // Sex
        };

        for (String field: patientInfo) {
            String referenceString = getSourcePatientInfo(referenceObject, field);
            String testString      = getSourcePatientInfo(testObject, field);
            LineItem.ItemType itemType = SUCCESS;
            String message = "Submitted value matches reference value.";

            if (! testString.equals(referenceString)) {
                itemType = ERROR;
                message =  "Submitted value does NOT match reference value.";
            }
            testReport.addLineItem(itemType, field,"Submitted metadata should match reference value", field, message, referenceString, testString, "", "");
        }
    }

    private String getSourcePatientInfo(ExtrinsicObject extrinsicObject, String valuePrefix) throws Exception {
        String rtn = null;

        for (int i = 1; i <= 4 & rtn == null; i++) {
            String candidate = extrinsicObject.getValue("sourcePatientInfo" + i);
            if (candidate != null && candidate.startsWith(valuePrefix)) {
                rtn = candidate;
            }
        }
        if (rtn == null) {
            rtn = "NO VALUE FOUND for: " + valuePrefix;
        }
        return rtn;
    }
}
