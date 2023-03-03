package edu.wustl.cil.SMM.jackson;

import edu.wustl.cil.SMM.Reporting.TestReport;
import edu.wustl.cil.SMM.XDS.ExtrinsicObject;
import edu.wustl.cil.SMM.XDS.SubmitObjectsRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static edu.wustl.cil.SMM.Reporting.LineItem.ItemType.*;

public class TestStepIndexExplodedFiles extends TestStep {

    public TestStepIndexExplodedFiles(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
    }

    public TestStepIndexExplodedFiles() {
        super();
        stepDescription = "Execute: Index Exploded Files";
    }

    @Override
    public void executeStep() throws Exception {
        printStepMetadata("\nExecute: IndexExplodedFiles");

        File inputFolder  = new File(inputPath + "/" + testStepBean.getInputFolder());
        File outputFolder = new File(outputPath + "/" + testStepBean.getOutputFolder());
        clearFolder(outputFolder);
        outputFolder.mkdirs();
        processIndexFile(inputFolder, outputFolder);

        String indexFilePath = findFile(inputFolder, "index.txt");
        Utility.copyFileToFolder(indexFilePath, outputFolder);
        if (indexFilePath != null) {
            File indexFile = new File(indexFilePath);
            List<String> indexList = Utility.readFileLines(indexFile);
            String metadataFile = null;
            for (String line: indexList) {
                String tokens[] = line.split("\t");
                switch (tokens[0]) {
                    case "metadata":
                        metadataFile = tokens[1];
                        break;
                    case "x-application/hl7-v2+er7":
                        break;
                    case "text/xml":
                        break;
                    default:
                        testReport.addLineItem(ERROR, "", "Index file constrained to have defined tokens in first column", "",
                                "Parsing code does not recognize token " + tokens[0] + " from the file: " + indexFilePath, "", "", "", "");
                        break;
                }
                Utility.copyFileToFolder(inputFolder.getAbsolutePath() + "/" + tokens[1], outputFolder);
                SubmitObjectsRequest submitObjectsRequest = factory.parseObjectsRequest(inputFolder, metadataFile);
                writeFileExtract(outputFolder, inputFolder, metadataFile, submitObjectsRequest);

                testReport.addLineItem(VERBOSE, "", "", outputFolder + "/objects.csv", "Writing ExtrinsicObjects to csv file for later use", "", "", "", "");
                factory.serializeExtrinsicObjects(submitObjectsRequest, outputFolder + "/objects.csv");

                testReport.addLineItem(VERBOSE, "", "", outputFolder + "/registry.csv", "Writing Registry Package to csv file for later use", "", "", "", "");
                factory.serializeRegistryPackage(submitObjectsRequest, outputFolder + "/registry.csv");

                testReport.addLineItem(VERBOSE, "", "", outputFolder.toString(), "Transforming an HL7 V2 files into XML for later use", "", "", "", "");
                transformHL7V2Files(outputFolder, inputFolder, submitObjectsRequest);
            }
        }
    }

    private void clearFolder(File folder) throws Exception {
        if (folder.exists()) {
            testReport.addLineItem(VERBOSE, "", "Output folder must exist and be empty", folder.getAbsolutePath(), "Deleting existing output folder", "", "", "", "");
            Path p = Paths.get(folder.getAbsolutePath());
            Files.walk(p)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    private void processIndexFile(File inputFolder, File outputFolder) throws Exception {


    }

    private void unzipFile(File inputZip, File destDir) throws Exception {
        byte[] buffer = new byte[32*1024];

        ZipInputStream zis = new ZipInputStream(new FileInputStream(inputZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            newFile.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        System.out.println(destFile.getAbsolutePath());
        return destFile;
    }

    public String findFile(File folder, String fileName) throws Exception {
        if (! folder.isDirectory()) {
            testReport.addLineItem(ERROR, "", "Folder exists", folder.getAbsolutePath(), "This path is not to a folder or does not exist", "", "", "", "");
            return null;
        }

        List<String> result = null;
        try (Stream<Path> walk = Files.walk(folder.toPath())) {

            result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith("/" + fileName)).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (result.size() == 1) {
            return result.get(0);
        } else if (result.size() == 0)  {
            testReport.addLineItem(ERROR, "", "Expected to find exactly one file", fileName, "Found 0 files by that name", "", "", "", "");
            return null;
        } else {
            testReport.addLineItem(ERROR, "", "Expected to find exactly one file", fileName, "Found multiple files by that name", "", "", "", "");
            return null;
        }
    }

    private void writeFileExtract(File outputFolder, String metadataFile) throws Exception {
        FileExtract fileExtract = new FileExtract();
        fileExtract.setMetadata(metadataFile);
        Utility.writeXML(outputFolder.getAbsolutePath() + "/file_extract.xml", fileExtract);
    }

    private void writeFileExtract(File outputFolder, String metadataFile, SubmitObjectsRequest submitObjectsRequest) throws Exception {
        FileExtract fileExtract = new FileExtract();
        fileExtract.setMetadata(metadataFile);
        String folderPath = (new File(metadataFile)).getParent();

        for (ExtrinsicObject obj: submitObjectsRequest.getExtrinsicObjects()) {
            String mimeType = obj.getMimeType();
            String objID    = Utility.evaluateXPathToString(obj.getNode(), "./@id");
            String filePath = folderPath + "/" + factory.getDocumentEntryURI(obj);
            DocumentEntry d = new DocumentEntry(mimeType, objID, filePath);
            fileExtract.addDocumentEntry(d);
        }

        Utility.writeXML(outputFolder.getAbsolutePath() + "/file_extract.xml", fileExtract);
    }

    private void writeFileExtract(File outputFolder, File inputFolder, String metadataFile, SubmitObjectsRequest submitObjectsRequest) throws Exception {
        writeFileExtract(outputFolder, inputFolder.getAbsolutePath() + "/" + metadataFile, submitObjectsRequest);
    }

    private void transformHL7V2Files(File outputFolder, String metadataFile, SubmitObjectsRequest submitObjectsRequest) throws Exception {
        File f = (new File(metadataFile)).getParentFile();
        List<ExtrinsicObject> extrinsicObjects = submitObjectsRequest.getExtrinsicObjects();
        for (ExtrinsicObject extrinsicObject: extrinsicObjects) {
            if (extrinsicObject.getMimeType().equals("x-application/hl7-v2+er7")) {
                String uri = extrinsicObject.getValue("URI");
                String xmlPath = outputFolder.getAbsolutePath() + "/" + uri + ".xml";
                String hl7V2Path = f.getAbsolutePath() + "/" + uri;
                UtilityHL7.transformHL7V2FileToXML(xmlPath, hl7V2Path);
            }
        }
    }

    private void transformHL7V2Files(File outputFolder, File inputFolder, SubmitObjectsRequest submitObjectsRequest) throws Exception {
        List<ExtrinsicObject> extrinsicObjects = submitObjectsRequest.getExtrinsicObjects();
        for (ExtrinsicObject extrinsicObject: extrinsicObjects) {
            if (extrinsicObject.getMimeType().equals("x-application/hl7-v2+er7")) {
                String uri = extrinsicObject.getValue("URI");
                String xmlPath = outputFolder.getAbsolutePath() + "/" + uri + ".xml";
                String hl7V2Path = inputFolder.getAbsolutePath() + "/" + uri;
                UtilityHL7.transformHL7V2FileToXML(xmlPath, hl7V2Path);
            }
        }
    }
}
