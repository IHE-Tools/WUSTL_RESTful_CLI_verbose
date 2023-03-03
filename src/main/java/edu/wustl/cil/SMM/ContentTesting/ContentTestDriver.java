package edu.wustl.cil.SMM.ContentTesting;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wustl.cil.SMM.Common.*;
import edu.wustl.cil.SMM.RESTfulTesting.JSONContentTester;
import edu.wustl.cil.SMM.RESTfulTesting.NameValueTestItem;
import edu.wustl.cil.SMM.RESTfulTesting.RESTfulTestInstance;
import io.restassured.http.Header;
import io.restassured.response.Response;
//import org.nrg.xnat.dicom.jackson.module.JsonDicomWebDeserializationModule;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class ContentTestDriver extends CommonTestDriver {
    private String inputFile;           // File with content to be tested

    private final ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public void executeTests(String[] args) throws Exception {
        initialize(args);
        List<String> testIndex = readTestCases();

        ArrayList<CommonTestInstance> testInstances = new ArrayList<>();

        createLogFolder();

        for (String testCase : testIndex) {
            testInstances.add(executeOneTest(testCase));
        }

        String summaryFile = logFolder + "/summary.txt";
        PrintWriter writer = new PrintWriter(new FileWriter(summaryFile));
        logSummaryOfTests(writer, testInstances);
        writer.close();


        System.out.println("Completed ContentTestDriver::executeTests");
    }

    public void initialize(String[] args) throws Exception {
        if (args.length != 5) {
            printArguments(args);
            printUsageAndDie("The module ContentTestDriver requires 5 arguments, you provided: " + args.length);
        }
        // args[0] is the command that brought us to this module. Assume that is driven by someone else, so ignore it
        testFolderPath = args[1];
        testIndexPath = args[2];
        logFolder = args[3];
        inputFile = args[4];

        if (!Files.isDirectory(Paths.get(testFolderPath)))
            printUsageAndDie("This folder does not exist: " + testFolderPath);
        if (!Files.isRegularFile(Paths.get(testIndexPath)))
            printUsageAndDie("This test index file does not exist: " + testIndexPath);
        // We will create the log folder, so do not test for it
        if (!Files.isRegularFile(Paths.get(inputFile)))
            printUsageAndDie("This input file does not exist: " + inputFile);
    }

    public void printArguments(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }
    }

    public void printUsageAndDie(String msg) throws Exception {
        System.out.println("Arguments: test_folder index_file log_folder input_fle\n" +
                " e.g.:\n" +
                "     /opt/restful_tests/XXX/testcases-content\n" +
                "     /opt/restful_tests/XXX/testcases-content/doc_1.txt\n" +
                "     /opt/restful_logs/XXX/content/doc_1/wxy\n" +
                "     /tmp/x1.json"
        );
        System.out.println(msg);
        throw new Exception("Wrong arguments or missing files/folders");
    }

    private CommonTestInstance executeOneTest(String testCase) throws Exception {
        System.out.println(testCase);

        CommonTestInstance testInstance = new CommonTestInstance();
        testInstance.loadTestInstance(testFolderPath, testCase);

        PrintWriter writer = CommonSupport.getPrintWriter(logFolder + "/" + testInstance.getTestKey() + ".txt");
        logTestMetadata(writer, testInstance, null);

        try {
            String[] testTokens = testInstance.get("Steps").split(",");
            if (testTokens == null) testTokens = new String[] {"Linear"};
            for (String token: testTokens) {
                switch (token) {
                    case "Linear":
                        testResourceContent(writer, inputFile, testInstance);
                        break;
                    case "Spreadsheet":
                        testContentWithSpreadsheetOutput(writer, inputFile, testInstance);
                        break;
                    default:
                        break;
                }
            }


        } catch (Exception e) {
            writer.println("Exception for input file: " + inputFile);
            e.printStackTrace();
            testInstance.addError("Some type of exception thrown when executing test for file: " + inputFile);
        }

        logTestMetrics(writer, testInstance);
        logTestCompletion(writer);

        writer.close();
        return testInstance;
    }

