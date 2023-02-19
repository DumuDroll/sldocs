package com.dddd.sldocs.core.controllers;

import com.dddd.sldocs.core.entities.Formulary;
import com.dddd.sldocs.core.entities.views.EdAsStView;
import com.dddd.sldocs.core.general.Dictionary;
import com.dddd.sldocs.core.services.EdAsStViewService;
import com.dddd.sldocs.core.services.FormularyService;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

@Controller
@Log4j2
public class WriteEASController {

    private static final String COMMA_REGEX = ",";
    private static final String TIMES_NEW_ROMAN = "Times New Roman";
    private final EdAsStViewService easVmService;
    private final FormularyService formularyService;

    public WriteEASController(EdAsStViewService easVmService, FormularyService formularyService) {
        this.easVmService = easVmService;
        this.formularyService = formularyService;
    }

    @RequestMapping("/EdAsSt")
    public String createExcel() {
        try (InputStream in = Files.newInputStream(new File("src/main/resources/EdAsStExample.xlsx").toPath())) {
            XSSFWorkbookFactory workbookFactory = new XSSFWorkbookFactory();
            try (XSSFWorkbook workbook = workbookFactory.create(in)) {

                XSSFFont defaultFont = workbook.createFont();
                defaultFont.setFontHeightInPoints((short) 12);
                defaultFont.setFontName(TIMES_NEW_ROMAN);
                defaultFont.setColor(IndexedColors.BLACK.getIndex());
                defaultFont.setBold(false);
                defaultFont.setItalic(false);

                XSSFFont font = workbook.createFont();
                font.setFontHeightInPoints((short) 12);
                font.setFontName(TIMES_NEW_ROMAN);
                font.setColor(IndexedColors.BLACK.getIndex());
                font.setBold(false);
                font.setItalic(false);


                CellStyle style = workbook.createCellStyle();
                style.setVerticalAlignment(VerticalAlignment.CENTER);
                style.setAlignment(HorizontalAlignment.CENTER);
                style.setBorderBottom(BorderStyle.THIN);
                style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style.setBorderLeft(BorderStyle.THIN);
                style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
                style.setBorderRight(BorderStyle.THIN);
                style.setRightBorderColor(IndexedColors.BLACK.getIndex());
                style.setBorderTop(BorderStyle.THIN);
                style.setTopBorderColor(IndexedColors.BLACK.getIndex());
                style.setWrapText(true);


                XSSFCellStyle rowAutoHeightStyle = workbook.createCellStyle();
                rowAutoHeightStyle.setWrapText(true);

                XSSFSheet sheet = workbook.getSheetAt(0);
                //Autumn 1-3 semesters
                int rowCount = 5;
                List<EdAsStView> data = easVmService.getEASVM13Data("1", "4.0");

                writeSheet(font, style, sheet, rowCount, data, false, workbook, rowAutoHeightStyle);
                //Autumn 4 semester
                sheet = workbook.getSheetAt(1);
                rowCount = 5;
                data = easVmService.getEASVMData("1", "4.0");

                writeSheet(font, style, sheet, rowCount, data, false, workbook, rowAutoHeightStyle);
                //Autumn 5 semester
                sheet = workbook.getSheetAt(2);
                rowCount = 5;
                data = easVmService.getEASVMData("1", "5.0");

                writeSheet(font, style, sheet, rowCount, data, true, workbook, rowAutoHeightStyle);
                //Autumn 6 semester
                sheet = workbook.getSheetAt(3);
                rowCount = 5;
                data = easVmService.getEASVMData("1", "6.0");

                writeSheet(font, style, sheet, rowCount, data, true, workbook, rowAutoHeightStyle);
                //Spring 1-3 semesters
                sheet = workbook.getSheetAt(4);
                rowCount = 5;
                data = easVmService.getEASVM13Data("2", "4.0");

                writeSheet(font, style, sheet, rowCount, data, false, workbook, rowAutoHeightStyle);
                //Spring 4 semester
                sheet = workbook.getSheetAt(5);
                rowCount = 5;
                data = easVmService.getEASVMData("2", "4.0");

                writeSheet(font, style, sheet, rowCount, data, true, workbook, rowAutoHeightStyle);
                //Spring 5 semester
                sheet = workbook.getSheetAt(6);
                rowCount = 5;
                data = easVmService.getEASVMData("2", "5.0");

                writeSheet(font, style, sheet, rowCount, data, true, workbook, rowAutoHeightStyle);
                //Spring 6 semester
                sheet = workbook.getSheetAt(7);
                rowCount = 5;
                data = easVmService.getEASVMData("2", "6.0");

                writeSheet(font, style, sheet, rowCount, data, true, workbook, rowAutoHeightStyle);

                File someFile = new File(Dictionary.getResultsFolder() + "Відомість_учбових_доручень.xlsx");

                try (FileOutputStream outputStream = new FileOutputStream(someFile)) {
                    workbook.write(outputStream);
                } catch (Exception e) {
                    log.error(e);
                }

                List<Formulary> formularies = formularyService.listAll();
                formularies.get(0).setEasFilename(someFile.getName());
                formularyService.save(formularies.get(0));
            } catch (Exception e) {
                log.error(e);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        return "redirect:/";
    }

    private void writeSheet(XSSFFont font, CellStyle style, XSSFSheet sheet, int rowCount,
                            List<EdAsStView> data, boolean divider, XSSFWorkbook workbook, XSSFCellStyle rowAutoHeightStyle) {

        Formulary formulary = formularyService.listAll().get(0);
        for (int i = 0; i < data.size(); i++) {

            if ((!(data.get(i).getLecHours().equals("") || data.get(i).getLecHours().equals("0.0"))) &&
                    (data.get(i).getLabHours().equals("") || data.get(i).getLabHours().equals("0.0")) &&
                    (data.get(i).getPractHours().equals("") || data.get(i).getPractHours().equals("0.0"))) {
                rowCount = writeLecHours(font, style, sheet, rowCount, data, i, divider, rowAutoHeightStyle);
            } else {
                if ((!(data.get(i).getLecHours().equals("") || data.get(i).getLecHours().equals("0.0"))) &&
                        (!(data.get(i).getLabHours().equals("") || data.get(i).getLabHours().equals("0.0"))) &&
                        (data.get(i).getPractHours().equals("") || data.get(i).getPractHours().equals("0.0"))) {
                    rowCount = writeLecHours(font, style, sheet, rowCount, data, i, divider, rowAutoHeightStyle);

                    rowCount = writeLabHours(font, style, sheet, rowCount, data, i, divider, rowAutoHeightStyle);
                } else {
                    if ((!(data.get(i).getLecHours().equals("") || data.get(i).getLecHours().equals("0.0"))) &&
                            (data.get(i).getLabHours().equals("") || data.get(i).getLabHours().equals("0.0")) &&
                            (!(data.get(i).getPractHours().equals("") || data.get(i).getPractHours().equals("0.0")))) {
                        rowCount = writeLecHours(font, style, sheet, rowCount, data, i, divider, rowAutoHeightStyle);

                        rowCount = writePractHours(font, style, sheet, rowCount, data, i, divider, rowAutoHeightStyle);
                    } else {
                        if ((!(data.get(i).getLabHours().equals("") || data.get(i).getLabHours().equals("0.0"))) &&
                                (data.get(i).getPractHours().equals("") || data.get(i).getPractHours().equals("0.0"))) {
                            rowCount = writeLabHours(font, style, sheet, rowCount, data, i, divider, rowAutoHeightStyle);
                        } else {
                            if ((data.get(i).getLabHours().equals("") || data.get(i).getLabHours().equals("0.0")) &&
                                    (!(data.get(i).getPractHours().equals("") || data.get(i).getPractHours().equals("0.0")))) {
                                rowCount = writePractHours(font, style, sheet, rowCount, data, i, divider, rowAutoHeightStyle);
                            }
                        }
                    }
                }
            }
        }
        int rows = 6;
        DataFormatter df = new DataFormatter();
        XSSFRow row;
        while (true) {
            row = sheet.getRow(rows);
            try {
                if (df.formatCellValue(row.getCell(0)).equals("")) {
                    break;
                }
                rows++;
            } catch (NullPointerException ex) {
                break;
            }
        }

        CellStyle styleBig = workbook.createCellStyle();
        styleBig.setVerticalAlignment(VerticalAlignment.CENTER);
        styleBig.setAlignment(HorizontalAlignment.CENTER);
        XSSFFont fontBig = workbook.createFont();
        fontBig.setFontHeightInPoints((short) 24);
        fontBig.setFontName(TIMES_NEW_ROMAN);
        fontBig.setColor(IndexedColors.BLACK.getIndex());
        fontBig.setBold(true);
        fontBig.setItalic(false);

        row = sheet.getRow(2);
        String[] res = row.getCell(0).toString().split(Dictionary.SPACE_REGEX);
        StringBuilder stringBuilder = new StringBuilder();
        for (int p = 0; p < res.length; p++) {
            if (p == 4) {
                stringBuilder.append(data.get(0).getYear()).append(" ");
            } else {
                stringBuilder.append(res[p]).append(" ");
            }
        }
        row.getCell(0).setCellValue(stringBuilder.toString());
        styleBig.setFont(fontBig);
        row.getCell(0).setCellStyle(styleBig);


        row = sheet.createRow(rows + 1);
        Cell cell = row.createCell(1);
        cell.setCellValue("\"_____\" ______________________ 20__ р.");
        CellStyle style2 = workbook.createCellStyle();
        XSSFFont fontCustom = workbook.createFont();
        fontCustom.setFontHeightInPoints((short) 12);
        fontCustom.setFontName(TIMES_NEW_ROMAN);
        fontCustom.setColor(IndexedColors.BLACK.getIndex());
        fontCustom.setItalic(true);
        style2.setFont(fontCustom);
        cell.setCellStyle(style2);
        cell = row.createCell(2);
        cell.setCellValue(formulary.getDepartmentHeadTittle());
        style2 = workbook.createCellStyle();
        fontCustom = workbook.createFont();
        fontCustom.setFontHeightInPoints((short) 12);
        fontCustom.setFontName(TIMES_NEW_ROMAN);
        fontCustom.setColor(IndexedColors.BLACK.getIndex());
        fontCustom.setItalic(true);
        fontCustom.setBold(true);
        style2.setFont(fontCustom);
        cell.setCellStyle(style2);
        cell = row.createCell(4);
        style2 = workbook.createCellStyle();
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cell.setCellStyle(style2);
        cell = row.createCell(5);
        style2 = workbook.createCellStyle();
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cell.setCellStyle(style2);
        cell = row.createCell(6);
        cell.setCellValue(formulary.getDepartmentHeadFullName());
        style2 = workbook.createCellStyle();
        style2.setFont(font);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cell.setCellStyle(style2);
        row = sheet.createRow(rows + 2);
        cell = row.createCell(4);
        cell.setCellValue("(підпис)");
        sheet.setFitToPage(true);
        sheet.getPrintSetup().setLandscape(true);
    }

    private int writePractHours(XSSFFont font, CellStyle style, XSSFSheet sheet, int rowCount, List<EdAsStView> data, int i,
                                boolean divider, XSSFCellStyle rowAutoHeightStyle) {
        String[] valuesGroups = data.get(i).getGroupNames().split(COMMA_REGEX);
        for (String values_group : valuesGroups) {
            rowCount = writePractHoursInner(font, style, sheet, rowCount, data, i, values_group, divider, rowAutoHeightStyle);
        }
        return rowCount;
    }

    private int writePractHoursInner(XSSFFont font, CellStyle style, XSSFSheet sheet, int rowCount,
                                     List<EdAsStView> data, int i, String group, boolean divider, XSSFCellStyle rowAutoHeightStyle) {
        Row row;
        Cell cell;
        row = sheet.createRow(++rowCount);
        cell = row.createCell(0);
        cell = writeDnGn(font, style, data, i, row, cell, rowAutoHeightStyle);
        cell.setCellValue(group);
        cell = row.createCell(3);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue("");
        cell = row.createCell(4);
        style.setFont(font);
        cell.setCellStyle(style);
        writePractHoursInnerInner(font, style, data, i, row, cell, divider);
        return rowCount;
    }

    private void writePractHoursInnerInner(XSSFFont font, CellStyle style, List<EdAsStView> data, int i, Row row, Cell cell,
                                           boolean divider) {
        cell.setCellValue("");
        cell = row.createCell(5);
        style.setFont(font);
        cell.setCellStyle(style);
        if (divider) {
            cell.setCellValue(Double.toString(Math.round(Double.parseDouble(data.get(i).getPractHours()) /
                    Double.parseDouble(data.get(i).getNumberOfSubgroups()) / 10)));
        } else {
            cell.setCellValue(Double.toString(Math.round(Double.parseDouble(data.get(i).getPractHours()) /
                    Double.parseDouble(data.get(i).getNumberOfSubgroups()) / 16)));
        }
        cell = row.createCell(6);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue(data.get(i).getTeacherName());
        cell = row.createCell(7);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue(data.get(i).getNote());
    }

    private int writeLabHours(XSSFFont font, CellStyle style, XSSFSheet sheet, int rowCount, List<EdAsStView> data, int i,
                              boolean divider, XSSFCellStyle rowAutoHeightStyle) {
        String[] valuesGroups = data.get(i).getGroupNames().split(COMMA_REGEX);
        for (String valuesGroup : valuesGroups) {
            rowCount = writeLabHoursInner(font, style, sheet, rowCount, data, i, valuesGroup, divider, rowAutoHeightStyle);
        }
        return rowCount;
    }

    private int writeLabHoursInner(XSSFFont font, CellStyle style, XSSFSheet sheet, int rowCount, List<EdAsStView> data, int i,
                                   String group, boolean divider, XSSFCellStyle rowAutoHeightStyle) {
        Row row;
        Cell cell;
        row = sheet.createRow(++rowCount);
        cell = row.createCell(0);
        cell = writeDnGn(font, style, data, i, row, cell, rowAutoHeightStyle);
        cell.setCellValue(group);
        cell = row.createCell(3);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue("");
        cell = row.createCell(4);
        style.setFont(font);
        cell.setCellStyle(style);
        writeLabHoursInnerInner(font, style, data, i, row, cell, divider);
        return rowCount;
    }

    private void writeLabHoursInnerInner(XSSFFont font, CellStyle style, List<EdAsStView> data, int i, Row row, Cell cell,
                                         boolean divider) {
        if (divider) {
            cell.setCellValue(Double.toString(Math.round(Double.parseDouble(data.get(i).getLabHours()) /
                    Double.parseDouble(data.get(i).getNumberOfSubgroups()) / 10)));
        } else {
            cell.setCellValue(Double.toString(Math.round(Double.parseDouble(data.get(i).getLabHours()) /
                    Double.parseDouble(data.get(i).getNumberOfSubgroups()) / 16)));
        }
        cell = row.createCell(5);
        style.setFont(font);
        cell.setCellStyle(style);
        writePnN(font, style, data, i, row, cell);
    }

    private int writeLecHours(XSSFFont font, CellStyle style, XSSFSheet sheet, int rowCount, List<EdAsStView> data, int i,
                              boolean divider, XSSFCellStyle rowAutoHeightStyle) {
        Row row = sheet.createRow(++rowCount);
        Cell cell = row.createCell(0);
        cell = writeDnGn(font, style, data, i, row, cell, rowAutoHeightStyle);
        cell.setCellValue(data.get(i).getGroupNames());
        cell = row.createCell(3);
        style.setFont(font);
        cell.setCellStyle(style);
        if (divider) {
            cell.setCellValue(Double.toString(Math.round(Double.parseDouble(data.get(i).getLecHours()) / 10)));
        } else {
            cell.setCellValue(Double.toString(Math.round(Double.parseDouble(data.get(i).getLecHours()) / 16)));
        }
        cell = row.createCell(4);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue("");
        cell = row.createCell(5);
        style.setFont(font);
        cell.setCellStyle(style);
        writePnN(font, style, data, i, row, cell);
        return rowCount;
    }

    private void writePnN(XSSFFont font, CellStyle style, List<EdAsStView> data, int i, Row row, Cell cell) {
        cell.setCellValue("");
        cell = row.createCell(6);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue(data.get(i).getTeacherName());
        cell = row.createCell(7);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue(data.get(i).getNote());
    }

    private Cell writeDnGn(XSSFFont font, CellStyle style, List<EdAsStView> data, int i, Row row, Cell cell, XSSFCellStyle rowAutoHeightStyle) {
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue((double) i + 1);
        cell = row.createCell(1);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue(data.get(i).getDisciplineName());
        cell = row.createCell(2);
        style.setFont(font);
        cell.setCellStyle(style);

        row.setRowStyle(rowAutoHeightStyle);
        return cell;
    }
}
