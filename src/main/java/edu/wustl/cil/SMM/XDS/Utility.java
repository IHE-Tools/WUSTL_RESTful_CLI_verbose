package edu.wustl.cil.SMM.XDS;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Utility {

    private static DocumentBuilder b = null;
    public static Document parseDocument(String path) throws Exception {
        FileInputStream fileIS = new FileInputStream(path);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        if (b == null) {
            b = builderFactory.newDocumentBuilder();
        }
//        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document xmlDocument = b.parse(fileIS);
        fileIS.close();
        return xmlDocument;
    }

    public static NodeList evaluateXPath(Document document, String expression) throws Exception {
        XPath xPath = XPathFactory.newInstance().newXPath();
        return (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
    }

    public static NodeList evaluateXPath(Node node, String expression) throws Exception {
        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression xPathExpression = xPath.compile(expression);
        String s = (String) xPathExpression.evaluate(node, XPathConstants.STRING);
        return (NodeList) xPath.compile(expression).evaluate(node, XPathConstants.NODESET);
    }

    public static String evaluateXPathToString(Node node, String expression) throws Exception {
        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression xPathExpression = xPath.compile(expression);
        String s = (String) xPathExpression.evaluate(node, XPathConstants.STRING);
        return s;
    }

    static void writeStringToFile(String filePath, String text) throws Exception {
        PrintWriter writer = new PrintWriter(filePath, "UTF-8");
        writer.println(text);
        writer.close();
    }

    static void writeBytesToFile(String filePath, byte[] data) throws Exception {
        OutputStream stream = new FileOutputStream(filePath);
        stream.write(data);
        stream.close();
    }
}
