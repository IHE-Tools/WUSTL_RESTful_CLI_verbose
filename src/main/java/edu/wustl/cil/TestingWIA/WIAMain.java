package edu.wustl.cil.TestingWIA;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
//import org.nrg.xnat.dicom.model.DicomObject;

//import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;

import static io.restassured.RestAssured.given;

public class WIAMain {
    private final ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    public static void main(String[] args) {

        checkArgs(args);

        try {
            //WIATestDriver testDriver = new WIATestDriver("http://mac-mini:8042/dicom-web", "/opt/wia/testcases/server", "/opt/wia/logs");
            WIATestDriver testDriver = new WIATestDriver(args);

            testDriver.executeServerTests();
        } catch (Exception e) {
            e.printStackTrace();;
        }
//        WIAMain main = new WIAMain();
//        System.out.println("WIAMain main");
//        main.execute(args);
    }

    public static void checkArgs(String[] args) {
        if (args.length != 4) printUsageAndDie("You did not supply 4 arguments");
        if (!Files.isDirectory(Paths.get(args[0])))    printUsageAndDie("This folder does not exist: " + args[0]);
        if (!Files.isRegularFile(Paths.get(args[1])))  printUsageAndDie("This test index file does not exist: " + args[1]);

    }

    public static void printUsageAndDie(String msg) {
        System.out.println("Arguments: test_folder index_file log_folder qido-rs_url\n" +
                " e.g.:\n"  +
                "     /opt/restful_tests/wia/testcases-server\n" +
                "     /opt/restful_tests/wia/testcases-server/baseline.txt\n" +
                "     /opt/restful_logs/wia/server/baseline/wxy\n" +
                "     http://localhost:8080/qido-wado"

        );
        System.out.println(msg);
        System.exit(1);
    }

    public void execute(String[] args) {
        try {
            switch(args[0]) {
                case "Study":
                    //testStudy(jsonString);
                    requestStudy();
                    break;
                case "Series":
                    break;
                case "Instance":
                    break;
                default:
                    System.out.println("Unrecognized command: " + args[0]);
                    System.exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
/*
    private void testStudy(PrintWriter writer, String jsonString) throws Exception {
        ElementTestItem[] testSpecifications = {
                new ElementTestItem("00080020", "EQUAL", "ERROR"),
                new ElementTestItem("00080030", "EQUAL", "ERROR"),
                new ElementTestItem("00080050", "EQUAL", "ERROR"),
                new ElementTestItem("00100010", "EQUAL", "ERROR"),
                new ElementTestItem("00100020", "EQUAL", "ERROR"),
                new ElementTestItem("00200010", "EQUAL", "ERROR")
        };
        writer.println(jsonString);

        DicomObject[] underTestArray = objectMapper.readValue(jsonString, DicomObject[].class);
        DicomObject[] referenceArray = objectMapper.readValue(jsonString, DicomObject[].class);
        Map<String, DicomObject> referenceMap = DicomFactory.constructDicomStudySet(referenceArray);
        Map<String, DicomObject> underTestMap = DicomFactory.constructDicomStudySet(referenceArray);

        System.out.println("Should not get here");
        System.exit(1);

//        DicomContentTester.testStudyArrayContent(writer, referenceMap, underTestMap, Arrays.asList(testSpecifications));
    }


 */
    public void requestStudy() throws Exception {
        Response response = RestAssured.get("http://mac-mini:8042/dicom-web/studies");
        String x = response.toString();
        String body = response.getBody().asString();
        int y = response.statusCode();
        int z;
        z=y;
//        System.out.println(body);
        Header header = new Header("Accept", "application/dicom+json");
        response = given().baseUri("http://mac-mini:8042/dicom-web").basePath("/studies").header(header).get();
        body = response.getBody().asString();
        System.out.println(body);
    }
}
