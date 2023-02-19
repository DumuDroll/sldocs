package com.dddd.sldocs.core.controllers;

import com.dddd.sldocs.core.entities.CreationMetric;
import com.dddd.sldocs.core.entities.Formulary;
import com.dddd.sldocs.core.entities.Teacher;
import com.dddd.sldocs.core.entities.views.PersonalLoadView;
import com.dddd.sldocs.core.general.Dictionary;
import com.dddd.sldocs.core.general.utils.cyrToLatin.UkrainianToLatin;
import com.dddd.sldocs.core.services.CreationMetricService;
import com.dddd.sldocs.core.services.FormularyService;
import com.dddd.sldocs.core.services.PersonalLoadViewService;
import com.dddd.sldocs.core.services.TeacherService;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
@Log4j2
public class WritePSLController {

    private final PersonalLoadViewService plsVmService;

    private final CreationMetricService metricService;
    private final TeacherService teacherService;
    private final FormularyService formularyService;
    private static final String PERSONAL_STUDY_LOAD_XLSX = "personal_study_load.xlsx";
    private static final String TIMES_NEW_ROMAN = "Times New Roman";

    public WritePSLController(PersonalLoadViewService plsVmService, TeacherService teacherService,
                              CreationMetricService metricService, FormularyService formularyService) {
        this.plsVmService = plsVmService;
        this.teacherService = teacherService;
        this.formularyService = formularyService;
        this.metricService = metricService;
    }

    @PostMapping("/uploadPSL")
    public String uploadPSLToLFS(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path path = Paths.get(Dictionary.getResultsFolder() + fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error(e);
        }
        writePSLforTeacher();
        return "success/pslToDB";
    }


