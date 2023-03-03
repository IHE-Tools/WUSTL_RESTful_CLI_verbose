package edu.wustl.cil.TestingWIA;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.restassured.http.Header;
import io.restassured.response.Response;
//import org.nrg.xnat.dicom.model.DicomObject;
//import org.nrg.xnat.dicom.jackson.module.JsonDicomWebDeserializationModule;

//import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.given;

public class WIATestDriver {
    private String endpoint;            // Endpoint to be tested
    private String testFolderPath;      // Path to a folder with test cases
    private String testIndexPath;       // Path to a file that lists test cases to execute
    private String logFolder;           // Path to a log folder

    private final ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public WIATestDriver(String endpoint, String testFolderPath, String logFolder) {
        this.endpoint = endpoint;
        this.testFolderPath = testFolderPath;
        this.logFolder = logFolder;
    }

    public WIATestDriver(String[] args) {

        this.testFolderPath = args[0];
        this.testIndexPath  = args[1];
        this.logFolder      = args[2];
        this.endpoint       = args[3];
    }

    public void executeServerTests() throws Exception {
        List<String> testIndex = readTestCases();

        createLogFolder();

        ArrayList<TestInstance> testInstances = new ArrayList<>();

        for (String testCase: testIndex) {
            testInstances.add(executeOneServerTest(testCase));
        }

        String summaryFile = logFolder + "/summary.txt";
        PrintWriter writer = new PrintWriter (new FileWriter(summaryFile));
        logSummaryOfTests(writer, testInstances);
        writer.close();
    }

    public void createLogFolder() throws Exception {
        Path p = Paths.get(logFolder);

        if (Files.exists(p)) {
            File f = new File(logFolder);
            File[] contents = f.listFiles();
            for (File x: contents) {
                x.delete();
            }

            Files.deleteIfExists(p);
        }
        Files.createDirectories(p);
    }

    private TestInstance executeOneServerTest(String testCase) throws Exception {
        System.out.println(testCase);

        TestInstance testInstance = new TestInstance();
        testInstance.loadTestInstance(testFolderPath, testCase);
        //TestResult testResult = new TestResult();

        String testLog = logFolder + "/" + testInstance.getTestKey() + ".txt";
        PrintWriter writer = new PrintWriter (new FileWriter(testLog));

        Header header = new Header("Accept", "application/dicom+json");
        logTestMetadata(writer, testInstance, header);

        Response response;

        try {
            response = given().baseUri(endpoint).basePath(testInstance.getPath()).params(testInstance.getQuery()).header(header).get();
            testResponseCode(writer, response, testInstance);
        } catch (Exception e) {
            throw e;
        }
//            testJSONResponse(writer, response, testInstance);     FIX
/*
        } catch (java.net.ConnectException e) {

            writer.println("ConnectionException for endpoint: " + endpoint);
            e.printStackTrace(writer);
            testInstance.setFailed();

//            writer.println("ConnectionException for endpoint: " + endpoint);
//            e.printStackTrace(writer);
        }

 */


        logTestMetrics(writer, testInstance);
        logTestCompletion(writer);

        writer.close();
        return testInstance;
    }

