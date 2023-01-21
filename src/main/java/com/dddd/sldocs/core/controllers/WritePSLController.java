package com.dddd.sldocs.core.controllers;

import com.dddd.sldocs.core.entities.Faculty;
import com.dddd.sldocs.core.entities.Professor;
import com.dddd.sldocs.core.entities.views.PersonalLoadView;
import com.dddd.sldocs.core.general.utils.cyrToLatin.UkrainianToLatin;
import com.dddd.sldocs.core.services.FacultyService;
import com.dddd.sldocs.core.services.PersonalLoadViewService;
import com.dddd.sldocs.core.services.ProfessorService;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
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
    private final ProfessorService professorService;
    private final FacultyService facultyService;
    private static final String pslFileName = "personal_study_load.xlsx";
    private static final String TIMES_NEW_ROMAN = "Times New Roman";

    public WritePSLController(PersonalLoadViewService pls_vmService, ProfessorService professorService,
                              FacultyService facultyService) {
        this.pls_vmService = pls_vmService;
        this.professorService = professorService;
        this.facultyService = facultyService;
    }

    @PostMapping("/uploadPSL")
    public String uploadPSLToLFS(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path path = Paths.get(fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writePSLforProf();
        return "redirect:/";
    }


    @RequestMapping("/PSL")
    public String writePSL() {

        long m = System.currentTimeMillis();
        try {
            InputStream inputStream = Files.newInputStream(new File("src/main/resources/PSLExample.xlsx").toPath());
            XSSFWorkbookFactory workbookFactory = new XSSFWorkbookFactory();
            XSSFWorkbook workbook = workbookFactory.create(inputStream);

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

            CellStyle style = workbook.createCellStyle();
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setFont(font10);
            style.setWrapText(true);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);


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

            CellStyle style14RightAl = workbook.createCellStyle();
            style14RightAl.setVerticalAlignment(VerticalAlignment.CENTER);
            style14RightAl.setAlignment(HorizontalAlignment.RIGHT);
            style14RightAl.setBorderBottom(BorderStyle.THIN);
            style14RightAl.setBorderLeft(BorderStyle.THIN);
            style14RightAl.setBorderRight(BorderStyle.THIN);
            style14RightAl.setBorderTop(BorderStyle.THIN);
            style14RightAl.setFont(font14);
            style14RightAl.setWrapText(true);

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
                    if (pls_vmService.getPSL_VMData("1", professor.getName()).size() != 0
                            || pls_vmService.getPSL_VMData("2", professor.getName()).size() != 0) {
                        XSSFSheet sheet = workbook.cloneSheet(1, professor.getName());
                        row = sheet.getRow(2);
                        cell = row.getCell(0);
                        cell.setCellValue(professor.getName());
                        cell.setCellStyle(style14);

                        cell = row.createCell(4);
                        cell.setCellValue("Ставка");
                        cell.setCellStyle(style14);

                        cell = row.createCell(4);
                        cell.setCellValue(professor.getStavka());
                        cell.setCellStyle(style14);

                        cell = row.getCell(5);
                        if (pls_vmService.getPSL_VMData("1", professor.getName()).size() != 0) {
                            personalLoadViewList = pls_vmService.getPSL_VMData("1", professor.getName());
                        } else {
                            personalLoadViewList = pls_vmService.getPSL_VMData("2", professor.getName());
                        }
                        CellRangeAddress cellRangeAddress = new CellRangeAddress(2, 2, 5, 18);
                        sheet.addMergedRegion(cellRangeAddress);
                        cell.setCellStyle(style14);
                        RegionUtil.setBorderBottom(cell.getCellStyle().getBorderBottom(), cellRangeAddress, sheet);
                        RegionUtil.setBorderTop(cell.getCellStyle().getBorderTop(), cellRangeAddress, sheet);
                        RegionUtil.setBorderLeft(cell.getCellStyle().getBorderLeft(), cellRangeAddress, sheet);
                        RegionUtil.setBorderRight(cell.getCellStyle().getBorderRight(), cellRangeAddress, sheet);
                        cell.setCellValue("Кафедра " + personalLoadViewList.get(0).getDepName() + " на " + personalLoadViewList.get(0).getYear());
                        cell.setCellStyle(style14);
                        rownum = 6;
                        if (pls_vmService.getPSL_VMData("1", professor.getName()).size() != 0) {
                            personalLoadViewList = pls_vmService.getPSL_VMData("1", professor.getName());
                            for (PersonalLoadView personalLoadView : personalLoadViewList) {
                                row = sheet.createRow(rownum++);
                                cell = row.createCell(0);
                                cell = writeDisciplines(style, row, cell, personalLoadView);
                                cell.setCellFormula("SUM(E" + rownum + ":S" + rownum + ")");
                                cell.setCellStyle(style);
                                row.setRowStyle(rowAutoHeightStyle);
                            }
                        }
                        String[] ends1 = {"КЕРІВНИЦТВО"};
                        rownum = writeKerivnictvo(style, professor, style14Bold, rownum, sheet, true, ends1);
                        String[] ends2 = {"Аспіранти, докторанти", "Магістри професійні", "Бакалаври", "Курсові 5 курс"};
                        rownum = writeKerivnictvo(style, professor, style14RightAl, rownum, sheet, true, ends2);


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
                            cell.setCellFormula("ROUND(SUM(" + sum + "7:" + sum + last_vert_cell_sum + "),0)");
                            cell.setCellStyle(styleThickBotTopBord);
                        }
                        cell = row.createCell(cell_count);
                        cell.setCellFormula("ROUND(SUM(T7:" + "T" + last_vert_cell_sum + "),0)");
                        cell.setCellStyle(styleThickBotTopRightBord);

                        row = sheet.createRow(rownum++);
                        cell = row.createCell(0);
                        cell.setCellValue("весна");
                        cell.setCellStyle(style14);
                        sheet.addMergedRegion(new CellRangeAddress(
                                rownum - 1, //first row (0-based)
                                rownum - 1, //last row  (0-based)
                                0, //first column (0-based)
                                19  //last column  (0-based)
                        ));
                        row = sheet.createRow(rownum++);
                        cell = row.createCell(0);
                        cell.setCellValue("Норматив");
                        cell.setCellStyle(styleThickBotTopBord);
                        for (int k = 1; k < 19; k++) {
                            cell = row.createCell(k);
                            cell.setCellValue("");
                            cell.setCellStyle(styleThickBotTopBord);
                        }
                        cell = row.createCell(19);
                        cell.setCellValue("");
                        cell.setCellStyle(styleThickBotTopRightBord);

                        int first_sum_cell_spring = rownum + 1;
                        if (pls_vmService.getPSL_VMData("2", professor.getName()).size() != 0) {
                            personalLoadViewList = pls_vmService.getPSL_VMData("2", professor.getName());
                            for (PersonalLoadView personalLoadView : personalLoadViewList) {
                                row = sheet.createRow(rownum++);
                                cell = row.createCell(0);
                                cell = writeDisciplines(style, row, cell, personalLoadView);
                                cell.setCellFormula("SUM(E" + rownum + ":S" + rownum + ")");
                                cell.setCellStyle(style);
                                row.setRowStyle(rowAutoHeightStyle);
                            }
                        }
                        ends2 = new String[]{"Аспіранти, докторанти", "Магістри наукові", "Бакалаври", "Курсові 5 курс"};
                        rownum = writeKerivnictvo(style, professor, style14Bold, rownum, sheet, false, ends1);
                        rownum = writeKerivnictvo(style, professor, style14RightAl, rownum, sheet, false, ends2);

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
                            cell.setCellFormula("ROUND(SUM(" + sum + first_sum_cell_spring + ":" + sum + last_vert_cell_sum + "),0)");
                            cell.setCellStyle(styleThickBotTopBord);
                        }
                        cell = row.createCell(cell_count);
                        cell.setCellFormula("ROUND(SUM(T" + first_sum_cell_spring + ":" + "T" + last_vert_cell_sum + "),0)");
                        cell.setCellStyle(styleThickBotTopRightBord);

                        row = sheet.createRow(rownum++);
                        cell = row.createCell(0);
                        cell.setCellValue("Загальний ПІДСУМОК");
                        cell.setCellStyle(styleThickBotBord);


                        for (int k = 1; k < 4; k++) {
                            cell = row.createCell(k);
                            cell.setCellValue("");
                            cell.setCellStyle(styleThickBotBord);
                        }
                        cell_count = 4;
                        for (String sum : sums) {
                            cell = row.createCell(cell_count++);
                            cell.setCellFormula("ROUND(SUM(" + sum + (autumn_sum + 1) + "+" + sum + (rownum - 1) + "),0)");
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
                    }
                }
            }

            workbook.removeSheetAt(1);
            inputStream.close();
            File someFile = new File(pslFileName);
            FileOutputStream outputStream = new FileOutputStream(someFile);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            List<Faculty> faculties = facultyService.listAll();
            faculties.get(0).setPslFilename(someFile.getName());
            facultyService.save(faculties.get(0));
            writePSLforProf();

        } catch (Exception ex) {
            log.error("Error creating PSL file");
            log.error(ex);
        }
        log.info("PSL created in {} seconds",(System.currentTimeMillis() - m)/100);
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
        }
        return 400;
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
                return 2;
        }
        return 2;
    }

    private void writePSLforProf() throws IOException {
        List<Professor> professors = professorService.listAll();
        for (Professor professor : professors) {
            File originalWb = new File(pslFileName);
            File clonedWb = new File(UkrainianToLatin.generateLat(professor.getName()) + " personal_study_load.xlsx");
            Files.copy(originalWb.toPath(), clonedWb.toPath(), StandardCopyOption.REPLACE_EXISTING);
            FileInputStream iS = new FileInputStream(clonedWb);
            XSSFWorkbookFactory wbF = new XSSFWorkbookFactory();
            XSSFWorkbook workbook = wbF.create(iS);
            while (workbook.getNumberOfSheets() > 1) {
                if (!professor.getName().equals(workbook.getSheetAt(0).getSheetName())) {
                    workbook.removeSheetAt(0);
                } else if (!professor.getName().equals(workbook.getSheetAt(1).getSheetName())) {
                    workbook.removeSheetAt(1);
                }

            }
            iS.close();
            FileOutputStream outputStream = new FileOutputStream(clonedWb);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            professor.setPslFilename(clonedWb.getName());
            professorService.save(professor);
        }
    }

    private int writeKerivnictvo(CellStyle style, Professor professor, CellStyle style14Bold, int rownum, XSSFSheet sheet, boolean autumn, String[] ends1) {
        XSSFRow row;
        XSSFCell cell;
        for (String end : ends1) {
            row = sheet.createRow(rownum++);
            cell = row.createCell(0);
            cell.setCellValue(end);
            cell.setCellStyle(style14Bold);
            for (int l = 1; l < 19; l++) {
                cell = row.createCell(l);
                //cell.setCellValue(0);
                cell.setCellStyle(style);
            }
            for (int l = 1; l < 19; l++) {
                switch (end.trim()) {
                    case ("Аспіранти, докторанти"):
                        if (l == 15) {
                            cell = row.createCell(l);
                            cell.setCellFormula("C" + rownum + "*25");
                            cell.setCellStyle(style);
                        }

                        if (l == 2) {
                            if (professor.getAspNum() != null && !professor.getAspNum().equals("")) {
                                cell = row.createCell(l);
                                cell.setCellValue(Double.parseDouble(professor.getAspNum()));
                                cell.setCellStyle(style);
                            }
                        }
                        break;
                    case ("Магістри професійні"):
                    case ("Магістри наукові"):
                        if (l == 12) {
                            cell = row.createCell(l);
                            cell.setCellFormula("C" + rownum + "*27");
                            cell.setCellStyle(style);
                        }
                        break;
                    case ("Бакалаври"):
                        if (autumn) {
                            if (l == 9) {
                                cell = row.createCell(l);
                                cell.setCellFormula("C" + rownum + "*3");
                                cell.setCellStyle(style);
                            }
                        } else {
                            if (l == 12) {
                                cell = row.createCell(l);
                                cell.setCellFormula("C" + rownum + "*3");
                                cell.setCellStyle(style);
                            }
                        }
                        break;
                    case ("Курсові 5 курс"):
                        if (l == 9) {
                            cell = row.createCell(l);
                            cell.setCellFormula("C" + rownum + "*3");
                            cell.setCellStyle(style);
                        }
                        break;
                    default:
                        cell = row.createCell(l);
                        cell.setCellValue(0);
                        cell.setCellStyle(style);
                        break;
                }
            }
            cell = row.createCell(19);
            cell.setCellFormula("SUM(E" + rownum + ":S" + rownum + ")");
            cell.setCellStyle(style);

        }
        return rownum;
    }

    private void writeAspHours(CellStyle style, Professor professor, boolean autumn, XSSFRow row, int l) {
        XSSFCell cell;
        if (autumn) {
            cell = row.createCell(l);
            if (professor.getAutumnAsp() != null && !professor.getAutumnAsp().equals("")) {
                cell.setCellValue(Double.parseDouble(professor.getAutumnAsp()));
            } else {
                cell.setCellValue(professor.getAutumnAsp());
            }
            cell.setCellStyle(style);
        } else {
            cell = row.createCell(l);
            if (professor.getSpringAsp() != null && !professor.getSpringAsp().equals("")) {
                cell.setCellValue(Double.parseDouble(professor.getSpringAsp()));
            } else {
                cell.setCellValue(professor.getSpringAsp());
            }
            cell.setCellStyle(style);
        }
    }

    private int writeLine(CellStyle style, int rownum, XSSFSheet sheet) {
        XSSFRow row;
        XSSFCell cell;
        row = sheet.createRow(rownum++);
        for (int l = 0; l < 19; l++) {
            cell = row.createCell(l);
            cell.setCellStyle(style);
        }
        cell = row.createCell(19);
        cell.setCellStyle(style);
        return rownum;
    }

    private XSSFCell writeDisciplines(CellStyle style, XSSFRow row, XSSFCell cell, PersonalLoadView personalLoadView) {
        cell.setCellValue(personalLoadView.getDname());
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(personalLoadView.getCourse());
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue(personalLoadView.getStudentsNumber());
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue(personalLoadView.getGroupNames());
        cell.setCellStyle(style);
        cell = row.createCell(4);
        if (personalLoadView.getLecHours().isEmpty()) {
            cell.setCellValue(personalLoadView.getLecHours());
        } else {
            cell.setCellValue(Double.parseDouble(personalLoadView.getLecHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(5);
        if (personalLoadView.getConsultHours().isEmpty()) {
            cell.setCellValue(personalLoadView.getConsultHours());
        } else {
            cell.setCellValue(Double.parseDouble(personalLoadView.getConsultHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(6);
        if (personalLoadView.getLabHours().isEmpty()) {
            cell.setCellValue(personalLoadView.getLabHours());
        } else {
            cell.setCellValue(Double.parseDouble(personalLoadView.getLabHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(7);
        if (personalLoadView.getPractHours().isEmpty()) {
            cell.setCellValue(personalLoadView.getPractHours());
        } else {
            cell.setCellValue(Double.parseDouble(personalLoadView.getPractHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(8);
        if (personalLoadView.getIndTaskHours().isEmpty()) {
            cell.setCellValue(personalLoadView.getIndTaskHours());
        } else {
            cell.setCellValue(Double.parseDouble(personalLoadView.getIndTaskHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(9);
        if (personalLoadView.getCpHours().isEmpty()) {
            cell.setCellValue(personalLoadView.getCpHours());
        } else {
            cell.setCellValue(Double.parseDouble(personalLoadView.getCpHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(10);
        if (personalLoadView.getZalikHours().isEmpty()) {
            cell.setCellValue(personalLoadView.getZalikHours());
        } else {
            cell.setCellValue(Double.parseDouble(personalLoadView.getZalikHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(11);
        if (personalLoadView.getExamHours().isEmpty()) {
            cell.setCellValue(personalLoadView.getExamHours());
        } else {
            cell.setCellValue(Math.round(Double.parseDouble(personalLoadView.getExamHours())));
        }
        cell.setCellStyle(style);
        cell = row.createCell(12);
        if (personalLoadView.getDiplomaHours().isEmpty()) {
            cell.setCellValue(personalLoadView.getDiplomaHours());
        } else {
            cell.setCellValue(Double.parseDouble(personalLoadView.getDiplomaHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(13);
        if (personalLoadView.getDecCell().isEmpty()) {
            cell.setCellValue(personalLoadView.getDecCell());
        } else {
            cell.setCellValue(Double.parseDouble(personalLoadView.getDecCell()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(14);
        if (personalLoadView.getNdrs().isEmpty()) {
            cell.setCellValue(personalLoadView.getNdrs());
        } else {
            cell.setCellValue(Double.parseDouble(personalLoadView.getNdrs()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(15);
        if (personalLoadView.getAspirantHours().isEmpty()) {
            cell.setCellValue(personalLoadView.getAspirantHours());
        } else {
            cell.setCellValue(Double.parseDouble(personalLoadView.getAspirantHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(16);
        if (personalLoadView.getPractice().isEmpty()) {
            cell.setCellValue(personalLoadView.getPractice());
        } else {
            cell.setCellValue(Double.parseDouble(personalLoadView.getPractice()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(17);
        cell.setCellValue(0);
        cell.setCellStyle(style);
        cell = row.createCell(18);
        if (personalLoadView.getOtherFormsHours().isEmpty()) {
            cell.setCellValue(personalLoadView.getOtherFormsHours());
        } else {
            cell.setCellValue(Double.parseDouble(personalLoadView.getOtherFormsHours()));
        }
        cell.setCellStyle(style);
        cell = row.createCell(19);
        return cell;
    }


}
