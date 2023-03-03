package edu.wustl.cil.SMM.XDS;

import org.apache.xmlbeans.impl.util.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Factory {

    public SubmitObjectsRequest parseObjectsRequest(String path) throws Exception {
        SubmitObjectsRequest request = new SubmitObjectsRequest();

        Document d = Utility.parseDocument(path);
        request.setDocument(d);
        addExtrinsicObjects(request, d);
        addRegistryPackage( request, d);
        return request;
    }

    public SubmitObjectsRequest parseObjectsRequest(File folder, String path) throws Exception {
        return parseObjectsRequest(folder.getAbsolutePath() + "/" + path);
    }

    public SOAPEnvelope parseSOAPEnvelope(String path) throws Exception {
        SOAPEnvelope soapEnvelope = new SOAPEnvelope();
        Document d = Utility.parseDocument(path);
        soapEnvelope.setDocument(d);
        String basePath = "/Envelope/Body/ProvideAndRegisterDocumentSetRequest";
        NodeList nodes = Utility.evaluateXPath(d, basePath + "/SubmitObjectsRequest");
        if ((nodes==null) || (nodes.getLength() != 1)) {
            throw new Exception("SOAP Envelope parsing failure. We expected to get exactly one node with this path: " + basePath + "/SubmitObjectsRequest in the file: " + path);
        }
        soapEnvelope.setSubmitObjectsRequestNode(nodes.item(0));

        nodes = Utility.evaluateXPath(d, basePath + "/Document");
        // No check here; maybe they did not include any documents
        soapEnvelope.setDocumentNodeList(nodes);

        return soapEnvelope;
    }

    public void serializeSOAPEnvelope(SOAPEnvelope soapEnvelope, File outputFolder) throws Exception {
        StringWriter indexWriter = new StringWriter(200);
        Node node = soapEnvelope.getSubmitObjectsRequestNode();
        writeXMLToFile(node, outputFolder + "/METADATA.XML");
        indexWriter.write("metadata\t" + "METADATA.XML");
        NodeList nodeList = soapEnvelope.getDocumentNodeList();
        for (int index =0; index < nodeList.getLength(); index++) {
            Node documentNode = nodeList.item(index);
            writeIncludedDocument(documentNode, outputFolder);
        }
        Utility.writeStringToFile(outputFolder.getAbsolutePath() + "/" + "index.txt", indexWriter.toString());
    }

    private void writeXMLToFile(Node node, String path) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(node);
        transformer.transform(source, result);
        String xmlString = result.getWriter().toString();
        Utility.writeStringToFile(path, xmlString);
    }

    private void writeIncludedDocument(Node node, File folderPath) throws Exception {
        NamedNodeMap nodeMap = node.getAttributes();
        String id = nodeMap.getNamedItem("id").getNodeValue();
        byte[] data = Base64.decode(node.getTextContent().getBytes());
        Utility.writeBytesToFile(folderPath + "/" + id, data);
    }

    private void addExtrinsicObjects(SubmitObjectsRequest request, Document document) throws Exception {
        NodeList extrinsicObjectNodeList = Utility.evaluateXPath(document,"/SubmitObjectsRequest/RegistryObjectList/ExtrinsicObject");
        ArrayList<ExtrinsicObject> extrinsicObjectArrayList = new ArrayList<>();
        int x = extrinsicObjectNodeList.getLength();
        for (int i = 0; i < extrinsicObjectNodeList.getLength(); i++) {
            Node node = extrinsicObjectNodeList.item(i);
            NamedNodeMap nodeAttributes = node.getAttributes();
            String mimeType = nodeAttributes.getNamedItem("mimeType").getNodeValue();
            String identifier = Utility.evaluateXPathToString(node, "./@id");
            ExtrinsicObject obj = new ExtrinsicObject(node, mimeType, identifier);
            fillExtrinsicObject(obj);
            extrinsicObjectArrayList.add(obj);
        }

        request.setExtrinsicObjects(extrinsicObjectArrayList);
    }

    public static Node getRegistryPackage(String path) throws Exception {
        Document document = Utility.parseDocument(path);
        NodeList registryPackageNodeList = Utility.evaluateXPath(document,"/SubmitObjectsRequest/RegistryObjectList/RegistryPackage");
        if (registryPackageNodeList.getLength() != 1) {
            throw new Exception("Expected 1 RegistryPackage node but found: " + registryPackageNodeList.getLength());
        }
        return registryPackageNodeList.item(0);
    }

    public static Node getExtrinsicObject(String path, String mimeType) throws Exception {
        Document document = Utility.parseDocument(path);
        String xPath = "/SubmitObjectsRequest/RegistryObjectList/ExtrinsicObject[@mimeType='" + mimeType+"']";
        NodeList nodeList = Utility.evaluateXPath(document,xPath);
        if (nodeList.getLength() != 1) {
            throw new Exception("Expected 1 ExtrinsicObject node but found: " + nodeList.getLength());
        }
        return nodeList.item(0);
    }

    public static String getExtrinsicObjectFilePath(String metadataPath, String mimeType) throws Exception {
        Node node = getExtrinsicObject(metadataPath, mimeType);
        String uri = Utility.evaluateXPathToString(node, "./Slot[@name='URI']/ValueList/Value");
        if (uri.equals("")) {
            uri = Utility.evaluateXPathToString(node, "./@id");
        }
        File f = (new File(metadataPath)).getParentFile();
        return f.getAbsolutePath() + "/" + uri;
    }

    private void addRegistryPackage(SubmitObjectsRequest request, Document document) throws Exception {
        NodeList registryPackageNodeList = Utility.evaluateXPath(document,"/SubmitObjectsRequest/RegistryObjectList/RegistryPackage");
        if (registryPackageNodeList.getLength() != 1) {
            throw new Exception("Expected 1 RegistryPackage node but found: " + registryPackageNodeList.getLength());
        }

        Node node = registryPackageNodeList.item(0);
        NamedNodeMap nodeAttributes = node.getAttributes();
        String identifier = Utility.evaluateXPathToString(node, "./@id");
        RegistryPackage registryPackage = new RegistryPackage(node, identifier);
        fillRegistryPackage(registryPackage);

        request.setRegistryPackage(registryPackage);
    }

    public void fillExtrinsicObject(ExtrinsicObject extrinsicObject) throws Exception {
        String[][] keyPathStrings = {

                {"id",                 "./@id"},
                {"mimeType",           "./@mimeType"},
                {"objectType",         "./@objectType"},
                {"creationTime",       "./Slot[@name='creationTime']/ValueList/Value"},
                {"sourcePatientId",    "./Slot[@name='sourcePatientId']/ValueList/Value"},
                {"sourcePatientInfo1", "./Slot[@name='sourcePatientInfo']/ValueList/Value[1]"},
                {"sourcePatientInfo2", "./Slot[@name='sourcePatientInfo']/ValueList/Value[2]"},
                {"sourcePatientInfo3", "./Slot[@name='sourcePatientInfo']/ValueList/Value[3]"},
                {"sourcePatientInfo4", "./Slot[@name='sourcePatientInfo']/ValueList/Value[4]"},
                {"URI",                "./Slot[@name='URI']/ValueList/Value"},

        };
        Node node = extrinsicObject.getNode();

        int length = keyPathStrings.length;
        for (int i = 0; i < keyPathStrings.length; i++) {
            extrinsicObject.putValue(keyPathStrings[i][0], getSingleString(node,keyPathStrings[i][1]));
        }
    }

    public void fillRegistryPackage(RegistryPackage registryPackage) throws Exception {
        String[][] keyPathStrings = {

                {"id",                 "./@id"},
                {"objectType",         "./@objectType"},
                {"status",             "./@status"},
                {"submissionTime",     "./Slot[@name='submissionTime']/ValueList/Value"},
                {"intendedRecipient",  "./Slot[@name='intendedRecipient']/ValueList/Value"},
                {"referenceIdList",            "./Slot[@name='urn:ihe:iti:xds:2013:referenceIdList']/ValueList/Value"},
                {"Classification.objectType",  "./Classification/@objectType"},
                {"Classification.classificationScheme",  "./Classification/@classificationScheme"},
                {"Classification.objectType",  "./Classification/@objectType"},


//                {"submissionTime",     "./Slot[@name='submissionTime']/ValueList/Value"},
//                {"submissionTime",     "./Slot[@name='submissionTime']/ValueList/Value"},
//                {"submissionTime",     "./Slot[@name='submissionTime']/ValueList/Value"},


        };
        Node node = registryPackage.getNode();

        int length = keyPathStrings.length;
        for (int i = 0; i < keyPathStrings.length; i++) {
            registryPackage.putValue(keyPathStrings[i][0], getSingleString(node,keyPathStrings[i][1]));
        }
    }

    public void serializeExtrinsicObjects(SubmitObjectsRequest submitObjectsRequest, String outputPath) throws Exception {
        PrintWriter writer = new PrintWriter(outputPath, "UTF-8");

        ArrayList<String> documentIdentifiers = new ArrayList<>();
        List<ExtrinsicObject> extrinsicObjects = submitObjectsRequest.getExtrinsicObjects();
        writer.print("Key");

        for (ExtrinsicObject obj: extrinsicObjects) {
            Node node = obj.getNode();
            String objID = getSingleString(node, "./@id");
            documentIdentifiers.add(objID);
            writer.print("\t" + objID);
        }
        writer.println();

        if (extrinsicObjects.size() != 0) {
            Set<String> keys = extrinsicObjects.get(0).getKeyValueMap().keySet();
            for (String key: keys) {
                writer.print(key);
                for (ExtrinsicObject extrinsicObject: extrinsicObjects) {
                    writer.print("\t" + extrinsicObject.getValue(key));
                }
                writer.println();
            }
         }
        writer.close();
    }

    public void serializeRegistryPackage(SubmitObjectsRequest submitObjectsRequest, String outputPath) throws Exception {
        PrintWriter writer = new PrintWriter(outputPath, "UTF-8");

        RegistryPackage registryPackage =  submitObjectsRequest.getRegistryPackage();

        Node node = registryPackage.getNode();
        String objID = getSingleString(node, "./@id");
        writer.println("Key\t" + objID);

        Set<String> keys = registryPackage.getKeyValueMap().keySet();
        for (String key: keys) {
            String value = registryPackage.getValue(key);
            writer.println(key + "\t" + registryPackage.getValue(key));
        }
        writer.close();
    }

    public ExtrinsicObject[] deserializeExtrinsicObjects(String inputPath) throws Exception {
        BufferedReader b = new BufferedReader(new FileReader(new File(inputPath)));

        ExtrinsicObject[] extrinsicObjects = null;

        String readLine = "";

        while ((readLine = b.readLine()) != null) {
//            System.out.println(readLine);
            extrinsicObjects = parseExtrinsicObjectLine(extrinsicObjects, readLine);
        }
        return extrinsicObjects;
    }

    public RegistryPackage deserializeRegistryPackage(String inputPath) throws Exception {
        BufferedReader b = new BufferedReader(new FileReader(new File(inputPath)));

        RegistryPackage registryPackage = null;
        String readLine = "";
        while ((readLine = b.readLine()) != null) {
            System.out.println(readLine);
            registryPackage = parseRegistryObjectLine(registryPackage, readLine);
        }
        return registryPackage;
    }

    public String getDocumentEntryURI(ExtrinsicObject extrinsicObject) throws Exception {
        String xPath = "./Slot[@name='URI']/ValueList/Value";
        Node n = extrinsicObject.getNode();
        String uri = Utility.evaluateXPathToString(n, xPath);
        return uri;
    }

    private ExtrinsicObject[] parseExtrinsicObjectLine(ExtrinsicObject[] extrinsicObjects, String line) throws Exception {
        String[] tokens = line.split("\t");
        if (extrinsicObjects == null) {
            extrinsicObjects = new ExtrinsicObject[tokens.length-1];
            for (int i = 0; i < tokens.length-1; i++) {
                extrinsicObjects[i] = new ExtrinsicObject(null, "", "");
            }
        }
        if (extrinsicObjects.length != tokens.length-1) {
            throw new Exception ("The component count in this line does not match the component count in previous lines: " + line);
        }
        String key = tokens[0];
        for (int i = 1; i < tokens.length; i++) {
            extrinsicObjects[i-1].putValue(key, tokens[i]);
            if (key.equals("mimeType")) {
                extrinsicObjects[i-1].setMimeType(tokens[i]);
            }
        }
        return extrinsicObjects;
    }

    private RegistryPackage parseRegistryObjectLine(RegistryPackage registryPackage, String line) throws Exception {
        String[] tokens = line.split("\t");
        if (registryPackage == null) {
            registryPackage = new RegistryPackage();
        }
        if (tokens.length == 0) {
            // Assume an empty line and nothing to do
        } else if (tokens.length > 2) {
            throw new Exception("Expected two tokens on this RegistryPackage line but found more: " + line);
        } else if (tokens.length == 1) {
            registryPackage.putValue(tokens[0], "");
        } else {
            registryPackage.putValue(tokens[0], tokens[1]);
        }
        return registryPackage;
    }

    private void writeExtrinsicObjectRow(PrintWriter writer, String key, List<ExtrinsicObject> extrinsicObjects, List<String> documentIdentifiers,  String expression) throws Exception {
        writer.print(key);

        int index = 0;
        for (ExtrinsicObject extrinsicObject: extrinsicObjects) {
            String id = documentIdentifiers.get(index++);
            Node node = extrinsicObject.getNode();
            String value = getSingleString(node, expression);
            writer.print("," + value);
        }
        writer.println();
    }

    private String getSingleString(Node node, String expression) throws Exception {
        return Utility.evaluateXPathToString(node, expression);
    }
}
