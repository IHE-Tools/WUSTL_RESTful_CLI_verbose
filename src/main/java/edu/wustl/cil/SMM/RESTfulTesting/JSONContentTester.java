package edu.wustl.cil.SMM.RESTfulTesting;

import com.fasterxml.jackson.databind.JsonNode;
//import edu.wustl.cil.TestingWIA.ElementTestItem;
//import edu.wustl.cil.TestingWIA.TestInstance;
//import org.nrg.xnat.dicom.model.DicomAttribute;
//import org.nrg.xnat.dicom.model.DicomObject;

import java.io.PrintWriter;
import java.util.*;

public class JSONContentTester {

    public void testJsonArray(PrintWriter writer, String caption, RESTfulTestInstance testInstance, JsonNode referenceArray, JsonNode underTestArray, List<NameValueTestItem> testList, String keyPath, int limit) throws Exception {
        writer.println("\nJSON Array Testing: " + caption);
        int referenceSize = referenceArray.size();
        int underTestSize = 0;
        if (underTestArray != null) {
            underTestSize = underTestArray.size();
        }
        if (underTestSize >= referenceSize) {
            writer.println("Array under test has at least as many elements as reference array: " + underTestSize + "/" + referenceSize);
        }

        Map<String, JsonNode> referenceMap;
        Map<String, JsonNode> underTestMap;
        try {
            referenceMap = createMap(referenceArray, keyPath, limit, "Reference Array");
            underTestMap = createMap(underTestArray, keyPath, limit, "Under Test Array");
        } catch (Exception e) {
            writer.flush();
            e.printStackTrace();
            throw new Exception("Unable to create a map of JsonNodes from reference or underTest array using path: " + keyPath);
        }

        Set<String> referenceKeys = referenceMap.keySet();
        for (String key: referenceKeys) {
            JsonNode referenceNode = referenceMap.get(key);
            JsonNode underTestNode = underTestMap.get(key);

            writer.println("\nTesting content for object selected by: " + keyPath + ", unique value: " + key);
            if (underTestNode != null) {
                testJsonNameValues(writer, caption, testInstance, referenceNode, underTestNode, testList);
            } else {
                writer.println("No corresponding object found in the array of test objects");
                testInstance.addError("No corresponding object found in the array of test objects, path: " + keyPath + ", value: " + key);
            }
        }
    }


    public void testJsonNameValues(PrintWriter writer, String caption, RESTfulTestInstance testInstance, JsonNode reference, JsonNode underTest, List<NameValueTestItem> testList) throws Exception {
        writer.println("JSON Content Testing: " + caption);

        String path = null;
        String referenceValue = null;
        String underTestValue = null;

        try {
            for (NameValueTestItem testItem : testList) {
                path = testItem.getPath();
                referenceValue = getNestedString(reference, path, "NULL-VALUE", "reference");
                underTestValue = getNestedString(underTest, path, "NULL-VALUE", "under test");
                testItem.testValue(writer, testInstance, referenceValue, underTestValue);
                referenceValue = underTestValue = null;
            }
        } catch (Exception e) {
            writer.flush();
            throw new Exception("Failed to complete testing a Name/Value pair" +
                    ", path: " + path +
                    ", referenceValue: " + referenceValue +
                    ", underTestValue; " + underTestValue);
        }
    }

    private String getNestedString(JsonNode node, String path, String nullReturn, String context) throws Exception {
        if (node == null) {
            throw new Exception("Input node to JSONContesterJava::getNestedString is null; path: " + path);
        }
        String[] tokens = path.split("/");
        JsonNode n = node;
        for (int i = 0; i < tokens.length - 1; i++) {
            n = n.get(tokens[i]);
            if (n == null) {
                throw new Exception("JsonNode.get returned a null node at index: " + i + ", path specification: " + path + ", context: " + context);
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

    private Map<String, JsonNode> createMap(JsonNode node, String keyPath, int limit, String caption) throws Exception {
        Map<String, JsonNode> m = new TreeMap<>();
        if (node != null) {
            String keyValue = "";
            String[] tokens = keyPath.split(":");
            for (int i = 0; i < node.size(); i++) {
                JsonNode n = node.get(i);
                for (String token : tokens) {
                    String tempValue = getNestedString(n, token, null, caption);
                    if (tempValue == null) {
                        throw new Exception("Was not able to find the value referenced by this path: " + keyPath + " " + caption);
                    }
                    if (limit > 0) {
                        tempValue = tempValue.substring(0, limit);
                    }
                    keyValue += tempValue + ":";
                }
                m.put(keyValue, node.get(i));
                keyValue = "";
            }
            if (node.size() != m.size()) {
                throw new Exception(" When converting a node array to a HashMap, the input/output lengths are different: " + node.size() + "/" + m.size() + " " + caption);
            }
        }
        return m;
    }



}
