package edu.wustl.cil.SMM.jackson;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JacksonMain {
    public static void main(String[] args) {
        System.out.println("Hello world");

        try {
            test1();
            test2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void test1() throws Exception {
        TestBean testBean = new TestBean("Name", "Description", "Key Value File");

        testBean = addTestSteps(testBean);

        XmlMapper mapper = new XmlMapper();
        String xml = mapper.writeValueAsString(testBean);
        System.out.println(xml);
    }

    static TestBean addTestSteps(TestBean testBean) {

        testBean.addTestStep(new TestStepBean("step-1", "R", "Extract", "zip", "x1"));
        testBean.addTestStep(new TestStepBean("step-2", "O", "Extract", "multi-part", "x2"));

        return testBean;
    }

    static void test2() throws Exception {
        XmlMapper mapper = new XmlMapper();

        String xml = new String(Files.readAllBytes(Paths.get("/tmp/testbean.xml")), StandardCharsets.UTF_8);
        TestBean testBean = mapper.readValue(xml, TestBean.class);
        String y = xml;

    }
}
