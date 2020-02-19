package edu.wustl.cil.SMM.Common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonTestInstance {
    protected Map<String, String> testParameters;
    protected String testFolder;
    protected String testRequirement;
    protected String testKey;
//    private String testObjectReference;

    protected ArrayList<String> errorLines = new ArrayList<String>();
    protected ArrayList<String> warningLines = new ArrayList<String>();

    protected int errors = 0;
    protected int warnings = 0;

    protected boolean didTestPass = true;


    public CommonTestInstance() {
        testParameters = new HashMap<>();
    }

    public void loadTestInstance(String folder, String indexLine) throws Exception {
//        this.testObjectReference = testObjectReference;
        String[] tokens = indexLine.split("\\t");
        testRequirement = tokens[0];
        testKey = tokens[1];
        this.testFolder = folder + "/" + testKey;

        List<String> tmp;
        Stream<String> lines = Files.lines(Paths.get(folder + "/" + testKey + "/test_plan.txt"));
        tmp = lines.collect(Collectors.toList());

        for (String line: tmp) {
            if ((line.length() == 0) || line.startsWith("#")) {
                continue;
            }
            tokens = line.split("\\t");
            if (tokens.length == 2) {
                testParameters.put(tokens[0], tokens[1]);
            }
        }
    }

    public String getReferenceString() throws Exception {
        String referenceString = null;
        if (this.getReference() == null) {
            return referenceString;
        }
        File f = new File(testFolder + "/" + getReference());
        if (f.exists()) {
            referenceString = new String(Files.readAllBytes(f.toPath()));
        }
        return referenceString;
    }

    public Set<Integer> getAllowedResponseCodes() {
        HashSet<Integer> rtn = new HashSet<>();

        String[] tokens = getResponses().split(",");
        for (String token: tokens) {
            Integer code = new Integer(Integer.parseInt(token));
            rtn.add(code);
        }
        return rtn;
    }


    public List<CommonNameValueTestItem> getNameValueTestItemsFromFileName(String fileName) throws Exception {
        List<CommonNameValueTestItem> rtn = new ArrayList<>();

        if (fileName != null && new File(testFolder + "/" + fileName).exists()) {
            List<String> lines = Files.readAllLines(Paths.get(testFolder + "/" + fileName));
            for (String line: lines) {
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] tokens = line.split("\\t");
                CommonNameValueTestItem item = new CommonNameValueTestItem(tokens);
                rtn.add(item);
            }
        }
        return rtn;
    }

    public List<CommonNameValueTestItem> getNameValueTestItemsFromKey(String nameValueKey) throws Exception {
        List<CommonNameValueTestItem> rtn = new ArrayList<>();

        String fileName = get(nameValueKey);

        if (fileName != null) {
            rtn = getNameValueTestItemsFromFileName(fileName);
        }

        return rtn;
    }


    public void setFailed() {
        didTestPass = false;
    }

    public void addError(String s) {
        if (s != null) errorLines.add(s);
        errors++;
        this.setFailed();
    }

    public void addWarning(String s) {
        if (s != null) warningLines.add(s);
        warnings++;
    }

    // Standard getter/setters

    public String get(String key) {
        return testParameters.get(key);
    }

    public String getTitle() {
        return testParameters.get("Title");
    }

    public String getDescription() {
        return testParameters.get("Description");
    }

    public String getPath() {
        return testParameters.get("Path");
    }

    public Map<String,String> getQuery() {
        Map<String, String> rtn = new TreeMap<>();
        String query = testParameters.get("Query");
        if (query != null) {
            String[] tokens = query.split("&");
            for (String token : tokens) {
                String[] keyValue = token.split("=");
                rtn.put(keyValue[0], keyValue[1]);
            }
        }
        return rtn;
    }

    public String getReference() {
        return testParameters.get("Reference");
    }

    public String getObjectCount() {
        return testParameters.get("ObjectCount");
    }

    public String getResponses() {
        return testParameters.get("Responses");
    }

    public String getBundleMetaData() { return testParameters.get("BundleMetaData"); }

    public Map<String, String> getTestParameters() {
        return testParameters;
    }

    public void setTestParameters(Map<String, String> testParameters) {
        this.testParameters = testParameters;
    }

    public String getTestRequirement() {
        return testRequirement;
    }

    public void setTestRequirement(String testRequirement) {
        this.testRequirement = testRequirement;
    }

    public String getTestFolder() {
        return testFolder;
    }

    public void setTestFolder(String testFolder) {
        this.testFolder = testFolder;
    }

    public String getTestKey() {
        return testKey;
    }

    public void setTestKey(String testKey) {
        this.testKey = testKey;
    }

    public ArrayList<String> getErrorLines() {
        return errorLines;
    }

    public ArrayList<String> getWarningLines() {
        return warningLines;
    }

    public int getErrors() {
        return errors;
    }

    public int getWarnings() {
        return warnings;
    }

    public boolean isDidTestPass() {
        return didTestPass;
    }
}
