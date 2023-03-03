package edu.wustl.cil.SMM.jackson;

import edu.wustl.cil.SMM.Reporting.LineItem;
import edu.wustl.cil.SMM.Reporting.TestReport;
import edu.wustl.cil.SMM.XDS.ExtrinsicObject;
import edu.wustl.cil.SMM.XDS.SubmitObjectsRequest;
import edu.wustl.cil.SMM.XDS.Factory;
import org.w3c.dom.Node;

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

public class TestStepExtractXDM extends TestStep {

    public TestStepExtractXDM(TestStepBean testStepBean, String testPath, String inputPath, String outputPath, TestReport testReport) {
        super(testStepBean, testPath, inputPath, outputPath, testReport);
    }

    public TestStepExtractXDM() {
        super();
        stepDescription = "Execute: Extract XDM";
    }

    @Override
    public void executeStep() throws Exception {
        printStepMetadata("\nExecute: ExtractXDM");

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
           testReport.addLineItem(WARNING, "","XDM requires one zip file", inputFolder.getAbsolutePath(), "Folder is empty, but that is OK. Maybe you have not gotten this far yet.", "", "", "", "");
       } else if (fileList.size() > 1) {
           testReport.addLineItem(ERROR, "","XDM requires one zip file", inputFolder.getAbsolutePath(), "This folder has more than one input file. Software is not designed to handle that.", "", "", "", "");
       } else {
           testReport.addLineItem(SUCCESS, "","XDM requires one zip file", inputFolder.getAbsolutePath(), "We have found one file and will try to unzip it: " + fileList.get(0), "", "", "", "");

           unzipFile(fileList.get(0), outputFolder);

           String metadataFile = findFile(outputFolder, "METADATA.XML");
           if (metadataFile == null) {
               testReport.addLineItem(ERROR, "", "Zip file shall contain METADATA.XML", outputFolder.getAbsolutePath(), "We found 0 or more than one metadata file", "", "", "", "");
               return;
           }
           testReport.addLineItem(SUCCESS, "", "Zip file shall contain METADATA.XML", outputFolder.getAbsolutePath(), "Found one file: METADATA.XML", "", "", "", "");

           SubmitObjectsRequest submitObjectsRequest = factory.parseObjectsRequest(metadataFile);
           writeFileExtract(outputFolder, metadataFile, submitObjectsRequest);

           testReport.addLineItem(VERBOSE, "", "", outputFolder + "/objects.csv", "Writing ExtrinsicObjects to csv file for later use", "", "", "", "");
           factory.serializeExtrinsicObjects(submitObjectsRequest, outputFolder + "/objects.csv");

           testReport.addLineItem(VERBOSE, "", "", outputFolder + "/registry.csv", "Writing Registry Package to csv file for later use", "", "", "", "");
           factory.serializeRegistryPackage(submitObjectsRequest, outputFolder + "/registry.csv");

           testReport.addLineItem(VERBOSE, "", "", outputFolder.toString(), "Transforming an HL7 V2 files into XML for later use", "", "", "", "");
           transformHL7V2Files(outputFolder, metadataFile, submitObjectsRequest);
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
                String xmlPath = outputFolder.getAbsolutePath() + "/" + uri + ".xml";
                String hl7V2Path = f.getAbsolutePath() + "/" + uri;
                UtilityHL7.transformHL7V2FileToXML(xmlPath, hl7V2Path);
            }
        }

    }
}
