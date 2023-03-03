package edu.wustl.cil.SMM.jackson;

import edu.wustl.cil.SMM.Reporting.TestReport;
import edu.wustl.cil.SMM.XDS.ExtrinsicObject;
import edu.wustl.cil.SMM.XDS.SOAPEnvelope;
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

public class TestStepExtract360XSubmission extends TestStep {

    public TestStepExtract360XSubmission(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
    }

    public TestStepExtract360XSubmission() {
        super();
        stepDescription = "Execute: Extract XDM";
    }

    @Override
    public void executeStep() throws Exception {
        printStepMetadata("\nExecute: Extract360XSubmission");

        File inputFolder  = new File(inputPath + "/" + testStepBean.getInputFolder());
        File outputFolder = new File(outputPath + "/" + testStepBean.getOutputFolder());

        if (outputFolder.exists()) {
            testReport.addLineItem(VERBOSE, "", "Output folder must exist and be empty", outputFolder.getAbsolutePath(), "Deleting existing output folder", "", "", "", "");
            Path p = Paths.get(outputFolder.getAbsolutePath());
            Files.walk(p)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        outputFolder.mkdirs();

        System.out.println("Extract from: " + inputFolder.getAbsolutePath());
        List<File> fileList = listFilesForFolder(inputFolder);

       if (fileList.size() == 0) {
           // We can ignore this; it will be common for the folks to not upload a file
           testReport.addLineItem(WARNING, "", "XDM requires one zip file", inputFolder.getAbsolutePath(), "Folder is empty, but that is OK. Maybe you have not gotten this far yet.", "", "", "", "");
       } else {
           if (containsFileWithExtension(fileList, ".zip", ".ZIP")) {
               extractXDMZip(inputFolder, outputFolder);
           } else if ((fileList.size() == 1) && containsFileWithExtension(fileList, ".xml", ".XML")) {
               extractXDR(inputFolder, outputFolder);
           } else if (containsFileWithExtension(fileList, ".txt", ".txt")) {
               extractExplodedFiles(inputFolder, outputFolder);
           } else {
               throw new Exception("Did not recognize files in folder " + inputPath + " to extract data.");
           }
       }
    }

    private boolean containsFileWithExtension(List<File> fileList, String extension1, String extension2) {
        for (File f: fileList) {
            String fileName = f.getName();
            if (fileName.endsWith(extension1) || fileName.endsWith(extension2))
                return true;
        }
        return false;
    }

    private File findFileWithExtension(List<File> fileList, String extension1, String extension2) throws Exception {
        for (File f: fileList) {
            String fileName = f.getName();
            if (fileName.endsWith(extension1) || fileName.endsWith(extension2))
                return f;
        }
        throw new Exception("Programming error; we expected to find a file with one of these extensions but did not: " + extension1 + " / " + extension2);
    }

    private void extractXDMZip(File inputFolder, File outputFolder) throws Exception {
        testReport.addLineItem(SUCCESS, "","XDM requires one zip file", inputFolder.getAbsolutePath(), "We have found one file and will try to unzip it",
                "", "", "", "");

        File zipFile = findFileWithExtension(listFilesForFolder(inputFolder), ".zip", ".ZIP");
        unzipFile(zipFile, outputFolder);
        testReport.addLineItem(SUCCESS, "", "", inputFolder.getAbsolutePath(),
                "Successfully unzipped the XDM ZIP file",
                "", "", "", "");

        String metadataFile = findFile(outputFolder, "METADATA.XML");
        if (metadataFile == null) {
            testReport.addLineItem(ERROR, "", "", zipFile.getAbsolutePath(),
                    "The zip file did not contain the file METADATA.XML",
                    "", "", "", "");
        }

        SubmitObjectsRequest submitObjectsRequest = factory.parseObjectsRequest(metadataFile);
        writeFileExtract(outputFolder, metadataFile, submitObjectsRequest);
        testReport.addLineItem(SUCCESS, "", "", metadataFile,
                "Successfully parsed metadata file contained in ZIP",
                "", "", "", "");

        transformHL7V2Files(outputFolder, metadataFile, submitObjectsRequest);
        testReport.addLineItem(VERBOSE, "", "", outputFolder.toString(),
                "Transformed HL7 V2 files into XML for later use",
                "", "", "", "");
    }

    private void extractXDR(File inputFolder, File outputFolder) throws Exception {
        testReport.addLineItem(SUCCESS, "","XDR requires one XML file", inputFolder.getAbsolutePath(), "We have found one file and will try to extract content",
                "", "", "", "");

        File xmlFile = findFileWithExtension(listFilesForFolder(inputFolder), ".xml", ".XML");
        SOAPEnvelope soapEnvelope = factory.parseSOAPEnvelope(xmlFile.getAbsolutePath());
        factory.serializeSOAPEnvelope(soapEnvelope, outputFolder);
        testReport.addLineItem(SUCCESS, "", "", xmlFile.getAbsolutePath(),
                "Successfully parsed the SOAP envelope and serialized METADATA.XML and included files",
                "", "", "", "");

        String metadataFile = findFile(outputFolder, "METADATA.XML");
        if (metadataFile == null) {
            testReport.addLineItem(ERROR, "", "XDR processing should have produced metadata.xml in the folder ", outputFolder.getAbsolutePath(), "Something went wrong parsing the XDR SOAP Envelope.", "", "", "", "");
            return;
        }

        SubmitObjectsRequest submitObjectsRequest = factory.parseObjectsRequest(metadataFile);
        testReport.addLineItem(SUCCESS, "", "", metadataFile,
                "Successfully parsed metadata file contained in ZIP",
                "", "", "", "");
        writeFileExtract(outputFolder, metadataFile, submitObjectsRequest);

        transformHL7V2Files(outputFolder, metadataFile, submitObjectsRequest);
        testReport.addLineItem(VERBOSE, "", "", outputFolder.toString(),
                "Transformed HL7 V2 files into XML for later use",
                "", "", "", "");

    }

    private void extractExplodedFiles(File inputFolder, File outputFolder) throws Exception {
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
            }
            SubmitObjectsRequest submitObjectsRequest = factory.parseObjectsRequest(inputFolder, metadataFile);
            writeFileExtract(outputFolder, inputFolder.getAbsolutePath() + "/" + metadataFile, submitObjectsRequest);

            testReport.addLineItem(VERBOSE, "", "", outputFolder.toString(), "Transforming an HL7 V2 files into XML for later use", "", "", "", "");
            transformHL7V2Files(outputFolder, inputFolder.getAbsolutePath() + "/" + metadataFile, submitObjectsRequest);
        }
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

    private void transformHL7V2Files(File outputFolder, String metadataFile, SubmitObjectsRequest submitObjectsRequest) throws Exception {
        File f = (new File(metadataFile)).getParentFile();
        List<ExtrinsicObject> extrinsicObjects = submitObjectsRequest.getExtrinsicObjects();
        for (ExtrinsicObject extrinsicObject: extrinsicObjects) {
            if (extrinsicObject.getMimeType().equals("x-application/hl7-v2+er7")) {
                String uri = extrinsicObject.getValue("URI");
                if ((uri == null) || (uri.equals(""))) {
                    uri = extrinsicObject.getIdentifier();
                }
                String xmlPath = outputFolder.getAbsolutePath() + "/" + uri + ".xml";
                String hl7V2Path = f.getAbsolutePath() + "/" + uri;
                UtilityHL7.transformHL7V2FileToXML(xmlPath, hl7V2Path);
            }
        }

    }
}
