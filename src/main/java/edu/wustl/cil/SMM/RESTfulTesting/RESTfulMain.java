package edu.wustl.cil.SMM.RESTfulTesting;

import edu.wustl.cil.SMM.Common.CommonTestDriver;
import edu.wustl.cil.SMM.ContentTesting.ContentTestDriver;

import static io.restassured.RestAssured.given;

public class RESTfulMain {
    public static void main(String[] args) {
        System.out.println("Hello world");
        checkArgs(args);
        CommonTestDriver testDriver;

        try {
            testDriver = getTestDriver(args[0]);
            testDriver.executeTests(args);
//            switch (args[0]) {
//                case "REST":
//                    testRESTful(args);
//                    break;
//                case "CONTENT":
//                    testContent(args);
//                    break;
//                default:
//                    printUsageAndDie(args[0]);
//                    break;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CommonTestDriver getTestDriver(String command) throws Exception {
        CommonTestDriver driver = null;
        switch (command) {
            case "REST":
                driver = new RESTfulTestDriver();
                break;
            case "CONTENT":
                driver = new ContentTestDriver();
                break;
            default:
                throw new Exception("Could not get a test driver for this command: " + command);
        }
        return driver;
    }

//    public static void testRESTful(String[] args) throws Exception {
//        try {
//            RESTfulTestDriver testDriver = new RESTfulTestDriver(args);
//            testDriver.executeServerTests();
//        } catch (Exception e) {
//            e.printStackTrace();;
//        }
//    }

//    public static void testContent(String[] args) throws Exception {
//        try {
//            ContentTestDriver testDriver = new ContentTestDriver();
//            testDriver.executeTests(args);
//        } catch (Exception e) {
//            e.printStackTrace();;
//        }
//    }

    public static void checkArgs(String[] args) {
        if (args.length < 1) {
            printUsageAndDie("");
        }
        String[] commands = {"REST", "CONTENT"};
        for (String c: commands) {
            if (c.equals(args[0])) return;
        }
        printUsageAndDie(args[0]);
    }

    public static void printArguments(String[] args) {
        for (String arg: args) {
            System.out.println(arg);
        }
    }

    public static void printUsageAndDie(String msg) {
        System.out.println("Arguments: <command> [other arguments]\n" +
                "     commands are\n" +
                "       CONTENT\n" +
                "       REST\n" +
                "     To see arguments for a specific command, invoke program with the proper command and no arguments"
        );
        System.out.println(msg);
        System.exit(1);
    }
}
