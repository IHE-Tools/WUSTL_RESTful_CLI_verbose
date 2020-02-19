package edu.wustl.cil.SMM.Common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.http.Header;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonTestDriver {
    protected String testFolderPath;      // Path to a folder with test cases
    protected String testIndexPath;       // Path to a file that lists test cases to execute
    protected String logFolder;           // Path to a log folder
    protected String referenceToTestObject; // Value in this reference will depend on the type of test that is to be run

    protected final ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    protected CommonSupport commonSupport = new CommonSupport();

    public void checkArgs(String[] args) throws Exception {
        String s = consolidateArgs(args);
        throw new Exception("Should not be in this method in the base class, args are: " + s);
    }

    public void executeTests(String[] args) throws Exception {
        String s = consolidateArgs(args);
        throw new Exception("Should not be in this method in the base class, args are: " + s);
    }

    private String consolidateArgs(String[] args) {
        String rtn = "";
        String delim = "";
        if (args != null) {
            for (String s: args) {
                if (s != null) {
                    rtn += s + delim;
                    delim = " ";
                }
            }
        }
        return rtn;
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

    protected JsonNode findNode(JsonNode node, String path) throws Exception {
        JsonNode rtn = node;
        if (path != null) {
            String[] tokens = path.split("/");
            for (String token: tokens) {
                rtn = rtn.get(token);
            }
        }
        return rtn;
    }

     protected List<String> readTestCases() throws Exception {
        List<String> result = new ArrayList<>();
        Stream<String> lines = Files.lines(Paths.get(testIndexPath));
        List<String> tmp = lines.collect(Collectors.toList());

        for (String line: tmp) {
            if ((!(line.length() == 0)) && (!line.startsWith("#"))) {
                result.add(line);
            }
        }
        return result;
    }


    public void logTestMetadata(PrintWriter writer, CommonTestInstance testInstance, Header header) throws Exception {
        writer.println("Test begins: " + LocalDateTime.now());
        writer.println("Test key:    " + testInstance.getTestKey());
        writer.println("Title:       " + testInstance.getTitle());
        writer.println("Description: " + testInstance.getDescription());
        writer.println("Requirement: " + testInstance.getTestRequirement());
        writer.println("Description: " + testInstance.getDescription());
        writer.println("Folder:      " + testInstance.getTestFolder());
//        writer.println("Endpoint:    " + endpoint);
        writer.println("Path:        " + testInstance.getPath());
        writer.println("Query:       " + testInstance.getQuery());
        if (header != null) {
            writer.println("Header:      " + header.toString());
        }
        writer.flush();
    }

    public void logSummaryOfTests(PrintWriter writer, ArrayList<CommonTestInstance> testInstances) throws Exception {
        String header =
                "Key\t" +
                        "Status\t" +
                        "Warnings\t" +
                        "Errors\t" +
                        "Description";
        writer.println(header);
        for (CommonTestInstance instance: testInstances) {
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

    public void logTestMetrics(PrintWriter writer, CommonTestInstance testInstance) throws Exception {
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