    private void testResponseCode(PrintWriter writer, Response response, TestInstance testInstance) {
        int returnedResponseCode = response.getStatusCode();
        Set<Integer> allowedResponseCodes = testInstance.getAllowedResponseCodes();

        writer.println("\nTest response code returned by peer, code is " + returnedResponseCode);
        if (allowedResponseCodes.contains(new Integer(returnedResponseCode))) {
            writer.println("Returned response code matches allowed value: " + returnedResponseCode);
        } else {
            writer.println      ("Returned response code (" + returnedResponseCode + ") is not in the set of allowed response codes (" + testInstance.getResponses() + ")" );
            testInstance.addError("Returned response code (" + returnedResponseCode + ") is not in the set of allowed response codes (" + testInstance.getResponses() + ")" );
        }
    }
/*
    private void testJSONResponse(PrintWriter writer, Response response, TestInstance testInstance) throws Exception {
        objectMapper.registerModule(JsonDicomWebDeserializationModule.build());

        String referenceString  = testInstance.getReferenceString();
        String responseBody = response.getBody().asString();
        List<ElementTestItem> testItems = testInstance.getTestItems();
        String objectCount = testInstance.getObjectCount();
        String level       = testInstance.getLevel();

        writer.println("Response body from peer\n" + responseBody + "\n");
        writer.flush();

        DicomObject[] underTestArray = new DicomObject[0];
        if (responseBody != null && responseBody.length() != 0) {
            underTestArray = objectMapper.readValue(responseBody, DicomObject[].class);
        }
        Map<String, DicomObject> underTestMap = DicomFactory.constructDicomSetFromArray(underTestArray, level);

        if (referenceString == null) {
            DicomContentTester.testArrayContentNoReference(writer, testInstance, underTestMap, testItems, objectCount);
        } else {
            DicomObject[] referenceArray = (referenceString == null) ? null: objectMapper.readValue(referenceString, DicomObject[].class);
            Map<String, DicomObject> referenceMap = DicomFactory.constructDicomSetFromArray(referenceArray, level);
            DicomContentTester.testStudyArrayContent(writer, testInstance, referenceMap, underTestMap, testItems, objectCount);
        }
    }
 */
    private List<String> readTestCases() throws Exception {
        List<String> tmp;
        List<String> result = new ArrayList<>();
        Stream<String> lines = Files.lines(Paths.get(testIndexPath));
        tmp = lines.collect(Collectors.toList());

        for (String line: tmp) {
            if ((!(line.length() == 0)) && (!line.startsWith("#"))) {
                result.add(line);
            }
        }
        return result;
    }


    public void logTestMetadata(PrintWriter writer, TestInstance testInstance, Header header) throws Exception {
        writer.println("Test begins: " + LocalDateTime.now());
        writer.println("Test key:    " + testInstance.getTestKey());
        writer.println("Title:       " + testInstance.getTitle());
        writer.println("Requirement: " + testInstance.getTestRequirement());
        writer.println("Description: " + testInstance.getDescription());
        writer.println("Folder:      " + testInstance.getTestFolder());
        writer.println("Endpoint:    " + endpoint);
        writer.println("Path:        " + testInstance.getPath());
        writer.println("Query:       " + testInstance.getQuery());
        writer.println("Header:      " + header.toString());
    }

    public void logSummaryOfTests(PrintWriter writer, ArrayList<TestInstance> testInstances) throws Exception {
        String header =
                "Key\t" +
                        "Status\t" +
                        "Warnings\t" +
                        "Errors\t" +
                        "Title";
        writer.println(header);
        for (TestInstance instance: testInstances) {
            String status = "FAILED";
            if (instance.isDidTestPass()) {
                if (instance.getWarnings() == 0) status = "PASS";
                else                             status = "PASS W/WARNINGS";
            }
            writer.println(
                    instance.getTestKey() + "\t" +
                            status + "\t" +
                            instance.getWarnings() + "\t" +
                            instance.getErrors() + "\t" +
                            instance.getTitle()
            );
        }

    }

    public void logTestMetrics(PrintWriter writer, TestInstance testInstance) throws Exception {
        writer.println();
        writer.println("Warning count:   " + testInstance.getWarnings());
        writer.println("Error count:     " + testInstance.getErrors());
        if (testInstance.isDidTestPass()) {
            writer.println("Overall status: PASS");
        } else {
            writer.println("Overall status: FAIL");
        }

        writer.println();
        logArrayLines(writer, "Warnings", testInstance.getWarningLines());
        logArrayLines(writer, "Errors  ", testInstance.getWarningLines());
    }

    public void logTestCompletion(PrintWriter writer) throws Exception {
        writer.println();

        writer.println("Test complete: " + LocalDateTime.now());
    }

    public void logArrayLines(PrintWriter writer, String heading, ArrayList<String> lines) throws Exception {
        writer.println(heading);
        for (String line: lines) {
            writer.println(line);
        }
    }


    // Getters and setters beneath here

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getTestFolderPath() {
        return testFolderPath;
    }

    public void setTestFolderPath(String testFolderPath) {
        this.testFolderPath = testFolderPath;
    }

    public String getLogFolder() {
        return logFolder;
    }

    public void setLogFolder(String logFolder) {
        this.logFolder = logFolder;
    }
}
