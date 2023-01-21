package com.dddd.sldocs.core.controllers;

import com.dddd.sldocs.core.entities.Faculty;
import com.dddd.sldocs.core.entities.views.EdAsStView;
import com.dddd.sldocs.core.services.EdAsStViewService;
import com.dddd.sldocs.core.services.FacultyService;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@Log4j2
public class WriteEASController {

    private static final String COMMA_REGEX = ",";
    private static final String GROUP_REGEX = "^([\\p{L}]{2})([-])([0-9]{3}|[\\p{L}][0-9]{3})(.[.].*|[і].*|.[і].*|[\\p{L}]*)?$";
    private static final String TIMES_NEW_ROMAN = "Times New Roman";

    private final EdAsStViewService eas_vmService;
    private final FacultyService facultyService;

    public WriteEASController(EdAsStViewService eas_vmService, FacultyService facultyService) {
        this.eas_vmService = eas_vmService;
        this.facultyService = facultyService;
    }

    @RequestMapping("/EdAsSt")
    public String createExcel() {
        long m = System.currentTimeMillis();
        try {
            InputStream in = Files.newInputStream(new File("src/main/resources/EdAsStExample.xlsx").toPath());
            XSSFWorkbookFactory workbookFactory = new XSSFWorkbookFactory();
            XSSFWorkbook workbook = workbookFactory.create(in);

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
            List<EdAsStView> data = eas_vmService.getEASVM13Data("1", "4.0");

            writeSheet(font, style, sheet, rowCount, data, false, workbook, rowAutoHeightStyle);
            //Autumn 4 semester
            sheet = workbook.getSheetAt(1);
            rowCount = 5;
            data = eas_vmService.getEASVMData("1", "4.0");

            writeSheet(font, style, sheet, rowCount, data, false, workbook, rowAutoHeightStyle);
            //Autumn 5 semester
            sheet = workbook.getSheetAt(2);
            rowCount = 5;
            data = eas_vmService.getEASVMData("1", "5.0");

            writeSheet(font, style, sheet, rowCount, data, true, workbook, rowAutoHeightStyle);
            //Autumn 6 semester
            sheet = workbook.getSheetAt(3);
            rowCount = 5;
            data = eas_vmService.getEASVMData("1", "6.0");

            writeSheet(font, style, sheet, rowCount, data, true, workbook, rowAutoHeightStyle);
            //Spring 1-3 semesters
            sheet = workbook.getSheetAt(4);
            rowCount = 5;
            data = eas_vmService.getEASVM13Data("2", "4.0");

            writeSheet(font, style, sheet, rowCount, data, false, workbook, rowAutoHeightStyle);
            //Spring 4 semester
            sheet = workbook.getSheetAt(5);
            rowCount = 5;
            data = eas_vmService.getEASVMData("2", "4.0");

            writeSheet(font, style, sheet, rowCount, data, true, workbook, rowAutoHeightStyle);
            //Spring 5 semester
            sheet = workbook.getSheetAt(6);
            rowCount = 5;
            data = eas_vmService.getEASVMData("2", "5.0");

            writeSheet(font, style, sheet, rowCount, data, true, workbook, rowAutoHeightStyle);
            //Spring 6 semester
            sheet = workbook.getSheetAt(7);
            rowCount = 5;
            data = eas_vmService.getEASVMData("2", "6.0");

            writeSheet(font, style, sheet, rowCount, data, true, workbook, rowAutoHeightStyle);



            in.close();
            File someFile = new File("Відомість_учбових_доручень.xlsx");
            FileOutputStream outputStream = new FileOutputStream(someFile);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

            List<Faculty> faculties = facultyService.listAll();
            faculties.get(0).setEasFilename(someFile.getName());
            facultyService.save(faculties.get(0));

        } catch (Exception ex) {
           log.error(ex);
        }
        log.info("EAS created in {} seconds",(System.currentTimeMillis() - m)/100);
        return "redirect:/";
    }