//
//        try {
//            response = given().baseUri(endpoint).basePath(testInstance.getPath()).params(testInstance.getQuery()).header(header).get();
//            writer.println("Response body from peer\n" + response.getBody().asString() + "\n");
//            testResponseCode(writer, response, testInstance);
//            testBundleMetaData(writer, response, testInstance);
//            testResourceContent(writer, response, testInstance);
//        } catch (java.net.ConnectException e) {
//            writer.println("ConnectionException for endpoint: " + endpoint);
//            e.printStackTrace(writer);
//            testInstance.setFailed();
//        }
//
//        logTestMetrics(writer, testInstance);
//        logTestCompletion(writer);
//
//        writer.close();
//        return testInstance;
//    }
//
//
//    private void testBundleMetaData(PrintWriter writer, Response response, RESTfulTestInstance testInstance) throws Exception {
//        objectMapper.registerModule(JsonDicomWebDeserializationModule.build());
//
//        String referenceString  = testInstance.getReferenceString();
//        String responseBody = response.getBody().asString();
//        List<NameValueTestItem> testItems = testInstance.getNameValueTestItemsFromKey("BundleMetadata");
//        ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        JsonNode nodeReference = objectMapper.readTree(referenceString);
//        JsonNode nodeUnderTest = objectMapper.readTree(responseBody);
//
//        JSONContentTester tester = new JSONContentTester();
//        tester.testJsonNameValues(writer, "Bundle Metadata", testInstance, nodeReference, nodeUnderTest, testItems);
//    }
//
//    private void testResponseCode(PrintWriter writer, Response response, RESTfulTestInstance testInstance) {
//        int returnedResponseCode = response.getStatusCode();
//        Set<Integer> allowedResponseCodes = testInstance.getAllowedResponseCodes();
//
//        writer.println("\nTest response code returned by peer, code is " + returnedResponseCode);
//        if (allowedResponseCodes.contains(new Integer(returnedResponseCode))) {
//            writer.println("Returned response code matches allowed value: " + returnedResponseCode);
//        } else {
//            writer.println      ("Returned response code (" + returnedResponseCode + ") is not in the set of allowed response codes (" + testInstance.getResponses() + ")" );
//            testInstance.addError("Returned response code (" + returnedResponseCode + ") is not in the set of allowed response codes (" + testInstance.getResponses() + ")" );
//        }
//    }
//

    private String readAllBytes(String inputFile) throws Exception {
        String rtn = "";
        File f = new File(inputFile);
        if (f.exists()) {
            rtn = new String(Files.readAllBytes(f.toPath()));
        } else {
            throw new Exception("Input file to be evaluated is not found: " + inputFile);
        }
        return rtn;
    }

    private void testResourceContent(PrintWriter writer,  String inputFile, CommonTestInstance testInstance) throws Exception {

        if (testInstance.get("ResourceContent") != null) {

            String[] tokens = testInstance.get("ResourceContent").split(",");
            List<CommonNameValueTestItem> testItems = testInstance.getNameValueTestItemsFromFileName(tokens[0]);
//            String nodePath = tokens[1];
//            String keyPath = tokens[2];

            String referenceString = testInstance.getReferenceString();
            String responseBody    = this.readAllBytes(inputFile);

            JsonNode nodeReference = objectMapper.readTree(referenceString);

            JsonNode nodeUnderTest = objectMapper.readTree(responseBody);

            CommonContentTester tester = new CommonContentTester();
            tester.testJsonNameValues(writer, "", testInstance,
                    nodeReference,
                    nodeUnderTest,
                    testItems);
        }
    }

    private void testContentWithSpreadsheetOutput(PrintWriter writer,  String inputFile, CommonTestInstance testInstance) throws Exception {
        List<CommonNameValueTestItem> testItems = null;

        try {
            if (testInstance.get("ResourceContent") != null && testInstance.get("CSVOutput") != null) {
                String[] tokens = testInstance.get("ResourceContent").split(",");
                testItems = testInstance.getNameValueTestItemsFromFileName(tokens[0]);
//            String nodePath = tokens[1];
//            String keyPath = tokens[2];

                String referenceString = testInstance.getReferenceString();
                String underTestString = this.readAllBytes(inputFile);

//                JsonNode nodeReference = objectMapper.readTree(referenceString);
//                JsonNode nodeUnderTest = objectMapper.readTree(underTestString);

                CommonContentTester tester = new CommonContentTester();
                tester.testJsonNameValuesWithSpreadsheetOutput(writer, "", testInstance,
                        referenceString, underTestString,
                        testItems,
                        logFolder);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (testInstance.get("SpreadsheetOutput") != null) {
                String spreadsheetPath = logFolder + "/" + testInstance.get("SpreadsheetOutput");
                commonSupport.writeSpreadsheet(spreadsheetPath, testItems);
            }

        }
    }

}
