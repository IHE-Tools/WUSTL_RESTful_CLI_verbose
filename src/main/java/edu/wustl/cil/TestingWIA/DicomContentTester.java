package edu.wustl.cil.TestingWIA;

import java.io.PrintWriter;
import java.util.Map;
import java.util.List;

//import org.nrg.xnat.dicom.model.DicomObject;
//import org.nrg.xnat.dicom.model.DicomAttribute;

public class DicomContentTester {

    /*
    public static void testStudyArrayContent(PrintWriter writer, TestInstance testInstance, Map<String, DicomObject> reference, Map<String, DicomObject> underTest, List<ElementTestItem> testList, String objectCount) {
        writer.println("\nTesting returned objects against a reference set." +
                "\n Number of objects in reference data set: " + reference.size() +
                "\n Number of objects returned by system:    " + underTest.size());

        if (objectCount == null) objectCount="IGNORE";
        switch (objectCount) {
            case "IGNORE":
                break;
            case "SAME":
                if (underTest.size() == reference.size()) {
                    writer.println("Number of objects in response matches number in the reference set");
                } else {
                    writer.println      ("Number of objects in response under test does not match the number if the reference set.");
                    testInstance.addError("Number of objects in response under test does not match the number if the reference set.");
                    testInstance.setFailed();
                }
                break;
            case "NOT-ZERO":
                break;
            default:
                writer.println     ("Unrecognized value for ObjectCount: " + objectCount + "    Should be null, IGNORE, SAME, or NOT-ZERO");
                testInstance.addError("Unrecognized value for ObjectCount: " + objectCount + "    Should be null, IGNORE, SAME, or NOT-ZERO");
                testInstance.setFailed();
                break;
        }
        for (String identifier: reference.keySet()) {
            DicomObject referenceStudy = reference.get(identifier);
            DicomObject studyUnderTest = underTest.get(identifier);
            if (studyUnderTest == null) {
                testInstance.addError("DICOM Study not found for Study Instance UID: " + identifier);
                testInstance.setFailed();
            } else {
                testOneDicomObject(writer, testInstance, identifier, referenceStudy, studyUnderTest, testList);
            }
        }
    }
*/

    /*
    public static void testArrayContentNoReference(PrintWriter writer, TestInstance testInstance, Map<String, DicomObject> underTest, List<ElementTestItem> testList, String objectCount) {
        writer.println("\nTesting returned objects to determine if they contain required elements; content is not tested." +
                "\n Number of objects in response: " + underTest.size());
        for (String identifier: underTest.keySet()) {
            DicomObject testObject = underTest.get(identifier);
            testOneDicomObject(writer, testInstance, identifier,null, testObject, testList);
        }
    }
    */


/*
    public static void testOneDicomObject(PrintWriter writer, TestInstance testInstance, String identifier, DicomObject reference, DicomObject underTest, List<ElementTestItem> testList) {
        writer.println();
        writer.println("Identifier of object under test:       " + identifier);
        writer.println("Number of elements in returned object: " + underTest.getAttributes().size());
        writer.println("Number of elements to be tested:       " + testList.size());
        for (ElementTestItem t: testList) {
            String tag = t.getElementTags()[0];

            DicomAttribute referenceAttribute = (reference == null) ? null : reference.findAttributeByTag(tag);
            DicomAttribute underTestAttribute = underTest.findAttributeByTag(tag);
            t.testAttribute(writer, testInstance, referenceAttribute, underTestAttribute);
        }

    }
*/
}
