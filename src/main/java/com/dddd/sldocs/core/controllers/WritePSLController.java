package com.dddd.sldocs.core.controllers;

import com.dddd.sldocs.core.entities.CreationMetric;
import com.dddd.sldocs.core.entities.Faculty;
import com.dddd.sldocs.core.entities.Professor;
import com.dddd.sldocs.core.entities.views.PersonalLoadView;
import com.dddd.sldocs.core.general.Dictionary;
import com.dddd.sldocs.core.general.utils.cyrToLatin.UkrainianToLatin;
import com.dddd.sldocs.core.services.CreationMetricService;
import com.dddd.sldocs.core.services.FacultyService;
import com.dddd.sldocs.core.services.PersonalLoadViewService;
import com.dddd.sldocs.core.services.ProfessorService;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.ss.util.RegionUtil;
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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
@Log4j2
public class WritePSLController {

    private final PersonalLoadViewService pls_vmService;

    private final CreationMetricService metricService;
    private final ProfessorService professorService;
    private final FacultyService facultyService;
    private static final String pslFileName = "personal_study_load.xlsx";
    private static final String TIMES_NEW_ROMAN = "Times New Roman";

    public WritePSLController(PersonalLoadViewService pls_vmService, ProfessorService professorService,
                              FacultyService facultyService, CreationMetricService metricService) {
        this.pls_vmService = pls_vmService;
        this.professorService = professorService;
        this.facultyService = facultyService;
        this.metricService = metricService;
    }

