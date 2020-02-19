package edu.wustl.cil.SMM.Common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CommonSupport {

    private CellStyle greenCellStyle = null;
    private CellStyle yellowCellStyle = null;
    private CellStyle orangeCellStyle  = null;
    private CellStyle redCellStyle  = null;

    static public PrintWriter getPrintWriter(String path) throws Exception {
        File f = new File(path);
        PrintWriter writer = new PrintWriter(new FileWriter(f));
        return writer;
    }

    public void writeSpreadsheet(String path, List<CommonNameValueTestItem> testItems) throws Exception {
        Workbook w = new XSSFWorkbook();
        Sheet sheet = w.createSheet();
        createAllCellStyles(w);
        int i = 0;
        Row headerRow = sheet.createRow(i++);
        fillHeaderRow(headerRow);

        for (CommonNameValueTestItem item: testItems) {
            switch (item.getItemType()) {
                case "DIRECTIVE":
                    Row directiveRow = sheet.createRow(i++);
                    fillDirectiveRow(directiveRow, item);
                    break;
                case "TEST":
                    System.out.println("" +
                            item.getTestResult() + "\t" +
                            item.getTestOperator() + "\t" +
                            item.getReference() + "\t" +
                            item.getUnderTest() + "\t" +
                            item.getTestPath()
                    );
                    Row r = sheet.createRow(i++);
                    fillResultRow(r, item);
                    break;
                default:
                    throw new Exception("Unrecognized CommonNameValueTestItem.getItemType() value: " + item.getItemType());
            }
        }

        FileOutputStream outputStream = new FileOutputStream(path);
        w.write(outputStream);
        w.close();
    }


    public void writeSpreadsheet(String path, CommonTestInstance testInstance, Map<String, List<CommonNameValueTestItem>> mapOfTestItems) throws Exception {
        Workbook w = new XSSFWorkbook();
        Sheet sheet = w.createSheet();
        createAllCellStyles(w);
        int i = 0;
        Row headerRow = sheet.createRow(i++);
        fillHeaderRow(headerRow);

        Set<String> mapKeys = mapOfTestItems.keySet();

        for (String key: mapKeys) {
            List<CommonNameValueTestItem> testItems = mapOfTestItems.get(key);

            for (CommonNameValueTestItem item : testItems) {
                switch (item.getItemType()) {
                    case "DIRECTIVE":
                        Row directiveRow = sheet.createRow(i++);
                        fillDirectiveRow(directiveRow, item);
                        break;
                    case "TEST":
                        System.out.println("" +
                                item.getTestResult() + "\t" +
                                item.getTestOperator() + "\t" +
                                item.getReference() + "\t" +
                                item.getUnderTest() + "\t" +
                                item.getTestPath()
                        );
                        Row r = sheet.createRow(i++);
                        fillResultRow(r, key, item);
                        break;
                    default:
                        throw new Exception("Unrecognized CommonNameValueTestItem.getItemType() value: " + item.getItemType());
                }
            }
        }

        FileOutputStream outputStream = new FileOutputStream(path);
        w.write(outputStream);
        w.close();
    }

    private void fillHeaderRow(Row row) {
        int cellIndex = 0;

        Cell categoryCell  = row.createCell(cellIndex++, CellType.STRING);
        Cell keyCell       = row.createCell(cellIndex++, CellType.STRING);
        Cell resultCell    = row.createCell(cellIndex++, CellType.STRING);
        Cell operatorCell  = row.createCell(cellIndex++, CellType.STRING);
        Cell captionCell   = row.createCell(cellIndex++, CellType.STRING);
        Cell referenceCell = row.createCell(cellIndex++, CellType.STRING);
        Cell underTestCell = row.createCell(cellIndex++, CellType.STRING);
        Cell pathCell      = row.createCell(cellIndex++, CellType.STRING);


        categoryCell. setCellValue("Category");
        keyCell.      setCellValue("Key");
        resultCell.   setCellValue("Result");
        operatorCell. setCellValue("Operator");
        captionCell.  setCellValue("Caption");
        referenceCell.setCellValue("Reference");
        underTestCell.setCellValue("Under Test");
        pathCell.     setCellValue("Path Expression");
    }


    private void fillDirectiveRow(Row row, CommonNameValueTestItem item) {
        int cellIndex = 0;

        Cell categoryCell  = row.createCell(cellIndex++, CellType.STRING);
        Cell keyCell       = row.createCell(cellIndex++, CellType.STRING);
        Cell resultCell    = row.createCell(cellIndex++, CellType.STRING);
        Cell operatorCell  = row.createCell(cellIndex++, CellType.STRING);
        Cell captionCell   = row.createCell(cellIndex++, CellType.STRING);
        Cell referenceCell = row.createCell(cellIndex++, CellType.STRING);
        Cell underTestCell = row.createCell(cellIndex++, CellType.STRING);
        Cell pathCell      = row.createCell(cellIndex++, CellType.STRING);

        categoryCell.  setCellValue("");
        keyCell.       setCellValue("");
        resultCell.    setCellValue("");
        operatorCell.  setCellValue("");
        captionCell.   setCellValue("");
        referenceCell. setCellValue("");
        underTestCell. setCellValue("");
        pathCell.      setCellValue("");

        switch(item.getArgument(0)) {
            case "TITLE":
                categoryCell.setCellValue(item.getArgument(1));
                break;
            default:
                categoryCell.setCellValue(item.getArgument(0));
                break;
        }
    }

    private void fillResultRow(Row row, CommonNameValueTestItem item) {
        int cellIndex = 0;

        Cell categoryCell = row.createCell(cellIndex++, CellType.STRING);

        Cell keyCell       = row.createCell(cellIndex++, CellType.STRING);
        Cell resultCell    = row.createCell(cellIndex++, CellType.STRING);
        Cell operatorCell  = row.createCell(cellIndex++, CellType.STRING);
        Cell captionCell   = row.createCell(cellIndex++, CellType.STRING);
        Cell referenceCell = row.createCell(cellIndex++, CellType.STRING);
        Cell underTestCell = row.createCell(cellIndex++, CellType.STRING);
        Cell pathCell      = row.createCell(cellIndex++, CellType.STRING);

        switch (item.getTestResult()) {
            case "PASS":
                resultCell.setCellStyle(greenCellStyle);
                break;
            case "ERROR":
                resultCell.setCellStyle(orangeCellStyle);
                break;
            case "WARN":
            case "WARNING":
                resultCell.setCellStyle(yellowCellStyle);
                break;
            case "FAIL":
            case "FAILED":
                resultCell.setCellStyle(orangeCellStyle);
                break;
            default:
        }

        keyCell.      setCellValue("");
        resultCell.   setCellValue(item.getTestResult());
        operatorCell. setCellValue(item.getTestOperator());
        captionCell.  setCellValue(item.getTestCaption());
        referenceCell.setCellValue(item.getReference());
        underTestCell.setCellValue(item.getUnderTest());
        pathCell.     setCellValue(item.getTestPath());

    }


    private void fillResultRow(Row row, String key, CommonNameValueTestItem item) {
        int cellIndex = 0;

        Cell categoryCell  = row.createCell(cellIndex++, CellType.STRING);
        Cell keyCell       = row.createCell(cellIndex++, CellType.STRING);
        Cell resultCell    = row.createCell(cellIndex++, CellType.STRING);
        Cell operatorCell  = row.createCell(cellIndex++, CellType.STRING);
        Cell captionCell   = row.createCell(cellIndex++, CellType.STRING);
        Cell referenceCell = row.createCell(cellIndex++, CellType.STRING);
        Cell underTestCell = row.createCell(cellIndex++, CellType.STRING);
        Cell pathCell      = row.createCell(cellIndex++, CellType.STRING);

        switch (item.getTestResult()) {
            case "PASS":
                resultCell.setCellStyle(greenCellStyle);
                break;
            case "ERROR":
                resultCell.setCellStyle(orangeCellStyle);
                break;
            case "WARN":
            case "WARNING":
                resultCell.setCellStyle(yellowCellStyle);
                break;
            case "FAIL":
            case "FAILED":
                resultCell.setCellStyle(orangeCellStyle);
                break;
            default:
        }

        categoryCell. setCellValue("");
        keyCell.      setCellValue(key);
        resultCell.   setCellValue(item.getTestResult());
        operatorCell. setCellValue(item.getTestOperator());
        captionCell.  setCellValue(item.getTestCaption());
        referenceCell.setCellValue(item.getReference());
        underTestCell.setCellValue(item.getUnderTest());
        pathCell.     setCellValue(item.getTestPath());
    }


    private void createAllCellStyles(Workbook w) {
        createGreenCellStyle(w);
        createYellowCellStyle(w);
        createOrangCellStyle(w);
        createRedCellStyle(w);
    }
    private void createGreenCellStyle(Workbook w) {
        greenCellStyle = w.createCellStyle();
        greenCellStyle.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        greenCellStyle.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        greenCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    private void createYellowCellStyle(Workbook w) {
        yellowCellStyle = w.createCellStyle();
        yellowCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        yellowCellStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
        yellowCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    private void createOrangCellStyle(Workbook w) {
        orangeCellStyle = w.createCellStyle();
        orangeCellStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        orangeCellStyle.setFillBackgroundColor(IndexedColors.ORANGE.getIndex());
        orangeCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    private void createRedCellStyle(Workbook w) {
        redCellStyle = w.createCellStyle();
        redCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        redCellStyle.setFillBackgroundColor(IndexedColors.RED.getIndex());
        redCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }
}
