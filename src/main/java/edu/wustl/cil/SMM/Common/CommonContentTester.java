package edu.wustl.cil.SMM.Common;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.io.PrintWriter;
import java.util.*;
import java.util.TreeMap;


public class CommonContentTester {

    public Map<String, List<CommonNameValueTestItem>> testJsonArray(PrintWriter writer, String caption, CommonTestInstance testInstance, JsonNode referenceArray, JsonNode underTestArray, List<CommonNameValueTestItem> testList, String keyPath, int limit) throws Exception {
        writer.println("\nJSON Array Testing: " + caption);
        int referenceSize = referenceArray.size();
        int underTestSize = (underTestArray == null) ? 0 : underTestArray.size();
        if (underTestSize >= referenceSize) {
            writer.println("Array under test has at least as many elements as reference array: " + underTestSize + "/" + referenceSize);
        }
        Map<String, List<CommonNameValueTestItem>> rtn = new HashMap<>();

        Map<String, String> referenceMap;
        Map<String, String> underTestMap;
        try {
            referenceMap = createStringMap(referenceArray, keyPath, limit, "Reference Array");
            underTestMap = createStringMap(underTestArray, keyPath, limit, "Under Test Array");
        } catch (Exception e) {
            writer.flush();
            e.printStackTrace();
            throw new Exception("Unable to create a map of JsonNodes from reference or underTest array using path: " + keyPath);
        }

        Set<String> referenceKeys = referenceMap.keySet();
        for (String key: referenceKeys) {
            String reference = referenceMap.get(key);
            String underTest = underTestMap.get(key);

            writer.println("\nTesting content for object selected by: " + keyPath + ", unique value: " + key);
            if (underTest != null) {
                List<CommonNameValueTestItem> copyOfTestList = duplicateList(testList);
                testJsonNameValues(writer, caption, testInstance, reference, underTest, copyOfTestList);
                rtn.put(key, copyOfTestList);
            } else {
                writer.println("No corresponding object found in the array of test objects");
                testInstance.addError("No corresponding object found in the array of test objects, path: " + keyPath + ", value: " + key);
            }
        }
        return rtn;
    }

    private List<CommonNameValueTestItem> duplicateList(List<CommonNameValueTestItem> originalList) {
        List<CommonNameValueTestItem> rtn = null;
        if (originalList != null) {
            rtn = new ArrayList<>();
            for (CommonNameValueTestItem item: originalList) {
                CommonNameValueTestItem copy = new CommonNameValueTestItem(item);
                rtn.add(copy);
            }
        }
        return rtn;
    }


    public void testJsonNameValues(PrintWriter writer, String caption, CommonTestInstance testInstance, JsonNode reference, JsonNode underTest, List<CommonNameValueTestItem> testList) throws Exception {
        writer.println("JSON Content Testing: " + caption);

        String path = null;
        String referenceValue = null;
        String underTestValue = null;

        try {
            for (CommonNameValueTestItem testItem : testList) {
                if (testItem.getItemType().equals("TEST")) {
                    path = testItem.getTestPath();
                    referenceValue = getNestedString(reference, path, "NULL-VALUE");
                    underTestValue = getNestedString(underTest, path, "NULL-VALUE");
                    testItem.testValue(writer, testInstance, referenceValue, underTestValue);
                    referenceValue = underTestValue = null;
                } else if (testItem.getItemType().equals("DIRECTIVE")) {
                    writer.println(testItem.getArgument(1));
                }
            }
        } catch (Exception e) {
            writer.flush();
            e.printStackTrace();
            throw new Exception("Failed to complete testing a Name/Value pair" +
                    ", path: " + path +
                    ", referenceValue: " + referenceValue +
                    ", underTestValue; " + underTestValue);
        }
    }


    public void testJsonNameValues(PrintWriter writer, String caption, CommonTestInstance testInstance, String reference, String underTest, List<CommonNameValueTestItem> testList) throws Exception {
        writer.println("JSON Content Testing: " + caption);

        String jsonPath = null;
        String referenceValue = null;
        String underTestValue = null;

        try {
            for (CommonNameValueTestItem testItem : testList) {
                if (testItem.getItemType().equals("TEST")) {
                    jsonPath = testItem.getTestPath();
                    referenceValue = getNestedStringJSONPath(reference, jsonPath, "NULL-VALUE");
                    underTestValue = getNestedStringJSONPath(underTest, jsonPath, "NULL-VALUE");
                    testItem.testValue(writer, testInstance, referenceValue, underTestValue);
                    referenceValue = underTestValue = null;
                } else if (testItem.getItemType().equals("DIRECTIVE")) {
                    writer.println(testItem.getArgument(1));
                }
            }
        } catch (Exception e) {
            writer.flush();
            e.printStackTrace();
            throw new Exception("Failed to complete testing a Name/Value pair" +
                    ", path: " + jsonPath +
                    ", referenceValue: " + referenceValue +
                    ", underTestValue; " + underTestValue);
        }
    }