    @PostMapping("/uploadPSL")
    public String uploadPSLToLFS(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path path = Paths.get(Dictionary.RESULTS_FOLDER + fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writePSLforProf();
        return "success/pslToDB";
    }


    @RequestMapping("/PSL")
    public String writePSL() {
        long startTime = System.currentTimeMillis();
        try {
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

                    List<Professor> professors = professorService.listAll();
                    professors.sort(Comparator.comparing(Professor::getFullName, Comparator.nullsLast(Comparator.naturalOrder())));

                    XSSFRow row;
                    XSSFCell cell;

                    int last_vert_cell_sum;
                    int rownum;
                    for (Professor professor : professors) {
                        if (!professor.getName().equals("")) {
                            List<PersonalLoadView> personalLoadViewList;
                            //Если препод не имеет нагрузки, то все равно лист создается
//                            if (!pls_vmService.getPSL_VMData("1", professor.getName()).isEmpty()
//                                    || !pls_vmService.getPSL_VMData("2", professor.getName()).isEmpty()) {
                            XSSFSheet sheet = workbook.cloneSheet(1, professor.getName());
                            row = sheet.getRow(2);
                            cell = row.getCell(0);
                            cell.setCellValue(professor.getName());
                            cell.setCellStyle(style14Bold); //Изменил на жирный

                            cell = row.createCell(3);
                            cell.setCellValue("Ставка " + professor.getStavka()); //В одну ячейку Ставка и значение
                            cell.setCellStyle(style14);

//                                cell = row.createCell(4);
//                                cell.setCellValue(professor.getStavka());
//                                cell.setCellStyle(style14);

                            cell = row.getCell(5);
                            //Мне показалось это лишнее
//                                if (!pls_vmService.getPSL_VMData("1", professor.getName()).isEmpty()) {
//                                    personalLoadViewList = pls_vmService.getPSL_VMData("1", professor.getName());
//                                } else {
//                                    personalLoadViewList = pls_vmService.getPSL_VMData("2", professor.getName());
//                                }
                            CellRangeAddress cellRangeAddress = new CellRangeAddress(2, 2, 5, 19); //18 поменял на 19
                            sheet.addMergedRegion(cellRangeAddress);
                            // без границ, как есть
//                                RegionUtil.setBorderBottom(cell.getCellStyle().getBorderBottom(), cellRangeAddress, sheet);
//                                RegionUtil.setBorderTop(cell.getCellStyle().getBorderTop(), cellRangeAddress, sheet);
//                                RegionUtil.setBorderLeft(cell.getCellStyle().getBorderLeft(), cellRangeAddress, sheet);
//                                RegionUtil.setBorderRight(cell.getCellStyle().getBorderRight(), cellRangeAddress, sheet);
                            //Просто выводим номер кафедры
//                                cell.setCellValue("Кафедра " + personalLoadViewList.get(0).getDepName()); // + " на " + personalLoadViewList.get(0).getYear());
                            cell.setCellValue("Кафедра програмної інженерії та інтелектуальних технологій управління");
                            //cell.setCellStyle(style14);
                            cell.getCellStyle().setFont(font14); //Просто 14 шрифт TimesNewRoman

                            rownum = 4; //Поменялся пример -ОСЕНЬ -Норматив
                            //ОСІНЬ
                            row = sheet.createRow(rownum++);
                            cell = row.createCell(0);
                            cell.setCellValue("ОСІНЬ");
                            cell.setCellStyle(style14);
                            sheet.addMergedRegion(new CellRangeAddress(
                                    rownum - 1, //first row (0-based)
                                    rownum - 1, //last row  (0-based)
                                    0, //first column (0-based)
                                    19  //last column  (0-based)
                            ));
                            // Добавляем строки нагрузки, если есть в осеннем семестре
                            if (!pls_vmService.getPSL_VMData("1", professor.getName()).isEmpty()) {
                                personalLoadViewList = pls_vmService.getPSL_VMData("1", professor.getName());
                                for (PersonalLoadView personalLoadView : personalLoadViewList) {
                                    row = sheet.createRow(rownum++);
                                    //cell = row.createCell(0);
                                    cell = writeDisciplines(styleDiscHours, styleDiscName, styleDiscGroups, row, personalLoadView);//cell,
                                    cell.setCellFormula(Dictionary.SUM_E_START_OF_THE_FORMULA + rownum + ":S" + rownum + ")");
                                    cell.setCellStyle(styleDiscHours);
                                    row.setRowStyle(rowAutoHeightStyle);
                                }
                            }

                            //Добавляется пустая строка - вдруг что-то нужно добавит руками
                            row = sheet.createRow(rownum++);
                            for (int k = 0; k < 20; k++) {
                                cell = row.createCell(k);
                                cell.setCellValue("");
                                cell.setCellStyle(styleDiscHours);

                            }
                            // Данные по студентам, которыми руководит преподаватель
                            //row = sheet.createRow(rownum++);
                            String[] ends1 = {"КЕРІВНИЦТВО"};
                            rownum = writeKerivnictvo(styleDiscHours, professor, style12Bold, rownum, sheet, true, ends1);
                            String[] ends2 = {Dictionary.ASPIRANTS_DOCTORANTS, "Магістри професійні", Dictionary.BACHELORS, Dictionary.COURSE_PROJECTS_5_COURSE};
                            rownum = writeKerivnictvo(styleDiscHours, professor, style12RightAl, rownum, sheet, true, ends2);


                            int autumn_sum = rownum;
                            row = sheet.createRow(rownum++);
                            cell = row.createCell(0);
                            cell.setCellValue("Усього за осінь");
                            cell.setCellStyle(styleThickBotTopBord);

                            for (int k = 1; k < 4; k++) {
                                cell = row.createCell(k);
                                cell.setCellValue("");
                                cell.setCellStyle(styleThickBotTopBord);
                            }
                            String[] sums = {"E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S"};
                            int cell_count = 4;
                            last_vert_cell_sum = rownum - 1;
                            for (String sum : sums) {
                                cell = row.createCell(cell_count++);
                                cell.setCellFormula(Dictionary.ROUND_SUM_START_OF_THE_FORMULA + sum + "6:" + sum + last_vert_cell_sum + "),0)"); //7>>>6
                                cell.setCellStyle(styleThickBotTopBord);
                            }
                            cell = row.createCell(cell_count);
                            cell.setCellFormula("ROUND(SUM(T6:" + "T" + last_vert_cell_sum + "),0)");//7>>>6
                            cell.setCellStyle(styleThickBotTopRightBord);

                            //ВЕСНА
                            row = sheet.createRow(rownum++);
                            cell = row.createCell(0);
                            cell.setCellValue("ВЕСНА");
                            cell.setCellStyle(style14);
                            sheet.addMergedRegion(new CellRangeAddress(
                                    rownum - 1, //first row (0-based)
                                    rownum - 1, //last row  (0-based)
                                    0, //first column (0-based)
                                    19  //last column  (0-based)
                            ));
                            //Строку норматив - убираем
//                                row = sheet.createRow(rownum++);
//                                cell = row.createCell(0);
//                                cell.setCellValue("Норматив");
//                                cell.setCellStyle(styleThickBotTopBord);
//                                for (int k = 1; k < 19; k++) {
//                                    cell = row.createCell(k);
//                                    cell.setCellValue("");
//                                    cell.setCellStyle(styleThickBotTopBord);
//                                }
//                                cell = row.createCell(19);
//                                cell.setCellValue("");
//                                cell.setCellStyle(styleThickBotTopRightBord);
                            // Добавляем строки нагрузки, если есть в осеннем семестре
                            int first_sum_cell_spring = rownum + 1;
                            if (pls_vmService.getPSL_VMData("2", professor.getName()).size() != 0) {
                                personalLoadViewList = pls_vmService.getPSL_VMData("2", professor.getName());
                                for (PersonalLoadView personalLoadView : personalLoadViewList) {
                                    row = sheet.createRow(rownum++);
                                    //cell = row.createCell(0);
                                    cell = writeDisciplines(styleDiscHours, styleDiscName, styleDiscGroups, row, personalLoadView);//, cell
                                    cell.setCellFormula(Dictionary.SUM_E_START_OF_THE_FORMULA + rownum + ":S" + rownum + ")");
                                    cell.setCellStyle(styleDiscHours);
                                    row.setRowStyle(rowAutoHeightStyle);
                                }
                            }
                            //Добавляется пустая строка - вдруг что-то нужно добавит руками
                            row = sheet.createRow(rownum++);
                            for (int k = 0; k < 20; k++) {
                                cell = row.createCell(k);
                                cell.setCellValue("");
                                cell.setCellStyle(styleDiscHours);
                            }
                            // Данные по студентам, которыми руководит преподаватель
                            ends2 = new String[]{Dictionary.ASPIRANTS_DOCTORANTS, "Магістри наукові", Dictionary.BACHELORS, Dictionary.COURSE_PROJECTS_5_COURSE};
                            rownum = writeKerivnictvo(styleDiscHours, professor, style12Bold, rownum, sheet, false, ends1);
                            rownum = writeKerivnictvo(styleDiscHours, professor, style12RightAl, rownum, sheet, false, ends2);

                            row = sheet.createRow(rownum++);
                            cell = row.createCell(0);
                            cell.setCellValue("Усього за весну");
                            cell.setCellStyle(styleThickBotTopBord);

                            for (int k = 1; k < 4; k++) {
                                cell = row.createCell(k);
                                cell.setCellValue("");
                                cell.setCellStyle(styleThickBotTopBord);
                            }
                            cell_count = 4;
                            last_vert_cell_sum = rownum - 1;
                            for (String sum : sums) {
                                cell = row.createCell(cell_count++);
                                cell.setCellFormula(Dictionary.ROUND_SUM_START_OF_THE_FORMULA + sum + first_sum_cell_spring + ":" + sum + last_vert_cell_sum + "),0)");
                                cell.setCellStyle(styleThickBotTopBord);
                            }
                            cell = row.createCell(cell_count);
                            cell.setCellFormula("ROUND(SUM(T" + first_sum_cell_spring + ":" + "T" + last_vert_cell_sum + "),0)");
                            cell.setCellStyle(styleThickBotTopRightBord);

                            row = sheet.createRow(rownum++);
                            cell = row.createCell(0);
                            cell.setCellValue("УСЬОГО ЗА РІК");
                            cell.setCellStyle(styleThickBotBord);


                            for (int k = 1; k < 4; k++) {
                                cell = row.createCell(k);
                                cell.setCellValue("");
                                cell.setCellStyle(styleThickBotBord);
                            }
                            cell_count = 4;
                            for (String sum : sums) {
                                cell = row.createCell(cell_count++);
                                cell.setCellFormula(Dictionary.ROUND_SUM_START_OF_THE_FORMULA + sum + (autumn_sum + 1) + "+" + sum + (rownum - 1) + "),0)");
                                cell.setCellStyle(styleThickBotBord);
                            }
                            cell = row.createCell(cell_count);
                            cell.setCellFormula("ROUND(SUM(T" + (autumn_sum + 1) + "+" + "T" + (rownum - 1) + "),0)");
                            cell.setCellStyle(styleThickBotTopRightBord);

                            if (!(professor.getPosada() == null || professor.getPosada().equals(""))) {
                                row = sheet.createRow(rownum + 2);
                                cell = row.createCell(cell_count++);
                                cell.setCellValue(getStandartHours(professor.getStavka(), professor.getPosada(), workbook));
                                cell = row.createCell(cell_count);
                                cell.setCellFormula("T" + rownum + "-T" + (rownum + 3));
                            }
                            sheet.setFitToPage(true);
                            sheet.getPrintSetup().setLandscape(true);
//                           }
                        }
                    }

                    workbook.removeSheetAt(1);
                    File someFile = new File(Dictionary.RESULTS_FOLDER + pslFileName);
                    try (FileOutputStream outputStream = new FileOutputStream(someFile)) {
                        workbook.write(outputStream);
                        List<Faculty> faculties = facultyService.listAll();
                        faculties.get(0).setPslFilename(someFile.getName());
                        facultyService.save(faculties.get(0));
                        writePSLforProf();
                    } catch (Exception e) {
                        log.error(e);
                    }
                    CreationMetric cr = new CreationMetric();
                    cr.setProfessorNumber(professors.size());
                    cr.setTimeToForm((System.currentTimeMillis() - startTime));
                    log.info("Number of professors: [{}]    Creation time: [{}]", cr.getProfessorNumber() + 100, cr.getTimeToForm());
                    metricService.save(cr);
                } catch (Exception e) {
                    log.error(e);
                }
            } catch (Exception e) {
                log.error(e);
            }

        } catch (Exception ex) {
            log.error("Error creating PSL file");
            log.error(ex);
        }