    private void writeSheet(XSSFFont font, CellStyle style, XSSFSheet sheet, int rowCount,
                            List<EdAsStView> data, boolean divider, XSSFWorkbook workbook, XSSFCellStyle rowAutoHeightStyle) {


        for (int i = 0; i < data.size(); i++) {

            if ((!(data.get(i).getLecHours().equals("") || data.get(i).getLecHours().equals("0.0"))) &&
                    (data.get(i).getLabHours().equals("") || data.get(i).getLabHours().equals("0.0")) &&
                    (data.get(i).getPractHours().equals("") || data.get(i).getPractHours().equals("0.0"))) {
                rowCount = writeLec_hours(font, style, sheet, rowCount, data, i, divider, rowAutoHeightStyle);
            } else {
                if ((!(data.get(i).getLecHours().equals("") || data.get(i).getLecHours().equals("0.0"))) &&
                        (!(data.get(i).getLabHours().equals("") || data.get(i).getLabHours().equals("0.0"))) &&
                        (data.get(i).getPractHours().equals("") || data.get(i).getPractHours().equals("0.0"))) {
                    rowCount = writeLec_hours(font, style, sheet, rowCount, data, i, divider, rowAutoHeightStyle);

                    rowCount = writeLab_hours(font, style, sheet, rowCount, data, i, divider, rowAutoHeightStyle);
                } else {
                    if ((!(data.get(i).getLecHours().equals("") || data.get(i).getLecHours().equals("0.0"))) &&
                            (data.get(i).getLabHours().equals("") || data.get(i).getLabHours().equals("0.0")) &&
                            (!(data.get(i).getPractHours().equals("") || data.get(i).getPractHours().equals("0.0")))) {
                        rowCount = writeLec_hours(font, style, sheet, rowCount, data, i, divider, rowAutoHeightStyle);

                        rowCount = writePract_hours(font, style, sheet, rowCount, data, i, divider, rowAutoHeightStyle);
                    } else {
                        if ((!(data.get(i).getLabHours().equals("") || data.get(i).getLabHours().equals("0.0"))) &&
                                (data.get(i).getPractHours().equals("") || data.get(i).getPractHours().equals("0.0"))) {
                            rowCount = writeLab_hours(font, style, sheet, rowCount, data, i, divider, rowAutoHeightStyle);
                        } else {
                            if ((data.get(i).getLabHours().equals("") || data.get(i).getLabHours().equals("0.0")) &&
                                    (!(data.get(i).getPractHours().equals("") || data.get(i).getPractHours().equals("0.0")))) {
                                rowCount = writePract_hours(font, style, sheet, rowCount, data, i, divider, rowAutoHeightStyle);
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
        fontBig.setFontName("Times New Roman");
        fontBig.setColor(IndexedColors.BLACK.getIndex());
        fontBig.setBold(true);
        fontBig.setItalic(false);

        row = sheet.getRow(2);
        String space_regex = "\\s+";
        String[] res = row.getCell(0).toString().split(space_regex);
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
        cell.setCellValue("В.о. зав. кафедрою");
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
        cell.setCellValue("Михайло ГОДЛЕВСЬКИЙ");
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

    private int writePract_hours(XSSFFont font, CellStyle style, XSSFSheet sheet, int rowCount, List<EdAsStView> data, int i,
                                 boolean divider, XSSFCellStyle rowAutoHeightStyle) {
        Row row;
        Cell cell;
        StringBuilder stringBuilder;
        String[] values_groups = data.get(i).getGroupNames().split(COMMA_REGEX);
        Pattern pattern = Pattern.compile(GROUP_REGEX);
        Matcher matcher;
        for (String values_group : values_groups) {
            matcher = pattern.matcher(values_group.trim());
            while (matcher.find()) {
                if (Character.isDigit(matcher.group(3).charAt(0))) {
                    if (!matcher.group(4).equals("") && matcher.group(4).length() > 1) {
                        if (matcher.group(4).charAt(0) == 'і' || matcher.group(4).charAt(1) == 'і' || matcher.group(4).charAt(1) == 'е' || matcher.group(4).charAt(1) == '.') {
                            rowCount = writePract_hours_inner(font, style, sheet, rowCount, data, i, matcher, divider, rowAutoHeightStyle);
                        } else {
                            for (int i1 = 0; i1 < matcher.group(4).length(); i1++) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(matcher.group(1)).append(matcher.group(2)).append(matcher.group(3)).append(matcher.group(4).charAt(i1));
                                row = sheet.createRow(++rowCount);
                                cell = row.createCell(0);
                                cell = writeDnGn(font, style, data, i, row, cell, rowAutoHeightStyle);
                                cell.setCellValue(stringBuilder.toString());
                                cell = row.createCell(3);
                                style.setFont(font);
                                cell.setCellStyle(style);
                                cell.setCellValue("");
                                cell = row.createCell(4);
                                style.setFont(font);
                                cell.setCellStyle(style);
                                writePract_hours_inner_inner(font, style, data, i, row, cell, divider);
                            }
                        }
                    } else {
                        rowCount = writePract_hours_inner(font, style, sheet, rowCount, data, i, matcher, divider, rowAutoHeightStyle);
                    }
                } else {
                    rowCount = writePract_hours_inner(font, style, sheet, rowCount, data, i, matcher, divider, rowAutoHeightStyle);
                }
            }
        }
        return rowCount;
    }

    private int writePract_hours_inner(XSSFFont font, CellStyle style, XSSFSheet sheet, int rowCount, List<EdAsStView> data, int i,
                                       Matcher matcher, boolean divider, XSSFCellStyle rowAutoHeightStyle) {
        Row row;
        Cell cell;
        row = sheet.createRow(++rowCount);
        cell = row.createCell(0);
        cell = writeDnGn(font, style, data, i, row, cell, rowAutoHeightStyle);
        cell.setCellValue(matcher.group(0));
        cell = row.createCell(3);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue("");
        cell = row.createCell(4);
        style.setFont(font);
        cell.setCellStyle(style);
        writePract_hours_inner_inner(font, style, data, i, row, cell, divider);
        return rowCount;
    }

    private void writePract_hours_inner_inner(XSSFFont font, CellStyle style, List<EdAsStView> data, int i, Row row, Cell cell,
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
        cell.setCellValue(data.get(i).getPname());
        cell = row.createCell(7);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue(data.get(i).getNote());
    }

    private int writeLab_hours(XSSFFont font, CellStyle style, XSSFSheet sheet, int rowCount, List<EdAsStView> data, int i,
                               boolean divider, XSSFCellStyle rowAutoHeightStyle) {
        Row row;
        Cell cell;
        StringBuilder stringBuilder;
        String[] values_groups = data.get(i).getGroupNames().split(COMMA_REGEX);
        Pattern pattern = Pattern.compile(GROUP_REGEX);
        Matcher matcher;
        for (int j = 0; j < values_groups.length; j++) {
            matcher = pattern.matcher(values_groups[j].trim());
            while (matcher.find()) {
                if (Character.isDigit(matcher.group(3).charAt(0))) {
                    if (!matcher.group(4).equals("") && matcher.group(4).length() > 1) {
                        if (matcher.group(4).charAt(0) == 'і' || matcher.group(4).charAt(1) == 'і' || matcher.group(4).charAt(1) == 'е' || matcher.group(4).charAt(1) == '.') {
                            rowCount = writeLab_hours_inner(font, style, sheet, rowCount, data, i, matcher, divider, rowAutoHeightStyle);
                        } else {
                            for (int i1 = 0; i1 < matcher.group(4).length(); i1++) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(matcher.group(1)).append(matcher.group(2)).append(matcher.group(3)).append(matcher.group(4).charAt(i1));
                                row = sheet.createRow(++rowCount);
                                cell = row.createCell(0);
                                cell = writeDnGn(font, style, data, i, row, cell, rowAutoHeightStyle);
                                cell.setCellValue(stringBuilder.toString());
                                cell = row.createCell(3);
                                style.setFont(font);
                                cell.setCellStyle(style);
                                cell.setCellValue("");
                                cell = row.createCell(4);
                                style.setFont(font);
                                cell.setCellStyle(style);
                                writeLab_hours_inner_inner(font, style, data, i, row, cell, divider);
                            }
                        }
                    } else {
                        rowCount = writeLab_hours_inner(font, style, sheet, rowCount, data, i, matcher, divider, rowAutoHeightStyle);
                    }
                } else {
                    rowCount = writeLab_hours_inner(font, style, sheet, rowCount, data, i, matcher, divider, rowAutoHeightStyle);
                }
            }
        }
        return rowCount;
    }

    private int writeLab_hours_inner(XSSFFont font, CellStyle style, XSSFSheet sheet, int rowCount, List<EdAsStView> data, int i,
                                     Matcher matcher, boolean divider, XSSFCellStyle rowAutoHeightStyle) {
        Row row;
        Cell cell;
        row = sheet.createRow(++rowCount);
        cell = row.createCell(0);
        cell = writeDnGn(font, style, data, i, row, cell, rowAutoHeightStyle);
        cell.setCellValue(matcher.group(0));
        cell = row.createCell(3);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue("");
        cell = row.createCell(4);
        style.setFont(font);
        cell.setCellStyle(style);
        writeLab_hours_inner_inner(font, style, data, i, row, cell, divider);
        return rowCount;
    }

    private void writeLab_hours_inner_inner(XSSFFont font, CellStyle style, List<EdAsStView> data, int i, Row row, Cell cell,
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

    private int writeLec_hours(XSSFFont font, CellStyle style, XSSFSheet sheet, int rowCount, List<EdAsStView> data, int i,
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
        cell.setCellValue(data.get(i).getPname());
        cell = row.createCell(7);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue(data.get(i).getNote());
    }

    private Cell writeDnGn(XSSFFont font, CellStyle style, List<EdAsStView> data, int i, Row row, Cell cell, XSSFCellStyle rowAutoHeightStyle) {
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue(i + 1);
        cell = row.createCell(1);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue(data.get(i).getDname());
        cell = row.createCell(2);
        style.setFont(font);
        cell.setCellStyle(style);

        row.setRowStyle(rowAutoHeightStyle);
        return cell;
    }
}