    @RequestMapping("/PSL")
    public String writePSL() {
        long startTime = System.currentTimeMillis();
        try (InputStream inputStream = Files.newInputStream(new File("src/main/resources/PSLExample.xlsx").toPath())) {
            XSSFWorkbookFactory workbookFactory = new XSSFWorkbookFactory();
            try (XSSFWorkbook workbook = workbookFactory.create(inputStream)) {

                XSSFFont defaultFont = workbook.createFont();
                defaultFont.setFontHeightInPoints((short) 12);
                defaultFont.setFontName(TIMES_NEW_ROMAN);
                defaultFont.setBold(false);
                defaultFont.setItalic(false);

                XSSFFont font10 = workbook.createFont();
                font10.setFontHeightInPoints((short) 10);
                font10.setFontName(TIMES_NEW_ROMAN);
                font10.setBold(false);
                font10.setItalic(false);

                XSSFFont font14 = workbook.createFont();
                font14.setFontHeightInPoints((short) 14);
                font14.setFontName(TIMES_NEW_ROMAN);
                font14.setBold(false);
                font14.setItalic(false);

                XSSFFont font14Bold = workbook.createFont();
                font14Bold.setFontHeightInPoints((short) 14);
                font14Bold.setFontName(TIMES_NEW_ROMAN);
                font14Bold.setBold(true);
                font14Bold.setItalic(false);

                XSSFFont font12 = workbook.createFont();
                font12.setFontName(TIMES_NEW_ROMAN);
                font12.setFontHeightInPoints((short) 12);
                font12.setBold(false);
                font12.setItalic(false);

                XSSFFont font12Bold = workbook.createFont();
                font12Bold.setFontHeightInPoints((short) 12);
                font12Bold.setFontName(TIMES_NEW_ROMAN);
                font12Bold.setBold(true);
                font12Bold.setItalic(false);

                CellStyle styleDiscHours = workbook.createCellStyle();
                styleDiscHours.setVerticalAlignment(VerticalAlignment.CENTER);
                styleDiscHours.setAlignment(HorizontalAlignment.CENTER);
                styleDiscHours.setFont(font12);
                styleDiscHours.setWrapText(false);
                styleDiscHours.setBorderBottom(BorderStyle.THIN);
                styleDiscHours.setBorderLeft(BorderStyle.THIN);
                styleDiscHours.setBorderRight(BorderStyle.THIN);
                styleDiscHours.setBorderTop(BorderStyle.THIN);

                CellStyle styleDiscName = workbook.createCellStyle();
                styleDiscName.setVerticalAlignment(VerticalAlignment.CENTER);
                styleDiscName.setAlignment(HorizontalAlignment.LEFT);
                styleDiscName.setFont(font12);
                styleDiscName.setWrapText(true);
                styleDiscName.setBorderBottom(BorderStyle.THIN);
                styleDiscName.setBorderLeft(BorderStyle.THIN);
                styleDiscName.setBorderRight(BorderStyle.THIN);
                styleDiscName.setBorderTop(BorderStyle.THIN);

                CellStyle styleDiscGroups = workbook.createCellStyle();
                styleDiscGroups.setVerticalAlignment(VerticalAlignment.CENTER);
                styleDiscGroups.setAlignment(HorizontalAlignment.CENTER);
                styleDiscGroups.setFont(font10);
                styleDiscGroups.setWrapText(true);
                styleDiscGroups.setBorderBottom(BorderStyle.THIN);
                styleDiscGroups.setBorderLeft(BorderStyle.THIN);
                styleDiscGroups.setBorderRight(BorderStyle.THIN);
                styleDiscGroups.setBorderTop(BorderStyle.THIN);


                CellStyle style14 = workbook.createCellStyle();
                style14.setVerticalAlignment(VerticalAlignment.CENTER);
                style14.setAlignment(HorizontalAlignment.CENTER);
                style14.setFont(font14);
                style14.setWrapText(true);
                style14.setBorderBottom(BorderStyle.THIN);
                style14.setBorderLeft(BorderStyle.THIN);
                style14.setBorderRight(BorderStyle.THIN);
                style14.setBorderTop(BorderStyle.THIN);

                CellStyle style14LeftAl = workbook.createCellStyle();
                style14LeftAl.setVerticalAlignment(VerticalAlignment.CENTER);
                style14LeftAl.setAlignment(HorizontalAlignment.LEFT);
                style14LeftAl.setFont(font14);
                style14LeftAl.setWrapText(true);
                style14LeftAl.setBorderBottom(BorderStyle.THIN);
                style14LeftAl.setBorderLeft(BorderStyle.THIN);
                style14LeftAl.setBorderRight(BorderStyle.THIN);
                style14LeftAl.setBorderTop(BorderStyle.THIN);

                CellStyle style14Bold = workbook.createCellStyle();
                style14Bold.setFont(font14Bold);
                style14Bold.setWrapText(true);
                style14Bold.setBorderBottom(BorderStyle.THIN);
                style14Bold.setBorderLeft(BorderStyle.THIN);
                style14Bold.setBorderRight(BorderStyle.THIN);
                style14Bold.setBorderTop(BorderStyle.THIN);

                CellStyle style12Bold = workbook.createCellStyle();
                style12Bold.setFont(font12Bold);
                style12Bold.setWrapText(true);
                style12Bold.setBorderBottom(BorderStyle.THIN);
                style12Bold.setBorderLeft(BorderStyle.THIN);
                style12Bold.setBorderRight(BorderStyle.THIN);
                style12Bold.setBorderTop(BorderStyle.THIN);

                CellStyle style14RightAl = workbook.createCellStyle();
                style14RightAl.setVerticalAlignment(VerticalAlignment.CENTER);
                style14RightAl.setAlignment(HorizontalAlignment.RIGHT);
                style14RightAl.setBorderBottom(BorderStyle.THIN);
                style14RightAl.setBorderLeft(BorderStyle.THIN);
                style14RightAl.setBorderRight(BorderStyle.THIN);
                style14RightAl.setBorderTop(BorderStyle.THIN);
                style14RightAl.setFont(font14);
                style14RightAl.setWrapText(true);

                CellStyle style12RightAl = workbook.createCellStyle();
                style12RightAl.setVerticalAlignment(VerticalAlignment.CENTER);
                style12RightAl.setAlignment(HorizontalAlignment.RIGHT);
                style12RightAl.setBorderBottom(BorderStyle.THIN);
                style12RightAl.setBorderLeft(BorderStyle.THIN);
                style12RightAl.setBorderRight(BorderStyle.THIN);
                style12RightAl.setBorderTop(BorderStyle.THIN);
                style12RightAl.setFont(font12);
                style12RightAl.setWrapText(true);

                CellStyle styleThickBotBord = workbook.createCellStyle();
                styleThickBotBord.setFont(font14Bold);
                styleThickBotBord.setWrapText(true);
                styleThickBotBord.setBorderBottom(BorderStyle.THICK);
                styleThickBotBord.setBorderLeft(BorderStyle.THIN);
                styleThickBotBord.setBorderRight(BorderStyle.THIN);
                styleThickBotBord.setBorderTop(BorderStyle.THIN);

                CellStyle styleThickBotRightBord = workbook.createCellStyle();
                styleThickBotRightBord.setFont(font14Bold);
                styleThickBotRightBord.setWrapText(true);
                styleThickBotRightBord.setBorderBottom(BorderStyle.THICK);
                styleThickBotRightBord.setBorderLeft(BorderStyle.THIN);
                styleThickBotRightBord.setBorderRight(BorderStyle.THICK);
                styleThickBotRightBord.setBorderTop(BorderStyle.THIN);

                CellStyle styleThickBotTopBord = workbook.createCellStyle();
                styleThickBotTopBord.setFont(font14Bold);
                styleThickBotTopBord.setWrapText(true);
                styleThickBotTopBord.setBorderBottom(BorderStyle.THICK);
                styleThickBotTopBord.setBorderLeft(BorderStyle.THIN);
                styleThickBotTopBord.setBorderRight(BorderStyle.THIN);
                styleThickBotTopBord.setBorderTop(BorderStyle.THICK);

                CellStyle styleThickBotTopRightBord = workbook.createCellStyle();
                styleThickBotTopRightBord.setFont(font14Bold);
                styleThickBotTopRightBord.setWrapText(true);
                styleThickBotTopRightBord.setBorderBottom(BorderStyle.THICK);
                styleThickBotTopRightBord.setBorderLeft(BorderStyle.THIN);
                styleThickBotTopRightBord.setBorderRight(BorderStyle.THICK);
                styleThickBotTopRightBord.setBorderTop(BorderStyle.THICK);

                CellStyle styleThickRightBord = workbook.createCellStyle();
                styleThickRightBord.setFont(font14);
                styleThickRightBord.setWrapText(true);
                styleThickRightBord.setBorderBottom(BorderStyle.THIN);
                styleThickRightBord.setBorderLeft(BorderStyle.THIN);
                styleThickRightBord.setBorderRight(BorderStyle.THICK);
                styleThickRightBord.setBorderTop(BorderStyle.THIN);

                XSSFCellStyle rowAutoHeightStyle = workbook.createCellStyle();
                rowAutoHeightStyle.setWrapText(true);

                List<Teacher> teachers = teacherService.listAll();
                teachers.sort(Comparator.comparing(Teacher::getFullName, Comparator.nullsLast(Comparator.naturalOrder())));
                Formulary formulary = formularyService.listAll().get(0);

                XSSFRow row;
                XSSFCell cell;

                int lastVertCellSum;
                int rowNum;
                int teacherSummaryRowNumber = 5;
                for (Teacher teacher : teachers) {
                    if (!teacher.getName().equals("")) {
                        List<PersonalLoadView> personalLoadViewList;
                        XSSFSheet sheet = workbook.cloneSheet(2, teacher.getName());
                        row = sheet.getRow(2);
                        cell = row.getCell(0);
                        cell.setCellValue(teacher.getName());
                        cell.setCellStyle(style14Bold);

                        cell = row.createCell(3);
                        cell.setCellValue("Ставка " + teacher.getStavka());
                        cell.setCellStyle(style14);

                        cell = row.getCell(5);
                        CellRangeAddress cellRangeAddress = new CellRangeAddress(2, 2, 5, 19);
                        sheet.addMergedRegion(cellRangeAddress);
                        cell.setCellValue("Кафедра програмної інженерії та інтелектуальних технологій управління");
                        cell.getCellStyle().setFont(font14);

                        rowNum = 4; //Поменялся пример -ОСЕНЬ -Норматив
                        //ОСІНЬ
                        row = sheet.createRow(rowNum++);
                        cell = row.createCell(0);
                        cell.setCellValue("ОСІНЬ");
                        cell.setCellStyle(style14);
                        sheet.addMergedRegion(new CellRangeAddress(
                                rowNum - 1, //first row (0-based)
                                rowNum - 1, //last row  (0-based)
                                0, //first column (0-based)
                                19  //last column  (0-based)
                        ));
                        // Добавляем строки нагрузки, если есть в осеннем семестре
                        double autumnHoursTotal = 0;
                        if (!plsVmService.getPSLVMData("1", teacher.getName()).isEmpty()) {
                            personalLoadViewList = plsVmService.getPSLVMData("1", teacher.getName());
                            for (PersonalLoadView personalLoadView : personalLoadViewList) {
                                row = sheet.createRow(rowNum++);
                                autumnHoursTotal = writeDisciplines(styleDiscHours, styleDiscName, styleDiscGroups, row, personalLoadView);
                                cell.setCellStyle(styleDiscHours);
                                cell = row.createCell(19);
                                cell.setCellFormula(Dictionary.SUM_E_START_OF_THE_FORMULA + rowNum + ":S" + rowNum + ")");
                                cell.setCellStyle(styleDiscHours);
                                row.setRowStyle(rowAutoHeightStyle);
                            }
                        }

                        //Добавляется пустая строка - вдруг что-то нужно добавит руками
                        row = sheet.createRow(rowNum++);
                        for (int k = 0; k < 20; k++) {
                            cell = row.createCell(k);
                            cell.setCellValue("");
                            cell.setCellStyle(styleDiscHours);

                        }
                        // Данные по студентам, которыми руководит преподаватель
                        String[] ends1 = {"КЕРІВНИЦТВО"};
                        rowNum = writeKerivnictvo(styleDiscHours, teacher, style12Bold, rowNum, sheet, true, ends1);
                        String[] ends2 = {Dictionary.ASPIRANTS_DOCTORANTS, "Магістри професійні", Dictionary.BACHELORS, Dictionary.COURSE_PROJECTS_5_COURSE};
                        rowNum = writeKerivnictvo(styleDiscHours, teacher, style12RightAl, rowNum, sheet, true, ends2);


                        int autumnSum = rowNum;
                        teacher.getTeacherHours().setAutumnSumRowNumber(String.valueOf(autumnSum));
                        row = sheet.createRow(rowNum++);
                        cell = row.createCell(0);
                        cell.setCellValue("Усього за осінь");
                        cell.setCellStyle(styleThickBotTopBord);

                        for (int k = 1; k < 4; k++) {
                            cell = row.createCell(k);
                            cell.setCellValue("");
                            cell.setCellStyle(styleThickBotTopBord);
                        }
                        String[] sums = {"E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S"};
                        int cellCount = 4;
                        lastVertCellSum = rowNum - 1;
                        for (String sum : sums) {
                            cell = row.createCell(cellCount++);
                            cell.setCellFormula(Dictionary.ROUND_SUM_START_OF_THE_FORMULA + sum + "6:" + sum + lastVertCellSum + "),0)"); //7>>>6
                            cell.setCellStyle(styleThickBotTopBord);
                        }
                        cell = row.createCell(cellCount);
                        cell.setCellFormula("ROUND(SUM(T6:" + "T" + lastVertCellSum + "),0)");//7>>>6
                        cell.setCellStyle(styleThickBotTopRightBord);

                        teacher.getTeacherHours().setAutumnSumPlan(String.valueOf(autumnHoursTotal));

                        //ВЕСНА
                        row = sheet.createRow(rowNum++);
                        cell = row.createCell(0);
                        cell.setCellValue("ВЕСНА");
                        cell.setCellStyle(style14);
                        sheet.addMergedRegion(new CellRangeAddress(
                                rowNum - 1, //first row (0-based)
                                rowNum - 1, //last row  (0-based)
                                0, //first column (0-based)
                                19  //last column  (0-based)
                        ));
                        // Добавляем строки нагрузки, если есть в весеннем семестре
                        int firstSumCellSpring = rowNum + 1;
                        double springHoursTotal = 0;
                        if (!plsVmService.getPSLVMData("2", teacher.getName()).isEmpty()) {
                            personalLoadViewList = plsVmService.getPSLVMData("2", teacher.getName());
                            for (PersonalLoadView personalLoadView : personalLoadViewList) {
                                row = sheet.createRow(rowNum++);
                                springHoursTotal = writeDisciplines(styleDiscHours, styleDiscName, styleDiscGroups, row, personalLoadView);
                                cell.setCellStyle(styleDiscHours);
                                cell = row.createCell(19);
                                cell.setCellFormula(Dictionary.SUM_E_START_OF_THE_FORMULA + rowNum + ":S" + rowNum + ")");
                                cell.setCellStyle(styleDiscHours);
                                row.setRowStyle(rowAutoHeightStyle);
                            }
                        }
                        //Добавляется пустая строка - вдруг что-то нужно добавит руками
                        row = sheet.createRow(rowNum++);
                        for (int k = 0; k < 20; k++) {
                            cell = row.createCell(k);
                            cell.setCellValue("");
                            cell.setCellStyle(styleDiscHours);
                        }
                        // Данные по студентам, которыми руководит преподаватель
                        ends2 = new String[]{Dictionary.ASPIRANTS_DOCTORANTS, "Магістри наукові", Dictionary.BACHELORS, Dictionary.COURSE_PROJECTS_5_COURSE};
                        rowNum = writeKerivnictvo(styleDiscHours, teacher, style12Bold, rowNum, sheet, false, ends1);
                        rowNum = writeKerivnictvo(styleDiscHours, teacher, style12RightAl, rowNum, sheet, false, ends2);

                        row = sheet.createRow(rowNum++);
                        cell = row.createCell(0);
                        cell.setCellValue("Усього за весну");
                        cell.setCellStyle(styleThickBotTopBord);

                        for (int k = 1; k < 4; k++) {
                            cell = row.createCell(k);
                            cell.setCellValue("");
                            cell.setCellStyle(styleThickBotTopBord);
                        }
                        cellCount = 4;
                        lastVertCellSum = rowNum - 1;
                        for (String sum : sums) {
                            cell = row.createCell(cellCount++);
                            cell.setCellFormula(Dictionary.ROUND_SUM_START_OF_THE_FORMULA + sum + firstSumCellSpring + ":" + sum + lastVertCellSum + "),0)");
                            cell.setCellStyle(styleThickBotTopBord);
                        }
                        cell = row.createCell(cellCount);
                        cell.setCellFormula("ROUND(SUM(T" + firstSumCellSpring + ":" + "T" + lastVertCellSum + "),0)");
                        cell.setCellStyle(styleThickBotTopRightBord);

                        teacher.getTeacherHours().setSpringSumPlan(String.valueOf(springHoursTotal));

                        row = sheet.createRow(rowNum++);
                        cell = row.createCell(0);
                        cell.setCellValue("УСЬОГО ЗА РІК");
                        cell.setCellStyle(styleThickBotBord);


                        for (int k = 1; k < 4; k++) {
                            cell = row.createCell(k);
                            cell.setCellValue("");
                            cell.setCellStyle(styleThickBotBord);
                        }
                        cellCount = 4;
                        for (String sum : sums) {
                            cell = row.createCell(cellCount++);
                            cell.setCellFormula(Dictionary.ROUND_SUM_START_OF_THE_FORMULA + sum + (autumnSum + 1) + "+" + sum + (rowNum - 1) + "),0)");
                            cell.setCellStyle(styleThickBotBord);
                        }
                        cell = row.createCell(cellCount);
                        cell.setCellFormula("ROUND(SUM(T" + (autumnSum + 1) + "+" + "T" + (rowNum - 1) + "),0)");
                        cell.setCellStyle(styleThickBotTopRightBord);

                        if (!(teacher.getPosada() == null || teacher.getPosada().equals(""))) {
                            row = sheet.createRow(rowNum + 2);
                            cell = row.createCell(cellCount++);
                            cell.setCellValue(getStandardHours(teacher.getStavka(), teacher.getPosada(), workbook));
                            cell = row.createCell(cellCount);
                            cell.setCellFormula("T" + rowNum + "-T" + (rowNum + 3));
                        }
                        sheet.setFitToPage(true);
                        sheet.getPrintSetup().setLandscape(true);

                        teacherService.save(teacher);

                        //filling up the summary
                        sheet = workbook.getSheetAt(0);
                        row = sheet.getRow(0);
                        cell = row.getCell(0);
                        cell.setCellValue("ЗВЕДЕНЕ НАВЧАЛЬНЕ НАВАНТАЖЕННЯ кафедри " + formulary.getDepartmentFullNameGenitiveCase()
                                + " на " + formulary.getAcademicYear() + " навчальний рік");

                        row = sheet.getRow(1);
                        cell = row.getCell(0);
                        cell.setCellValue("Станом на " + DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDateTime.now()));

                        if(teacher.getFullName()==null || teacher.getFullName().isEmpty()){
                            break;
                        }

                        row = sheet.createRow(teacherSummaryRowNumber - 1);
                        cell = row.createCell(0);
                        cell.setCellStyle(style14RightAl);
                        cell.setCellValue(teacherSummaryRowNumber - 4);

                        cell = row.createCell(1);
                        cell.setCellStyle(style14LeftAl);
                        cell.setCellValue(teacher.getPosada());

                        cell = row.createCell(2);
                        cell.setCellStyle(style14LeftAl);
                        //Штат/сумісництво
                        cell.setCellValue(teacher.getNote());

                        cell = row.createCell(3);
                        cell.setCellStyle(style14LeftAl);
                        cell.setCellValue(teacher.getFullName());

                        cell = row.createCell(4);
                        cell.setCellStyle(style14);
                        cell.setCellValue(Double.parseDouble(teacher.getStavka()));

                        cell = row.createCell(5);
                        cell.setCellStyle(style14RightAl);
                        if (teacher.getTeacherHours().getAutumnSumPlan() != null && !teacher.getTeacherHours().getAutumnSumPlan().isEmpty()) {
                            cell.setCellValue(Math.round(Double.parseDouble(teacher.getTeacherHours().getAutumnSumPlan())));
                        } else {
                            cell.setCellValue(0);
                        }

                        cell = row.createCell(6);
                        cell.setCellStyle(style14RightAl);
                        if (teacher.getTeacherHours().getSpringSumPlan() != null && !teacher.getTeacherHours().getSpringSumPlan().isEmpty()) {
                            cell.setCellValue(Math.round(Double.parseDouble(teacher.getTeacherHours().getSpringSumPlan())));
                        } else {
                            cell.setCellValue(0);
                        }

                        cell = row.createCell(7);
                        cell.setCellStyle(style14RightAl);
                        if (teacher.getTeacherHours().getMasterProfNum() != null && !teacher.getTeacherHours().getMasterProfNum().isEmpty()) {
                            if (teacher.getTeacherHours().getMasterScNum() != null && !teacher.getTeacherHours().getMasterScNum().isEmpty()) {
                                cell.setCellValue((double) Math.round(Double.parseDouble(teacher.getTeacherHours().getMasterProfNum()))
                                        + Math.round(Double.parseDouble(teacher.getTeacherHours().getMasterScNum())));
                            } else {
                                cell.setCellValue((double) Math.round(Double.parseDouble(teacher.getTeacherHours().getMasterProfNum()))
                                        + 0);
                            }
                        } else {
                            if (teacher.getTeacherHours().getMasterScNum() != null && !teacher.getTeacherHours().getMasterScNum().isEmpty()) {
                                cell.setCellValue((double) 0
                                        + Math.round(Double.parseDouble(teacher.getTeacherHours().getMasterScNum())));
                            } else {
                                cell.setCellValue(0);
                            }

                        }

                        cell = row.createCell(8);
                        cell.setCellStyle(style14RightAl);
                        cell.setCellFormula("27*H" + teacherSummaryRowNumber);

                        cell = row.createCell(9);
                        cell.setCellStyle(style14RightAl);
                        if (teacher.getTeacherHours().getBachNum() != null && !teacher.getTeacherHours().getBachNum().isEmpty()) {
                            cell.setCellValue(Math.round(Double.parseDouble(teacher.getTeacherHours().getBachNum())));
                        } else {
                            cell.setCellValue(0);
                        }

                        cell = row.createCell(10);
                        cell.setCellStyle(style14RightAl);
                        cell.setCellFormula("14*J" + teacherSummaryRowNumber + "+3*J" + teacherSummaryRowNumber);

                        cell = row.createCell(11);
                        cell.setCellStyle(style14RightAl);
                        cell.setCellFormula("F" + teacherSummaryRowNumber + "+G" + teacherSummaryRowNumber
                                + "+H" + teacherSummaryRowNumber + "+I" + teacherSummaryRowNumber
                                + "+J" + teacherSummaryRowNumber + "+K" + teacherSummaryRowNumber);

                        cell = row.createCell(12);
                        cell.setCellStyle(style14RightAl);
                        cell.setCellFormula("E" + teacherSummaryRowNumber + "*" + 600);

                        cell = row.createCell(13);
                        cell.setCellStyle(style14RightAl);
                        cell.setCellFormula("L" + teacherSummaryRowNumber + "-M" + teacherSummaryRowNumber);

                        cell = row.createCell(14);
                        cell.setCellStyle(style14LeftAl);

                        teacherSummaryRowNumber++;
                    }
                }

                workbook.removeSheetAt(2);
                File someFile = new File(Dictionary.getResultsFolder() + PERSONAL_STUDY_LOAD_XLSX);
                try (FileOutputStream outputStream = new FileOutputStream(someFile)) {
                    workbook.write(outputStream);
                    formulary.setPslFilename(someFile.getName());
                    formularyService.save(formulary);
                    writePSLforTeacher();

                    writeSummaryInSeparateFile();

                    CreationMetric cr = new CreationMetric();
                    cr.setTeacherNumber(teachers.size());
                    cr.setTimeToForm((System.currentTimeMillis() - startTime));
                    log.info("Number of teachers: [{}]    Creation time: [{}]", cr.getTeacherNumber() + 100, cr.getTimeToForm());
                    metricService.save(cr);
                } catch (Exception e) {
                    log.error("Error creating resulting PSL file");
                    log.error(e);
                }
            } catch (Exception e) {
                log.error("Error creating workbook object");
                log.error(e);
            }
        } catch (Exception e) {
            log.error("Error reading PSL example");
            log.error(e);
        }
        return "redirect:/";
    }

    private double getStandardHours(String stavka, String posada, XSSFWorkbook workbook) {

        XSSFSheet sheet = workbook.getSheetAt(1);
        XSSFRow row;
        XSSFCell cell;

        switch (posada) {
            case "проф.":
                row = sheet.getRow(2);
                cell = row.getCell(getByStavka(stavka));
                return cell.getNumericCellValue();
            case "доц.":
                row = sheet.getRow(3);
                cell = row.getCell(getByStavka(stavka));
                return cell.getNumericCellValue();
            case "ст. викл.":
                row = sheet.getRow(4);
                cell = row.getCell(getByStavka(stavka));
                return cell.getNumericCellValue();
            case "ас.":
                row = sheet.getRow(5);
                cell = row.getCell(getByStavka(stavka));
                return cell.getNumericCellValue();
            default:
                return 600;
        }
    }

    private int getByStavka(String stavka) {
        switch (stavka) {
            case "0.25":
                return 6;
            case "0.5":
                return 5;
            case "1.0":
                return 4;
            case "1.25":
                return 3;
            case "1.5":
            default:
                return 2;
        }
    }

    private void writePSLforTeacher() throws IOException {
        List<Teacher> teachers = teacherService.listAll();
        for (Teacher teacher : teachers) {
            File originalWb = new File(Dictionary.getResultsFolder() + PERSONAL_STUDY_LOAD_XLSX);
            File clonedWb = new File(Dictionary.getResultsFolder() + UkrainianToLatin.generateLat(teacher.getName()) + " personal_study_load.xlsx");
            Files.copy(originalWb.toPath(), clonedWb.toPath(), StandardCopyOption.REPLACE_EXISTING);
            try (FileInputStream iS = new FileInputStream(clonedWb)) {

                XSSFWorkbookFactory wbF = new XSSFWorkbookFactory();
                try (XSSFWorkbook workbook = wbF.create(iS)) {
                    while (workbook.getNumberOfSheets() > 1) {
                        if (!teacher.getName().equals(workbook.getSheetAt(0).getSheetName())) {
                            workbook.removeSheetAt(0);
                        } else if (!teacher.getName().equals(workbook.getSheetAt(1).getSheetName())) {
                            workbook.removeSheetAt(1);
                        }
                    }
                    try (FileOutputStream outputStream = new FileOutputStream(clonedWb)) {
                        workbook.write(outputStream);
                        teacher.getTeacherHours().setPslFilename(clonedWb.getName());
                        teacherService.save(teacher);
                    } catch (Exception e) {
                        log.error(e);
                    }
                } catch (Exception e) {
                    log.error(e);
                }
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    private void writeSummaryInSeparateFile() throws IOException {
        File originalWb = new File(Dictionary.getResultsFolder() + PERSONAL_STUDY_LOAD_XLSX);
        File clonedWb = new File(Dictionary.getResultsFolder() + Dictionary.STUDYLOAD_SUMMARY_FILENAME_XLSX);
        Files.copy(originalWb.toPath(), clonedWb.toPath(), StandardCopyOption.REPLACE_EXISTING);
        try (FileInputStream iS = new FileInputStream(clonedWb)) {

            XSSFWorkbookFactory wbF = new XSSFWorkbookFactory();
            try (XSSFWorkbook workbook = wbF.create(iS)) {
                while (workbook.getNumberOfSheets() > 1) {
                    workbook.removeSheetAt(1);
                }
                try (FileOutputStream outputStream = new FileOutputStream(clonedWb)) {
                    workbook.write(outputStream);
                } catch (Exception e) {
                    log.error("Error writing summary to a file");
                    log.error(e);
                }
            } catch (Exception e) {
                log.error("Error creating a workbook for summary");
                log.error(e);
            }
        } catch (Exception e) {
            log.error("Error opening copied summary file");
            log.error(e);
        }

    }


    private int writeKerivnictvo(CellStyle style, Teacher teacher, CellStyle styleBold, int rownum, XSSFSheet sheet, boolean autumn, String[] endsKer) {
        XSSFRow row;
        XSSFCell cell;
        for (String end : endsKer) {
            row = sheet.createRow(rownum++);
            cell = row.createCell(0);
            cell.setCellValue(end);
            cell.setCellStyle(styleBold);
            for (int l = 1; l < 19; l++) {
                cell = row.createCell(l);
                cell.setCellStyle(style);
            }
            for (int l = 1; l < 19; l++) {
                switch (end.trim()) {
                    case (Dictionary.ASPIRANTS_DOCTORANTS):
                        if (l == 2) {
                            cell = row.createCell(l);
                            cell.setCellStyle(style);
                        }
                        if (l == 15) {
                            cell = row.createCell(l);
                            cell.setCellFormula("C" + rownum + "*25");
                            cell.setCellStyle(style);
                        }
                        break;
                    case ("Магістри професійні"):
                        if (l == 2 && teacher.getTeacherHours().getMasterProfNum() != null
                                && !teacher.getTeacherHours().getMasterProfNum().isEmpty()) {
                            cell = row.createCell(l);
                            cell.setCellValue((int) Double.parseDouble(teacher.getTeacherHours().getMasterProfNum()));
                            cell.setCellStyle(style);
                        }
                        if (l == 12) {
                            cell = row.createCell(l);
                            cell.setCellFormula("C" + rownum + "*27");
                            cell.setCellStyle(style);
                        }
                        break;
                    case ("Магістри наукові"):
                        if (l == 2 && teacher.getTeacherHours().getMasterScNum() != null
                                && !teacher.getTeacherHours().getMasterScNum().isEmpty()) {
                            cell = row.createCell(l);
                            cell.setCellValue((int) Double.parseDouble(teacher.getTeacherHours().getMasterScNum()));
                            cell.setCellStyle(style);
                        }
                        if (l == 12) {
                            cell = row.createCell(l);
                            cell.setCellFormula("C" + rownum + "*27");
                            cell.setCellStyle(style);
                        }
                        break;
                    case (Dictionary.BACHELORS):
                        if (l == 2 && teacher.getTeacherHours().getBachNum() != null
                                && !teacher.getTeacherHours().getBachNum().isEmpty()) {
                            cell = row.createCell(l);
                            cell.setCellValue((int) Double.parseDouble(teacher.getTeacherHours().getBachNum()));
                            cell.setCellStyle(style);
                        }
                        if (autumn) {
                            if (l == 9) {
                                cell = row.createCell(l);
                                cell.setCellFormula("C" + rownum + "*3");
                                cell.setCellStyle(style);
                            }
                        } else {
                            if (l == 12) {
                                cell = row.createCell(l);
                                cell.setCellFormula("C" + rownum + "*14");
                                cell.setCellStyle(style);
                            }
                        }
                        break;
                    case (Dictionary.COURSE_PROJECTS_5_COURSE):
                        if (l == 2 && teacher.getTeacherHours().getFifthCourseNum() != null
                                && !teacher.getTeacherHours().getFifthCourseNum().isEmpty()) {
                            cell = row.createCell(l);
                            cell.setCellValue((int) Double.parseDouble(teacher.getTeacherHours().getFifthCourseNum()));
                            cell.setCellStyle(style);
                        }
                        if (l == 9) {
                            cell = row.createCell(l);
                            cell.setCellFormula("C" + rownum + "*3");
                            cell.setCellStyle(style);
                        }
                        break;
                    default:
                        cell = row.createCell(l);
                        cell.setCellStyle(style);
                        break;
                }
            }
            cell = row.createCell(19);
            cell.setCellFormula(Dictionary.SUM_E_START_OF_THE_FORMULA + rownum + ":S" + rownum + ")");
            cell.setCellStyle(style); //

        }
        return rownum;
    }

    private double writeDisciplines(CellStyle style, CellStyle styleName, CellStyle styleGroups, XSSFRow row, PersonalLoadView personalLoadView) {
        XSSFCell cell;
        double sum = 0;
        cell = row.createCell(0);
        cell.setCellValue(personalLoadView.getDisciplineName());
        cell.setCellStyle(styleName);
        cell = row.createCell(1);
        if (!personalLoadView.getCourse().isEmpty()) {
            cell.setCellValue((int) Double.parseDouble(personalLoadView.getCourse()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(2);
        if (!personalLoadView.getStudentsNumber().isEmpty()) {
            cell.setCellValue((int) Double.parseDouble(personalLoadView.getStudentsNumber()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue(personalLoadView.getGroupNames());
        cell.setCellStyle(styleGroups);
        cell = row.createCell(4);
        if (!personalLoadView.getLecHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getLecHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(5);
        if (!personalLoadView.getConsultHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getConsultHours()));
            sum += Double.parseDouble(personalLoadView.getConsultHours());
        }
        cell.setCellStyle(style);
        cell = row.createCell(6);
        if (!personalLoadView.getLabHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getLabHours()));
            sum += Double.parseDouble(personalLoadView.getLabHours());
        }
        cell.setCellStyle(style);
        cell = row.createCell(7);
        if (!personalLoadView.getPractHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getPractHours()));
            sum += Double.parseDouble(personalLoadView.getPractHours());
        }
        cell.setCellStyle(style);
        cell = row.createCell(8);
        if (!personalLoadView.getIndTaskHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getIndTaskHours()));
            sum += Double.parseDouble(personalLoadView.getIndTaskHours());
        }
        cell.setCellStyle(style);
        cell = row.createCell(9);
        if (!personalLoadView.getCpHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getCpHours()));
            sum += Double.parseDouble(personalLoadView.getCpHours());
        }
        cell.setCellStyle(style);
        cell = row.createCell(10);
        if (!personalLoadView.getZalikHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getZalikHours()));
            sum += Double.parseDouble(personalLoadView.getZalikHours());
        }
        cell.setCellStyle(style);
        cell = row.createCell(11);
        if (!personalLoadView.getExamHours().isEmpty()) {
            cell.setCellValue(Math.round(Double.parseDouble(personalLoadView.getExamHours())));
            sum += Math.round(Double.parseDouble(personalLoadView.getExamHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(12);
        if (!personalLoadView.getDiplomaHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getDiplomaHours()));
            sum += Double.parseDouble(personalLoadView.getDiplomaHours());
        }
        cell.setCellStyle(style);
        cell = row.createCell(13);
        if (!personalLoadView.getDecCell().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getDecCell()));
            sum += Double.parseDouble(personalLoadView.getDecCell());
        }
        cell.setCellStyle(style);
        cell = row.createCell(14);
        if (!personalLoadView.getNdrs().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getNdrs()));
            sum += Double.parseDouble(personalLoadView.getNdrs());
        }
        cell.setCellStyle(style);
        cell = row.createCell(15);
        if (!personalLoadView.getAspirantHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getAspirantHours()));
            sum += Double.parseDouble(personalLoadView.getAspirantHours());
        }
        cell.setCellStyle(style);
        cell = row.createCell(16);
        if (!personalLoadView.getPractice().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getPractice()));
            sum += Double.parseDouble(personalLoadView.getPractice());
        }
        cell.setCellStyle(style);
        cell = row.createCell(17);
        cell.setCellStyle(style);
        cell = row.createCell(18);
        if (!personalLoadView.getOtherFormsHours().isEmpty()) {
            cell.setCellValue(Math.round(Double.parseDouble(personalLoadView.getOtherFormsHours())));
            sum += Math.round(Double.parseDouble(personalLoadView.getOtherFormsHours()));
        }
        cell.setCellStyle(style);
        return sum;
    }
}