        return "redirect:/";
    }

    private double getStandartHours(String stavka, String posada, XSSFWorkbook workbook) {

        XSSFSheet sheet = workbook.getSheetAt(0);
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
                return 400;
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

    private void writePSLforProf() throws IOException {
        List<Professor> professors = professorService.listAll();
        for (Professor professor : professors) {
            File originalWb = new File(Dictionary.RESULTS_FOLDER + pslFileName);
            File clonedWb = new File(Dictionary.RESULTS_FOLDER + UkrainianToLatin.generateLat(professor.getName()) + " personal_study_load.xlsx");
            Files.copy(originalWb.toPath(), clonedWb.toPath(), StandardCopyOption.REPLACE_EXISTING);
            try (FileInputStream iS = new FileInputStream(clonedWb)) {

                XSSFWorkbookFactory wbF = new XSSFWorkbookFactory();
                try (XSSFWorkbook workbook = wbF.create(iS)) {
                    while (workbook.getNumberOfSheets() > 1) {
                        if (!professor.getName().equals(workbook.getSheetAt(0).getSheetName())) {
                            workbook.removeSheetAt(0);
                        } else if (!professor.getName().equals(workbook.getSheetAt(1).getSheetName())) {
                            workbook.removeSheetAt(1);
                        }
                    }
                    try (FileOutputStream outputStream = new FileOutputStream(clonedWb)) {
                        workbook.write(outputStream);
                        professor.setPslFilename(clonedWb.getName());
                        professorService.save(professor);
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


    private int writeKerivnictvo(CellStyle style, Professor professor, CellStyle styleBold, int rownum, XSSFSheet sheet, boolean autumn, String[] endsKer) {
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
                        if (l == 2 && professor.getMasterProfNum() != null && !professor.getMasterProfNum().isEmpty()) {
                            cell = row.createCell(l);
                            cell.setCellValue((int) Double.parseDouble(professor.getMasterProfNum()));
                            cell.setCellStyle(style);
                        }
                        if (l == 12) {
                            cell = row.createCell(l);
                            cell.setCellFormula("C" + rownum + "*27");
                            cell.setCellStyle(style);
                        }
                        break;
                    case ("Магістри наукові"):
                        if (l == 2 && professor.getMasterScNum() != null && !professor.getMasterScNum().isEmpty()) {
                            cell = row.createCell(l);
                            cell.setCellValue((int) Double.parseDouble(professor.getMasterScNum()));
                            cell.setCellStyle(style);
                        }
                        if (l == 12) {
                            cell = row.createCell(l);
                            cell.setCellFormula("C" + rownum + "*27");
                            cell.setCellStyle(style);
                        }
                        break;
                    case (Dictionary.BACHELORS):
                        if (l == 2 && professor.getBachNum() != null && !professor.getBachNum().isEmpty()) {
                            cell = row.createCell(l);
                            cell.setCellValue((int) Double.parseDouble(professor.getBachNum()));
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
                        if (l == 2 && professor.getFifthCourseNum() != null && !professor.getFifthCourseNum().isEmpty()) {
                            cell = row.createCell(l);
                            cell.setCellValue((int) Double.parseDouble(professor.getFifthCourseNum()));
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

    private XSSFCell writeDisciplines(CellStyle style, CellStyle styleName, CellStyle styleGroups, XSSFRow row, PersonalLoadView personalLoadView) {
        XSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue(personalLoadView.getDname());
        cell.setCellStyle(styleName);
        cell = row.createCell(1);
        //cell.setCellValue(personalLoadView.getCourse());
        if (!personalLoadView.getCourse().isEmpty()) {
            cell.setCellValue((int) Double.parseDouble(personalLoadView.getCourse()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(2);
        //cell.setCellValue(personalLoadView.getStudentsNumber());
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
        }
        cell.setCellStyle(style);
        cell = row.createCell(6);
        if (!personalLoadView.getLabHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getLabHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(7);
        if (!personalLoadView.getPractHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getPractHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(8);
        if (!personalLoadView.getIndTaskHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getIndTaskHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(9);
        if (!personalLoadView.getCpHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getCpHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(10);
        if (!personalLoadView.getZalikHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getZalikHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(11);
        if (!personalLoadView.getExamHours().isEmpty()) {
            cell.setCellValue(Math.round(Double.parseDouble(personalLoadView.getExamHours())));
        }
        cell.setCellStyle(style);
        cell = row.createCell(12);
        if (!personalLoadView.getDiplomaHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getDiplomaHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(13);
        if (!personalLoadView.getDecCell().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getDecCell()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(14);
        if (!personalLoadView.getNdrs().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getNdrs()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(15);
        if (!personalLoadView.getAspirantHours().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getAspirantHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(16);
        if (!personalLoadView.getPractice().isEmpty()) {
            cell.setCellValue(Double.parseDouble(personalLoadView.getPractice()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(17);
        cell.setCellStyle(style);
        cell = row.createCell(18);
        if (!personalLoadView.getOtherFormsHours().isEmpty()) {
            cell.setCellValue(Math.round(Double.parseDouble(personalLoadView.getOtherFormsHours())));
        }
        cell.setCellStyle(style);
        cell = row.createCell(19);
        return cell;
    }


}
