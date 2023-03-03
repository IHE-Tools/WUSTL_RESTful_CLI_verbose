package edu.wustl.cil.SMM.jackson;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.dcm4che3.hl7.HL7Message;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.dcm4che3.hl7.HL7Charset;
import org.dcm4che3.hl7.HL7Parser;
import org.dcm4che3.hl7.HL7Segment;

public class UtilityHL7 {
    static void transformHL7V2FileToXML(String outputPath, String inputPath) throws Exception {
        FileInputStream fis = new FileInputStream(inputPath);
        byte[] buf = new byte[25600];
        int len = fis.read(buf);
        HL7Segment msh = HL7Segment.parseMSH(buf, buf.length);
        String charset = "";
        String charsetName = HL7Charset.toCharsetName(msh.getField(17, charset));
        Reader reader = new InputStreamReader(
                new SequenceInputStream(
                        new ByteArrayInputStream(buf, 0, len), fis),
                charsetName);
        TransformerHandler th = ((SAXTransformerFactory)
                TransformerFactory.newInstance()).newTransformerHandler();

        th.getTransformer().setOutputProperty(OutputKeys.INDENT,"yes");
        th.setResult(new StreamResult(new File(outputPath)));
        HL7Parser hl7Parser = new HL7Parser(th);
        hl7Parser.setIncludeNamespaceDeclaration(true);
        hl7Parser.parse(reader);

//        HL7Message msg = HL7Message.parse(buf, buf.length, charsetName);
//        HL7Segment pid = msg.getSegment("PID");
//        String     pid3= pid.getField(3, "");
//        String     x = "";
    }

    public static Node transformHL7V2FileToNode(String inputPath) throws Exception {
        File temp = File.createTempFile("hl7v2-", ".xml");
        transformHL7V2FileToXML(temp.getAbsolutePath(), inputPath);
        Document document = edu.wustl.cil.SMM.XDS.Utility.parseDocument(temp.getAbsolutePath());
        Node n = document.getFirstChild();
        String x = n.getNodeName();
        return n;
    }

    public static String getField(String inputPath, String field) throws Exception {
        FileInputStream fis = new FileInputStream(inputPath);
        byte[] buf = new byte[25600];
        int len = fis.read(buf);
        HL7Segment msh = HL7Segment.parseMSH(buf, buf.length);
        String charset = "";
        String charsetName = HL7Charset.toCharsetName(msh.getField(17, charset));

        HL7Message msg = HL7Message.parse(buf, buf.length, charsetName);

        String[] tokens = field.split("\\.");
        HL7Segment segment = msg.getSegment(tokens[0]);
        String rtn = segment.getField(Integer.parseInt(tokens[1]), "");
        if (tokens.length > 2) {
            int index = Integer.parseInt(tokens[2]) - 1;
            String[] fieldTokens = rtn.split("\\^");
            if (index < fieldTokens.length) {
                rtn = fieldTokens[index];
            } else {
                rtn = "Out of bounds requests for " + field;
            }

        }

        return rtn;
    }

}
