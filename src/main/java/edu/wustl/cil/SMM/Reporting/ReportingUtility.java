package edu.wustl.cil.SMM.Reporting;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

public class ReportingUtility {

    public static void writeTestReportXML(TestReport testReport, String path) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(FAIL_ON_EMPTY_BEANS);
        xmlMapper.enable(INDENT_OUTPUT);
        xmlMapper.writeValue(new File(path), testReport);
    }

    public static void writeTestReportXLS(TestReport testReport, String path) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Map<String, CellStyle> cellStyleMap = createCellColorMap(workbook);


        List<Section> sections = testReport.getSectionList();
        for (Section section: sections) {
            addSectionToWorkbook(workbook, section, cellStyleMap);
        }
        FileOutputStream outputStream = new FileOutputStream(path);
        workbook.write(outputStream);
        outputStream.close();
    }

    private static HashMap<String, CellStyle> createCellColorMap(Workbook workbook) {
        HashMap<String, CellStyle> map = new HashMap<>();

        map.put("VERBOSE",  createCellStyle(workbook, IndexedColors.WHITE.getIndex()));
        map.put("SUCCESS",  createCellStyle(workbook, IndexedColors.BRIGHT_GREEN.getIndex()));
        map.put("WARNING",  createCellStyle(workbook, IndexedColors.YELLOW.getIndex()));
        map.put("ERROR",    createCellStyle(workbook, IndexedColors.RED.getIndex()));
        map.put("NONE",     createCellStyle(workbook, IndexedColors.WHITE.getIndex()));
        map.put("HEADER",   createCellStyle(workbook, IndexedColors.WHITE.getIndex()));

        return map;
    }

    private static CellStyle createCellStyle(Workbook w, short index) {
        CellStyle style = w.createCellStyle();
        style.setFillForegroundColor(index);
        style.setFillBackgroundColor(index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft  (BorderStyle.THIN);
        style.setBorderRight (BorderStyle.THIN);
        style.setBorderTop   (BorderStyle.THIN);

        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor  (IndexedColors.BLACK.getIndex());
        style.setRightBorderColor (IndexedColors.BLACK.getIndex());
        style.setTopBorderColor   (IndexedColors.BLACK.getIndex());
        return style;
    }

    private static void addSectionToWorkbook(Workbook workbook, Section section, Map<String, CellStyle> cellStyleMap) throws Exception {
        Sheet sheet = workbook.createSheet(section.getTitle().replaceAll("[:/]",""));
        addHeaderRow(sheet, cellStyleMap.get("HEADER"));

        List<LineItem> lineItems = section.getLineItems();
        int rowIndex = 1;
        for (LineItem lineItem: lineItems) {
            addRowToSheet(sheet, lineItem, rowIndex++, cellStyleMap);
        }
    }

    private static void addHeaderRow(Sheet sheet, CellStyle cellStyle) throws Exception {
        String [] rowHeadings = {
                "Type",
                "Label",
                "Assertion",
                "Context",
                "Reference Value",
                "Submitted Value",
                "Message",
                "Reference",
                "Comments"
        };

        Row row = sheet.createRow(0);
        for (int i = 0; i < rowHeadings.length; i++) {
            Cell c = row.createCell(i);
            c.setCellValue(rowHeadings[i]);
            if (cellStyle != null) {
                c.setCellStyle(cellStyle);
            }
        }

    }

    private static void addRowToSheet(Sheet sheet, LineItem lineItem, int rowIndex, Map<String, CellStyle> cellStyleMap) throws Exception {
        Row row = sheet.createRow(rowIndex);
        int colIndex = 0;
        String type = lineItem.getType();
        CellStyle cellStyle = cellStyleMap.get(type);

        addCellToRow(row, lineItem.getType(),           colIndex++, cellStyle);
        addCellToRow(row, lineItem.getLabel(),          colIndex++, cellStyle);
        addCellToRow(row, lineItem.getAssertion(),      colIndex++, cellStyle);
        addCellToRow(row, lineItem.getContext(),        colIndex++, cellStyle);
        addCellToRow(row, lineItem.getReferenceValue(), colIndex++, cellStyle);
        addCellToRow(row, lineItem.getSubmittedValue(), colIndex++, cellStyle);
        addCellToRow(row, lineItem.getMessage(),        colIndex++, cellStyle);
        addCellToRow(row, lineItem.getDocReference(),   colIndex++, cellStyle);
        addCellToRow(row, lineItem.getComments(),       colIndex++, cellStyle);
    }

    private static void addCellToRow(Row row, String value, int colIndex, CellStyle cellStyle) throws Exception {
        Cell c = row.createCell(colIndex);
        c.setCellValue(value);
        if (cellStyle != null) {
            c.setCellStyle(cellStyle);
        }
    }


}
