package edu.wustl.cil.SMM.jackson;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;

public class TestBean {
    private String testName;
    private String testDescription;
    private String keyValueFile;
    private ArrayList<TestStepBean> testStepBeanArrayList;

    public TestBean() {
        testStepBeanArrayList = new ArrayList<>();

    }

    public TestBean(String testName, String testDescription, String keyValueFile) {
        this.testName = testName;
        this.testDescription = testDescription;
        this.keyValueFile = keyValueFile;

        this.testStepBeanArrayList = new ArrayList<>();
    }

    public static TestBean readTestBean(String filePath) throws Exception {
        XmlMapper mapper = new XmlMapper();
        String xml = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        return mapper.readValue(xml, TestBean.class);
    }

    // Getters and Setters

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
    }

    public String getKeyValueFile() {
        return keyValueFile;
    }

    public void setKeyValueFile(String keyValueFile) {
        this.keyValueFile = keyValueFile;
    }

    public ArrayList<TestStepBean> getTestStepBeanArrayList() {
        return testStepBeanArrayList;
    }

    public void setTestStepBeanArrayList(ArrayList<TestStepBean> testStepBeanArrayList) {
        this.testStepBeanArrayList = testStepBeanArrayList;
    }

    public void addTestStep(TestStepBean testStepBean) {
        testStepBeanArrayList.add(testStepBean);
    }
}