    public void testJsonNameValuesWithSpreadsheetOutput(PrintWriter writer, String caption, CommonTestInstance testInstance, JsonNode reference, JsonNode underTest, List<CommonNameValueTestItem> testList, String logFolder) throws Exception {
        writer.println("JSON Content Testing: " + caption);

        PrintWriter spreadsheetWriter = CommonSupport.getPrintWriter(logFolder + "/" + testInstance.get("SpreadsheetOutput"));
        String path = null;
        String referenceValue = null;
        String underTestValue = null;

        try {
            for (CommonNameValueTestItem testItem : testList) {
                if (testItem.getItemType().equals("TEST")) {
                    path = testItem.getTestPath();
                    if (!path.startsWith("/")) path = "/" + path;
                    referenceValue = getNestedStringJSONPath(reference, path, "NULL-VALUE");
                    underTestValue = getNestedStringJSONPath(underTest, path, "NULL-VALUE");
                    spreadsheetWriter.println(
                            testItem.getTestCaption() + "\t" +
                            referenceValue + "\t" +
                            underTestValue + "\t" +
                            path
                    );
                    testItem.testValue(writer, testInstance, referenceValue, underTestValue);
                    referenceValue = underTestValue = null;
                } else {
                    spreadsheetWriter.println(testItem.getArgument(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to complete testing a Name/Value pair" +
                    ", path: " + path +
                    ", referenceValue: " + referenceValue +
                    ", underTestValue; " + underTestValue);
        } finally {
            writer.flush();
            spreadsheetWriter.close();
        }
    }

    public void testJsonNameValuesWithSpreadsheetOutput(PrintWriter writer, String caption, CommonTestInstance testInstance, String reference, String underTest, List<CommonNameValueTestItem> testList, String logFolder) throws Exception {
        writer.println("JSON Content Testing: " + caption);

        PrintWriter spreadsheetWriter = CommonSupport.getPrintWriter(logFolder + "/" + testInstance.get("SpreadsheetOutput"));
        String path = null;
        String referenceValue = null;
        String underTestValue = null;

        try {
            for (CommonNameValueTestItem testItem : testList) {
                if (testItem.getItemType().equals("TEST")) {
                    path = testItem.getTestPath();
                    //if (!path.startsWith("/")) path = "/" + path;
                    referenceValue = getNestedStringJSONPath(reference, path, "NULL-VALUE");
                    underTestValue = getNestedStringJSONPath(underTest, path, "NULL-VALUE");
                    spreadsheetWriter.println(
                            testItem.getTestCaption() + "\t" +
                                    referenceValue + "\t" +
                                    underTestValue + "\t" +
                                    path
                    );
                    testItem.testValue(writer, testInstance, referenceValue, underTestValue);
                    referenceValue = underTestValue = null;
                } else {
                    spreadsheetWriter.println(testItem.getArgument(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to complete testing a Name/Value pair" +
                    ", path: " + path +
                    ", referenceValue: " + referenceValue +
                    ", underTestValue; " + underTestValue);
        } finally {
            writer.flush();
            spreadsheetWriter.close();
        }
    }



    private String getNestedString(JsonNode node, String path, String nullReturn) throws Exception {
        if (node == null) {
            throw new Exception("Input node to CommonContentTester::getNestedString is null; path: " + path);
        }

        String[] tokens = path.split("/");
        JsonNode n = node;
        for (int i = 0; i < tokens.length - 1; i++) {
            n = n.get(tokens[i]);
            if (n == null) {
                throw new Exception("JsonNode.get returned a null node at index: " + i + ", path specification: " + path);
            }
            if (n.isArray()) {
                n = n.get(0);
            }
        }
        n = n.get(tokens[tokens.length-1]);
        if (n == null && nullReturn == null) {
            throw new Exception("JsonNode.get returned a null node at index: " + (tokens.length-1) + ", path specification: " + path);
        } else if (n == null) {
            return nullReturn;
        }
        return n.asText();
    }


    private String getNestedStringJSONPath(JsonNode node, String path, String nullReturn) throws Exception {
        if (node == null) {
            throw new Exception("Input node to CommonContentTester::getNestedStringJSONPath is null; path: " + path);
        }

        JsonNode n = node.at(path);

        if (n == null && nullReturn == null) {
            throw new Exception("JsonNode.at returned a null node, path specification: " + path);
        } else if (n == null) {
            return nullReturn;
        }
        String x = n.asText();
        return n.asText();
    }


    private String getNestedStringJSONPath(String jsonInput, String path, String nullReturn) throws Exception {
        if (jsonInput == null) {
            throw new Exception("Input string to CommonContentTester::getNestedStringJSONPath is null; path: " + path);
        }

        if (jsonInput.isEmpty()) {
            throw new Exception("Input string to CommonContentTester::getNestedStringJSONPath is empty; path: " + path);
        }

        if (! path.startsWith("$")) {
            throw new Exception ("Input JsonPath does not start with $: " + path);
        }

        boolean indefinitePath = path.contains("?") || path.contains("..");

        String rtn = "Coding error: Value never assigned";

        if (indefinitePath) {
            List<Object> values = JsonPath.read(jsonInput, path);
            if (values == null) {
                rtn = nullReturn;
            } else if (values.size() == 0) {
                rtn = nullReturn;
            } else if (values.size() > 1) {
                rtn = "";
                String delim="";
                for (Object o: values) {
                    rtn += delim + o.toString();
                    delim = ":";
                }
//                throw new Exception("Expected one result when scanning JSON with this path: " + path + ", but received: " + values.size());
            } else {
                Object o = values.get(0);
                rtn = o.toString();
            }
        } else {
            Object obj = null;
            try {
                obj = JsonPath.read(jsonInput, path);
            } catch (com.jayway.jsonpath.PathNotFoundException e) {
                obj = new String("PathNotFound");
            }
            if (obj == null) {
                rtn = null;
            } else {
                rtn = obj.toString();
            }
        }

        if (rtn == null) {
            throw new Exception("JsonPath.read returned a null result; path specification: " + path);
        }

        return rtn;
    }

    private Map<String, JsonNode> createNodeMap(JsonNode node, String keyPath, String caption) throws Exception {
        Map<String, JsonNode> m = new TreeMap<>();
        String keyValue="";
        String[] tokens = keyPath.split("\\+");
        for (int i = 0; i < node.size(); i++) {
            JsonNode n = node.get(i);
            for (String token: tokens) {
                String tempValue = getNestedString(n, token, null);
                if (tempValue == null) {
                    throw new Exception("Was not able to find the value referenced by this path: " + keyPath + " " + caption);
                }
                keyValue += tempValue + ":";
            }
            m.put(keyValue, node.get(i));
            keyValue = "";
        }
        if (node.size() != m.size()) {
            throw new Exception(" When converting a node array to a HashMap, the input/output lengths are different: " + node.size() + "/" + m.size() + " " + caption);
        }
        return m;
    }

    private Map<String, String> createStringMap(JsonNode node, String keyPath, int limit, String caption) throws Exception {
        Map<String, String> m = new TreeMap<>();
        String keyValue="";
        String[] tokens = keyPath.split("\\+");
        if (node != null) {
            for (int i = 0; i < node.size(); i++) {
                JsonNode jsonNode = node.get(i);
                String jsonBlob = jsonNode.toString();
                for (String token : tokens) {
                    String tempValue = getNestedStringJSONPath(jsonBlob, token, null);
                    if (tempValue == null) {
                        throw new Exception("Was not able to find the value referenced by this path: " + keyPath + " " + caption);
                    }
                    if (limit > 0) {
                       tempValue = tempValue.substring(0, limit);
                    }
                    keyValue += tempValue + ":";
                }
                System.out.println("  Key: " + keyValue);
                m.put(keyValue, jsonBlob);
                keyValue = "";
            }
            if (node.size() != m.size()) {
                String errorText = "There is an issue with the data for: " + caption +
                        ", the path that is used to identify unique instances did not work properly. " +
                        "\n Path=" + keyPath +
                        "\n Number of input nodes: " + node.size() +
                        "\n Number of unique key values: " + m.size() +
                        "\n This could be a bad assumption by the person who wrote the path or duplicate data.";
                throw new Exception(errorText);
            }
        }
        return m;
    }
}
