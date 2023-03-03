package edu.wustl.cil.SMM.RESTfulTesting;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wustl.cil.SMM.Common.CommonContentTester;
import edu.wustl.cil.SMM.Common.CommonNameValueTestItem;
import edu.wustl.cil.SMM.Common.CommonTestDriver;
import edu.wustl.cil.TestingWIA.DicomContentTester;
import edu.wustl.cil.TestingWIA.DicomFactory;
import edu.wustl.cil.TestingWIA.ElementTestItem;
import io.restassured.http.Header;
import io.restassured.response.Response;
//import org.nrg.xnat.dicom.jackson.module.JsonDicomWebDeserializationModule;
//import org.nrg.xnat.dicom.model.DicomObject;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class RESTfulTestDriver extends CommonTestDriver {
    private String endpoint;            // Endpoint to be tested
//    private String testFolderPath;      // Path to a folder with test cases
//    private String testIndexPath;       // Path to a file that lists test cases to execute
//    private String logFolder;           // Path to a log folder

    private final ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public RESTfulTestDriver(String endpoint, String testFolderPath, String logFolder) {
        this.endpoint = endpoint;
        this.testFolderPath = testFolderPath;
        this.logFolder = logFolder;
    }

    public RESTfulTestDriver() {
    }

    public void executeTests(String[] args) throws Exception {
        this.testFolderPath = args[1];
        this.testIndexPath  = args[2];
        this.logFolder      = args[3];
        this.endpoint       = args[4];

        List<String> testIndex = readTestCases();

        createLogFolder();

        ArrayList<RESTfulTestInstance> testInstances = new ArrayList<>();

        for (String testCase: testIndex) {
//            testInstances.add(executeOneServerTest(testCase));    FIX
        }

        String summaryFile = logFolder + "/summary.txt";
        PrintWriter writer = new PrintWriter (new FileWriter(summaryFile));
        logSummaryOfTestsX(writer, testInstances);
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

    /*
    private RESTfulTestInstance executeOneServerTest(String testCase) throws Exception {
        System.out.println(testCase);

        RESTfulTestInstance testInstance = new RESTfulTestInstance();
        testInstance.loadTestInstance(testFolderPath, testCase);

        String testLog = logFolder + "/" + testInstance.getTestKey() + ".txt";
        PrintWriter writer = new PrintWriter (new FileWriter(testLog));

        Header header = new Header("Accept", "application/dicom+json");
        logTestMetadata(writer, testInstance, header);

        Response response;

        try {
            response = given().baseUri(endpoint).basePath(testInstance.getPath()).params(testInstance.getQuery()).header(header).get();
            writer.println("Response body from peer\n" + response.getBody().asString() + "\n");
            testResponseCode(writer, response, testInstance);
            testBundleMetaData(writer, response, testInstance);
            Map<String, List<CommonNameValueTestItem>> mapOfTestItems = testResourceContent(writer, response, testInstance);
            writeSpreadsheet(testInstance, mapOfTestItems);
        } catch (java.net.ConnectException e) {
            writer.println("ConnectionException for endpoint: " + endpoint);
            e.printStackTrace(writer);
            testInstance.setFailed();
        }

        logTestMetrics(writer, testInstance);
        logTestCompletion(writer);

        writer.close();
        return testInstance;
    }
     */
    private void writeSpreadsheet(RESTfulTestInstance testInstance, Map<String, List<CommonNameValueTestItem>> mapOfTestItems) throws Exception {

        if (testInstance.get("SpreadsheetOutput") != null && mapOfTestItems != null) {
            String spreadsheetPath = logFolder + "/" + testInstance.get("SpreadsheetOutput");
            commonSupport.writeSpreadsheet(spreadsheetPath, testInstance, mapOfTestItems);
        }
    }

/*
    private void testBundleMetaData(PrintWriter writer, Response response, RESTfulTestInstance testInstance) throws Exception {
        objectMapper.registerModule(JsonDicomWebDeserializationModule.build());

        String referenceString  = testInstance.getReferenceString();
        String responseBody = response.getBody().asString();
        List<CommonNameValueTestItem> testItems = testInstance.getNameValueTestItemsFromKey("BundleMetadata");
        ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JsonNode nodeReference = objectMapper.readTree(referenceString);
        JsonNode nodeUnderTest = objectMapper.readTree(responseBody);

        CommonContentTester tester = new CommonContentTester();
        tester.testJsonNameValues(writer, "Bundle Metadata", testInstance, nodeReference, nodeUnderTest, testItems);
    }
    */


    private void testResponseCode(PrintWriter writer, Response response, RESTfulTestInstance testInstance) {
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

    private Map<String, List<CommonNameValueTestItem>> testResourceContent(PrintWriter writer,  Response response, RESTfulTestInstance testInstance) throws Exception {

        Map<String, List<CommonNameValueTestItem>> mapOfResults = null;

        if (testInstance.get("ResourceContent") != null) {

            String[] tokens = testInstance.get("ResourceContent").split(",");
            List<CommonNameValueTestItem> testItems = testInstance.getNameValueTestItemsFromFileName(tokens[0]);
            String nodePath = tokens[1];
            String keyPath = tokens[2];
            int limit = 0;
            if (tokens.length > 3) {
                limit = Integer.parseInt(tokens[3]);
            }

            String referenceString = testInstance.getReferenceString();
            String responseBody = response.getBody().asString();

            JsonNode nodeReference = objectMapper.readTree(referenceString);
            nodeReference = findNode(nodeReference, nodePath);

            JsonNode nodeUnderTest = objectMapper.readTree(responseBody);
            nodeUnderTest = findNode(nodeUnderTest, nodePath);

            CommonContentTester tester = new CommonContentTester();
            mapOfResults = tester.testJsonArray(writer, "", testInstance,
                    nodeReference,
                    nodeUnderTest,
                    testItems, keyPath, limit);
//            CommonContentTester tester = new CommonContentTester();
//            tester.testJsonNameValuesWithSpreadsheetOutput(
//                    writer,
//                    "",
//                    testInstance,
//                    "reference",
//                    "underTest",
//                    testItems,
//                    "logfoldere");
        }
        return mapOfResults;
    }
/*
    private void testJSONResponse(PrintWriter writer, Response response, RESTfulTestInstance testInstance) throws Exception {
        objectMapper.registerModule(JsonDicomWebDeserializationModule.build());

        String referenceString  = testInstance.getReferenceString();
        String responseBody = response.getBody().asString();
//        List<ElementTestItem> testItems = testInstance.getTestItems();
        String objectCount = testInstance.getObjectCount();
        String level       = testInstance.getLevel();

//        writer.println("Response body from peer\n" + responseBody + "\n");
        writer.flush();
    }


 */

    public void logTestMetadata(PrintWriter writer, RESTfulTestInstance testInstance, Header header) throws Exception {
        writer.println("Test begins: " + LocalDateTime.now());
        writer.println("Test key:    " + testInstance.getTestKey());
        writer.println("Title:       " + testInstance.getTitle());
        writer.println("Description: " + testInstance.getDescription());
        writer.println("Requirement: " + testInstance.getTestRequirement());
        writer.println("Description: " + testInstance.getDescription());
        writer.println("Folder:      " + testInstance.getTestFolder());
        writer.println("Endpoint:    " + endpoint);
        writer.println("Path:        " + testInstance.getPath());
        writer.println("Query:       " + testInstance.getQuery());
        writer.println("Header:      " + header.toString());
    }

    public void logSummaryOfTestsX(PrintWriter writer, ArrayList<RESTfulTestInstance> testInstances) throws Exception {
        String header =
                "Key\t" +
                        "Status\t" +
                        "Warnings\t" +
                        "Errors\t" +
                        "Description";
        writer.println(header);
        for (RESTfulTestInstance instance: testInstances) {
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
                            instance.getDescription()
            );
        }

    }

    public void logTestMetrics(PrintWriter writer, RESTfulTestInstance testInstance) throws Exception {
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
