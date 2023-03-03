package edu.wustl.cil.SMM.jackson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Node;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class Utility {

    public static void writeXML(String path, Object o) throws Exception {
        XmlMapper mapper = new XmlMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String xml = mapper.writeValueAsString(o);
        writeStringToFile(path, xml);
    }

    static void writeStringToFile(String filePath, String text) throws Exception {
        PrintWriter writer = new PrintWriter(filePath, "UTF-8");
        writer.println(text);
        writer.close();
    }

    public static Object readXML(String filePath, Class c) throws Exception {
        XmlMapper mapper = new XmlMapper();
        String xml = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        return mapper.readValue(xml, c);
    }

    public static List<FieldConformanceItem> readFieldConformanceItems(File file) throws Exception {
        List<String> stringList = Utility.readFileLines(file);

        List<FieldConformanceItem> fieldConformanceItems = new ArrayList<>();
        for (String s: stringList) {
            FieldConformanceItem item = Utility.parseConformanceItem(s);
            fieldConformanceItems.add(item);
        }
        return fieldConformanceItems;
    }

    public static FieldConformanceItem parseConformanceItem(String line) throws Exception {
        String[] tokens = line.split("\t");
        if (tokens.length < 12) {
            throw new Exception("This line does not have enough tokens to create a proper validation item: " + line +
                    ". There should be at least 10 and up to 12 tab separated tokens.");
        }
        FieldConformanceItem item = new FieldConformanceItem();
        int i = 0;
        item.setOptionality       (tokens[i++]);
        item.setSeverity          (tokens[i++]);
        item.setIteratedOperation (tokens[i++]);
        item.setComparisonOperator(tokens[i++]);
        item.setLabel             (tokens[i++]);
        item.setSource            (tokens[i++]);
        item.setSelector          (tokens[i++]);
        item.setMimeType          (tokens[i++]);
        item.setxPath             (tokens[i++]);
        item.setReferenceValue    (tokens[i++]);
        if (tokens.length > i) {
            item.setDocReference  (tokens[i++]);
        }
        if (tokens.length > i) {
            item.setComments      (tokens[i++]);
        }

        return item;
    }

    public static List<FieldValidationItem> readFieldValidationItems(File file) throws Exception {
        List<String> stringList = Utility.readFileLines(file);

        List<FieldValidationItem> fieldValidationItems = new ArrayList<>();
        for (String s: stringList) {
            String[] tokens = s.split("\t");
            if (tokens.length < 6) {
                throw new Exception("This line does not have enough tokens to create a proper validation item: " + s +
                        ". There should be at least 6 and up to 7 tab separated tokens.");
            }
            FieldValidationItem item = new FieldValidationItem();
            item.setOptionality       (tokens[0]);
            item.setSeverity          (tokens[1]);
            item.setIteratedOperation (tokens[2]);
            item.setComparisonOperator(tokens[3]);
            item.setLabel             (tokens[4]);
            item.setxPath             (tokens[5]);
            item.setReferenceValue    (tokens[6]);
            if (tokens.length > 7) {
                item.setDocReference  (tokens[7]);
            }
            if (tokens.length > 8) {
                item.setComments      (tokens[8]);
            }
            fieldValidationItems.add(item);
        }
        return fieldValidationItems;
    }

    public static List<FieldConsistencyItem> readFieldConsistencyItems(File file) throws Exception {
        List<String> stringList = Utility.readFileLines(file);

        List<FieldConsistencyItem> fieldConsistencyItems = new ArrayList<>();
        for (String s: stringList) {
            String[] tokens = s.split("\t");
            if (tokens.length < 7) {
                throw new Exception("This line does not have enough tokens to create a proper validation item: " + s +
                        ". There should be at least 6 and up to 7 tab separated tokens.");
            }
            FieldConsistencyItem item = new FieldConsistencyItem();
            int i = 0;
            item.setOptionality       (tokens[i++]);
            item.setSeverity          (tokens[i++]);
            item.setIteratedOperation (tokens[i++]);
            item.setComparisonOperator(tokens[i++]);
            item.setLabel             (tokens[i++]);
            item.setInitial           (tokens[i++]);
            item.setInitialSelector   (tokens[i++]);
            item.setInitialMimeType   (tokens[i++]);
            item.setInitialXPath      (tokens[i++]);
            item.setDerived           (tokens[i++]);
            item.setDerivedSelector   (tokens[i++]);
            item.setDerivedMimeType   (tokens[i++]);
            item.setDerivedXPath      (tokens[i++]);

            if (tokens.length > i) {
                item.setDocReference  (tokens[i++]);
            }
            if (tokens.length > i) {
                item.setComments      (tokens[i++]);
            }
            fieldConsistencyItems.add(item);
        }
        return fieldConsistencyItems;
    }

    public static List<String> readFileLines(File file) throws IOException {
        ArrayList<String> outputList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("END")) break;
                if (line.startsWith("#")) continue;
                if (line.replaceAll("\t","").isEmpty())       continue;
                outputList.add(line);
            }
            return outputList;
        } finally {
            reader.close();
        }
    }

    public static void copyFileToFolder(String inputFile, File outputFolder) throws Exception {
        File source = new File(inputFile);
        String name = source.getName();
        File destination = new File(outputFolder, name);
        FileUtils.copyFile(source, destination);
    }

    public static String expandVariable(TestStep testStep, String value) {
        String rtnString = value;
        if (rtnString.contains("$OUTPUT")) {
            rtnString = rtnString.replaceAll("\\$OUTPUT", testStep.getOutputPath());
        }
        if (rtnString.contains("$INPUT")) {
            rtnString = rtnString.replaceAll("\\$INPUT", testStep.getInputPath());
        }
        return rtnString;
    }

    public static String evaluateXPathToString(Node node, String expression) throws Exception {
        while (expression.startsWith("\"")) {
            expression = expression.substring(1, expression.length()-1);
        }

        String s = "";
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression xPathExpression = xPath.compile(expression);
            s = (String) xPathExpression.evaluate(node, XPathConstants.STRING);
        } catch (Exception e) {
            System.out.println("Error while evaluating XPath: " + expression);
            e.printStackTrace();
            s = "";
        }

        return s;
    }

    public static String evaluateXPathFindOneElement(Node node, String expression, String prefix) throws Exception {
        String rtn = "Z";

        int index = 0;
        while ((rtn != null) && !rtn.isEmpty() && !rtn.startsWith(prefix)) {
            rtn = Utility.evaluateXPathToString(node, expression + "[" + ++index + "]");
        }
        return rtn;
    }

    public static String evaluateXPathFindElementRegex(Node node, String expression, String regex) throws Exception {
        String rtn = "Z";

        int index = 0;
        while ((rtn != null) && !rtn.isEmpty() && !rtn.contains(regex)) {
            rtn = Utility.evaluateXPathToString(node, expression + "[" + ++index + "]");
        }
        return rtn;
    }
}
