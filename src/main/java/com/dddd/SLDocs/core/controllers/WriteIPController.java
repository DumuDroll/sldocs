package com.dddd.SLDocs.core.controllers;

import com.dddd.SLDocs.core.entities.Faculty;
import com.dddd.SLDocs.core.entities.Professor;
import com.dddd.SLDocs.core.entities.views.PSL_VM;
import com.dddd.SLDocs.core.servImpls.FacultyServiceImpl;
import com.dddd.SLDocs.core.servImpls.PSL_VMServiceImpl;
import com.dddd.SLDocs.core.servImpls.ProfessorServiceImpl;
import com.dddd.SLDocs.core.utils.cyrToLatin.UkrainianToLatin;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
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
import java.util.List;
import java.util.Objects;

@Controller
public class WriteIPController {
    private final PSL_VMServiceImpl pls_vmService;
    private final ProfessorServiceImpl professorService;
    private final FacultyServiceImpl facultyService;
    private double total_sum;
    private static final String TIMES_NEW_ROMAN = "Times New Roman";

    public WriteIPController(PSL_VMServiceImpl pls_vmService, ProfessorServiceImpl professorService,
                             FacultyServiceImpl facultyService) {
        this.pls_vmService = pls_vmService;
        this.professorService = professorService;
        this.facultyService = facultyService;
    }

    @PostMapping("/uploadIp")
    public String uploadAgain(@RequestParam("file") MultipartFile[] files) throws IOException {
        for(MultipartFile file : files){
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            Path path = Paths.get(fileName);
            try {
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/";
    }

    @RequestMapping("/IP")
    public String createExcel() {
        long m = System.currentTimeMillis();
        try {
            List<Professor> professors = professorService.ListAll();
            XSSFRow row;
            XSSFCell cell;
            int last_vert_cell_sum;
            int rownum;
            for (Professor professor : professors) {
                if (!professor.getName().equals("")) {
                    total_sum = 0;
                    List<PSL_VM> psl_vmList;
                    InputStream iS = getClass().getResourceAsStream("/BOOT-INF/classes/IndPlanExample.xlsx");
                    XSSFWorkbookFactory wbF = new XSSFWorkbookFactory();
                    XSSFWorkbook workbook = wbF.create(iS);

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

                    XSSFFont font12 = workbook.createFont();
                    font12.setFontHeightInPoints((short) 12);
                    font12.setFontName(TIMES_NEW_ROMAN);
                    font12.setBold(false);
                    font12.setItalic(false);

                    XSSFFont font12Bold = workbook.createFont();
                    font12Bold.setFontHeightInPoints((short) 12);
                    font12Bold.setFontName(TIMES_NEW_ROMAN);
                    font12Bold.setBold(true);
                    font12Bold.setItalic(false);

                    XSSFFont font12I = workbook.createFont();
                    font12I.setFontHeightInPoints((short) 12);
                    font12I.setFontName(TIMES_NEW_ROMAN);
                    font12I.setBold(false);
                    font12I.setItalic(true);

                    XSSFFont font12BI = workbook.createFont();
                    font12BI.setFontHeightInPoints((short) 12);
                    font12BI.setFontName(TIMES_NEW_ROMAN);
                    font12BI.setBold(true);
                    font12BI.setItalic(true);

                    XSSFFont font14 = workbook.createFont();
                    font14.setFontHeightInPoints((short) 14);
                    font14.setFontName(TIMES_NEW_ROMAN);
                    font14.setBold(false);
                    font14.setItalic(false);

                    XSSFFont font16 = workbook.createFont();
                    font16.setFontHeightInPoints((short) 16);
                    font16.setFontName(TIMES_NEW_ROMAN);
                    font16.setBold(false);
                    font16.setItalic(false);

                    XSSFFont font20 = workbook.createFont();
                    font16.setFontHeightInPoints((short) 20);
                    font16.setFontName(TIMES_NEW_ROMAN);
                    font16.setBold(true);
                    font16.setItalic(false);

                    CellStyle style = workbook.createCellStyle();
                    style.setVerticalAlignment(VerticalAlignment.CENTER);
                    style.setAlignment(HorizontalAlignment.CENTER);
                    style.setFont(font10);
                    style.setWrapText(true);
                    style.setBorderBottom(BorderStyle.THIN);
                    style.setBorderLeft(BorderStyle.THIN);
                    style.setBorderRight(BorderStyle.THIN);
                    style.setBorderTop(BorderStyle.THIN);

                    CellStyle style10 = workbook.createCellStyle();
                    style10.setVerticalAlignment(VerticalAlignment.CENTER);
                    style10.setAlignment(HorizontalAlignment.LEFT);
                    style10.setFont(font10);
                    style10.setWrapText(true);
                    style10.setBorderBottom(BorderStyle.THIN);
                    style10.setBorderLeft(BorderStyle.THIN);
                    style10.setBorderRight(BorderStyle.THIN);
                    style10.setBorderTop(BorderStyle.THIN);

                    CellStyle style12 = workbook.createCellStyle();
                    style12.setVerticalAlignment(VerticalAlignment.CENTER);
                    style12.setAlignment(HorizontalAlignment.LEFT);
                    style12.setFont(font12);
                    style12.setWrapText(true);
                    style12.setBorderBottom(BorderStyle.THIN);
                    style12.setBorderLeft(BorderStyle.THIN);
                    style12.setBorderRight(BorderStyle.THIN);
                    style12.setBorderTop(BorderStyle.THIN);

                    CellStyle style12I = workbook.createCellStyle();
                    style12I.setFont(font12I);
                    style12I.setWrapText(true);


                    CellStyle style12Bold = workbook.createCellStyle();
                    style12Bold.setVerticalAlignment(VerticalAlignment.CENTER);
                    style12Bold.setAlignment(HorizontalAlignment.LEFT);
                    style12Bold.setFont(font12Bold);
                    style12Bold.setWrapText(true);
                    style12Bold.setBorderBottom(BorderStyle.THIN);
                    style12Bold.setBorderLeft(BorderStyle.THIN);
                    style12Bold.setBorderRight(BorderStyle.THIN);
                    style12Bold.setBorderTop(BorderStyle.THIN);

                    CellStyle style12BI = workbook.createCellStyle();
                    style12BI.setVerticalAlignment(VerticalAlignment.CENTER);
                    style12BI.setAlignment(HorizontalAlignment.CENTER);
                    style12BI.setFont(font12Bold);
                    style12BI.setWrapText(true);
                    style12BI.setBorderBottom(BorderStyle.THIN);
                    style12BI.setBorderLeft(BorderStyle.THIN);
                    style12BI.setBorderRight(BorderStyle.THIN);
                    style12BI.setBorderTop(BorderStyle.THIN);

                    CellStyle style12ThickBotTop = workbook.createCellStyle();
                    style12ThickBotTop.setVerticalAlignment(VerticalAlignment.CENTER);
                    style12ThickBotTop.setAlignment(HorizontalAlignment.CENTER);
                    style12ThickBotTop.setFont(font12Bold);
                    style12ThickBotTop.setWrapText(true);
                    style12ThickBotTop.setBorderBottom(BorderStyle.THICK);
                    style12ThickBotTop.setBorderLeft(BorderStyle.THIN);
                    style12ThickBotTop.setBorderRight(BorderStyle.THIN);
                    style12ThickBotTop.setBorderTop(BorderStyle.THICK);

                    CellStyle style12ThickBotTopRight = workbook.createCellStyle();
                    style12ThickBotTopRight.setVerticalAlignment(VerticalAlignment.CENTER);
                    style12ThickBotTopRight.setAlignment(HorizontalAlignment.CENTER);
                    style12ThickBotTopRight.setFont(font12Bold);
                    style12ThickBotTopRight.setWrapText(true);
                    style12ThickBotTopRight.setBorderBottom(BorderStyle.THICK);
                    style12ThickBotTopRight.setBorderLeft(BorderStyle.THIN);
                    style12ThickBotTopRight.setBorderRight(BorderStyle.THICK);
                    style12ThickBotTopRight.setBorderTop(BorderStyle.THICK);

                    CellStyle style12ThickBotTopRal = workbook.createCellStyle();
                    style12ThickBotTopRal.setVerticalAlignment(VerticalAlignment.CENTER);
                    style12ThickBotTopRal.setAlignment(HorizontalAlignment.RIGHT);
                    style12ThickBotTopRal.setFont(font12Bold);
                    style12ThickBotTopRal.setWrapText(true);
                    style12ThickBotTopRal.setBorderBottom(BorderStyle.THICK);
                    style12ThickBotTopRal.setBorderLeft(BorderStyle.THIN);
                    style12ThickBotTopRal.setBorderRight(BorderStyle.THIN);
                    style12ThickBotTopRal.setBorderTop(BorderStyle.THICK);

                    CellStyle style14B = workbook.createCellStyle();
                    style14B.setVerticalAlignment(VerticalAlignment.CENTER);
                    style14B.setAlignment(HorizontalAlignment.CENTER);
                    style14B.setFont(font14);
                    style14B.setWrapText(true);
                    style14B.setBorderBottom(BorderStyle.THIN);
                    style14B.setBorderLeft(BorderStyle.THIN);
                    style14B.setBorderRight(BorderStyle.THIN);
                    style14B.setBorderTop(BorderStyle.THIN);

                    CellStyle style14Bot = workbook.createCellStyle();
                    style14Bot.setVerticalAlignment(VerticalAlignment.CENTER);
                    style14Bot.setAlignment(HorizontalAlignment.CENTER);
                    style14Bot.setFont(font14);
                    style14Bot.setWrapText(true);
                    style14Bot.setBorderBottom(BorderStyle.MEDIUM);
                    style14Bot.setBorderLeft(BorderStyle.THIN);
                    style14Bot.setBorderRight(BorderStyle.THIN);
                    style14Bot.setBorderTop(BorderStyle.THIN);

                    CellStyle style16Bot = workbook.createCellStyle();
                    style16Bot.setVerticalAlignment(VerticalAlignment.CENTER);
                    style16Bot.setAlignment(HorizontalAlignment.CENTER);
                    style16Bot.setFont(font16);
                    style16Bot.setWrapText(true);
                    style16Bot.setBorderBottom(BorderStyle.THIN);

                    CellStyle style20Bot = workbook.createCellStyle();
                    style20Bot.setVerticalAlignment(VerticalAlignment.CENTER);
                    style20Bot.setAlignment(HorizontalAlignment.CENTER);
                    style20Bot.setFont(font20);
                    style20Bot.setWrapText(true);
                    style20Bot.setBorderBottom(BorderStyle.THIN);

                    XSSFCellStyle rowAutoHeightStyle = workbook.createCellStyle();
                    rowAutoHeightStyle.setWrapText(true);
                    if (pls_vmService.getPSL_VMData("1", professor.getName()).size() != 0
                            || pls_vmService.getPSL_VMData("2", professor.getName()).size() != 0) {
                        if (pls_vmService.getPSL_VMData("1", professor.getName()).size() != 0) {
                            psl_vmList = pls_vmService.getPSL_VMData("1", professor.getName());
                        } else {
                            psl_vmList = pls_vmService.getPSL_VMData("2", professor.getName());
                        }
                        XSSFSheet sheet = workbook.getSheetAt(0);
                        row = sheet.getRow(8);
                        cell = row.getCell(1);
                        cell.setCellValue(psl_vmList.get(0).getFac_name());
                        cell.setCellStyle(style16Bot);
                        row = sheet.getRow(11);
                        cell = row.getCell(1);
                        cell.setCellValue(psl_vmList.get(0).getDep_name());
                        cell.setCellStyle(style16Bot);
                        row = sheet.getRow(23);
                        cell = row.getCell(0);
                        cell.setCellValue(professor.getFull_name());
                        //cell.setCellStyle(style20Bot);
                        row = sheet.getRow(33);
                        cell = row.getCell(0);
                        cell.setCellValue(psl_vmList.get(0).getYear());
                        cell.setCellStyle(style12BI);
                        cell = row.getCell(1);
                        cell.setCellValue(professor.getPosada());
                        cell.setCellStyle(style12BI);
                        cell = row.getCell(2);
                        cell.setCellValue(professor.getNauk_stupin());
                        cell.setCellStyle(style12BI);
                        cell = row.getCell(3);
                        cell.setCellValue(professor.getVch_zvana());
                        cell.setCellStyle(style12BI);
                        cell = row.getCell(4);
                        cell.setCellValue(professor.getStavka());
                        cell.setCellStyle(style12BI);
                        cell = row.getCell(5);
                        cell.setCellValue(professor.getNote());
                        cell.setCellStyle(style12BI);

                        sheet = workbook.getSheetAt(4);
                        row = sheet.getRow(12);
                        cell = row.getCell(1);
                        String sb = "Звіт про виконання індивідуального плану за" + " 2021/2022 навчальний рік викладача " +
                                getCellValue(workbook, 0, 23, 0) + " розглянуто розглянуто ___  _____________ 20__ р. " +
                                " на засіданні кафедри " + getCellValue(workbook, 0, 11, 1) + " й ухвалено рішення ( протокол №___):" +
                                " ): Індивідуальний план виконано в повному обсязі.";
                        cell.setCellValue(sb);
                        cell.setCellStyle(style10);

                        rownum = 4;
                        sheet = workbook.getSheetAt(2);
                        if (pls_vmService.getPSL_VMData("1", professor.getName()).size() != 0) {
                            psl_vmList.clear();
                            psl_vmList = pls_vmService.getPSL_VMData("1", professor.getName());
                            rownum = writeHours(cell, rownum, psl_vmList, style, rowAutoHeightStyle, sheet);
                        }
                        String[] ends1 = {"КЕРІВНИЦТВО"};
                        rownum = writeKerivnictvo(rownum, professor, style, style12Bold, sheet, true, ends1);
                        String[] ends2 = {"              Аспіранти, докторанти", "                  Магістри професійні",
                                "                  Бакалаври", "                  Курсові 5 курс"};
                        rownum = writeKerivnictvo(rownum, professor, style, style12, sheet, true, ends2);
                        row = sheet.createRow(rownum++);
                        int autumn_sum = rownum;
                        CellRangeAddress cellRangeAddress = new CellRangeAddress(rownum - 1, rownum - 1, 1, 6);
                        sheet.addMergedRegion(cellRangeAddress);
                        cell = row.createCell(1);
                        cell.setCellValue("РАЗОМ ЗА I СЕМЕСТР");
                        cell.setCellStyle(style12ThickBotTopRal);
                        RegionUtil.setBorderBottom(cell.getCellStyle().getBorderBottom(), cellRangeAddress, sheet);
                        RegionUtil.setBorderTop(cell.getCellStyle().getBorderTop(), cellRangeAddress, sheet);
                        RegionUtil.setBorderLeft(cell.getCellStyle().getBorderLeft(), cellRangeAddress, sheet);
                        RegionUtil.setBorderRight(cell.getCellStyle().getBorderRight(), cellRangeAddress, sheet);

                        String[] sums = {"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
                                "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD",
                                "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO",
                                "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW"};
                        int cell_count = 7;
                        last_vert_cell_sum = rownum - 1;
                        for (String sum : sums) {
                            cell = row.createCell(cell_count++);
                            cell.setCellFormula("ROUND(SUM(" + sum + "5:" + sum + last_vert_cell_sum + "),0)");
                            cell.setCellStyle(style12ThickBotTop);
                        }
                        cell = row.createCell(cell_count++);
                        cell.setCellFormula("H" + rownum + "+J" + rownum + "+L" + rownum + "+N" + rownum + "+P" + rownum +
                                "+R" + rownum + "+T" + rownum + "+V" + rownum + "+X" + rownum + "+Z" + rownum +
                                "+AB" + rownum + "+AD" + rownum + "+AF" + rownum + "+AH" + rownum + "+AL" + rownum + "+AJ" + rownum +
                                "+AN" + rownum + "+AP" + rownum + "+AR" + rownum + "+AT" + rownum + "+AV" + rownum);
                        cell.setCellStyle(style12ThickBotTop);
                        cell = row.createCell(cell_count);
                        cell.setCellFormula("I" + rownum + "+K" + rownum + "+M" + rownum + "+O" + rownum + "+Q" + rownum +
                                "+S" + rownum + "+U" + rownum + "+W" + rownum + "+Y" + rownum + "+AA" + rownum + "+AC" + rownum +
                                "+AE" + rownum + "+AG" + rownum + "+AI" + rownum + "+AK" + rownum + "+AM" + rownum + "+AO" + rownum +
                                "+AQ" + rownum + "+AS" + rownum + "+AU" + rownum + "+AW" + rownum);
                        //cell.setCellStyle(style12ThickBotTopRight);

                        if (pls_vmService.getPSL_VMData("2", professor.getName()).size() != 0) {
                            psl_vmList.clear();
                            psl_vmList = pls_vmService.getPSL_VMData("2", professor.getName());

                            rownum = writeHours(cell, rownum, psl_vmList, style, rowAutoHeightStyle, sheet);
                        }
                        ends2 = new String[]{"              Аспіранти, докторанти", "                  Магістри наукові",
                                "                  Бакалаври", "                  Курсові 5 курс"};
                        rownum = writeKerivnictvo(rownum, professor, style, style12Bold, sheet, false, ends1);
                        rownum = writeKerivnictvo(rownum, professor, style, style12, sheet, false, ends2);
                        row = sheet.createRow(rownum++);
                        int spring_sum = rownum;
                        cellRangeAddress = new CellRangeAddress(rownum - 1, rownum - 1, 1, 6);
                        sheet.addMergedRegion(cellRangeAddress);
                        cell = row.createCell(1);
                        cell.setCellValue("РАЗОМ ЗА II СЕМЕСТР");
                        cell.setCellStyle(style12ThickBotTopRal);
                        RegionUtil.setBorderBottom(cell.getCellStyle().getBorderBottom(), cellRangeAddress, sheet);
                        RegionUtil.setBorderTop(cell.getCellStyle().getBorderTop(), cellRangeAddress, sheet);
                        RegionUtil.setBorderLeft(cell.getCellStyle().getBorderLeft(), cellRangeAddress, sheet);
                        RegionUtil.setBorderRight(cell.getCellStyle().getBorderRight(), cellRangeAddress, sheet);
                        cell_count = 7;
                        last_vert_cell_sum = rownum - 1;
                        for (String sum : sums) {
                            cell = row.createCell(cell_count++);
                            cell.setCellFormula("ROUND(SUM(" + sum + (autumn_sum + 1) + ":" + sum + last_vert_cell_sum + "),0)");
                            cell.setCellStyle(style12ThickBotTop);
                        }
                        cell = row.createCell(cell_count++);
                        cell.setCellFormula("H" + rownum + "+J" + rownum + "+L" + rownum + "+N" + rownum + "+P" + rownum +
                                "+R" + rownum + "+T" + rownum + "+V" + rownum + "+X" + rownum + "+Z" + rownum +
                                "+AB" + rownum + "+AD" + rownum + "+AF" + rownum + "+AH" + rownum + "+AL" + rownum + "+AJ" + rownum +
                                "+AN" + rownum + "+AP" + rownum + "+AR" + rownum + "+AT" + rownum + "+AV" + rownum);
                        cell.setCellStyle(style12ThickBotTop);
                        cell = row.createCell(cell_count);
                        cell.setCellFormula("I" + rownum + "+K" + rownum + "+M" + rownum + "+O" + rownum + "+Q" + rownum +
                                "+S" + rownum + "+U" + rownum + "+W" + rownum + "+Y" + rownum + "+AA" + rownum + "+AC" + rownum +
                                "+AE" + rownum + "+AG" + rownum + "+AI" + rownum + "+AK" + rownum + "+AM" + rownum + "+AO" + rownum +
                                "+AQ" + rownum + "+AS" + rownum + "+AU" + rownum + "+AW" + rownum);
                        cell.setCellStyle(style12ThickBotTopRight);

                        row = sheet.createRow(rownum++);
                        cellRangeAddress = new CellRangeAddress(rownum - 1, rownum - 1, 1, 6);
                        sheet.addMergedRegion(cellRangeAddress);
                        cell = row.createCell(1);
                        cell.setCellValue("УСЬОГО за навчальний рік");
                        cell.setCellStyle(style12ThickBotTopRal);
                        RegionUtil.setBorderBottom(cell.getCellStyle().getBorderBottom(), cellRangeAddress, sheet);
                        RegionUtil.setBorderTop(cell.getCellStyle().getBorderTop(), cellRangeAddress, sheet);
                        RegionUtil.setBorderLeft(cell.getCellStyle().getBorderLeft(), cellRangeAddress, sheet);
                        RegionUtil.setBorderRight(cell.getCellStyle().getBorderRight(), cellRangeAddress, sheet);
                        cell_count = 7;
                        for (String sum : sums) {
                            cell = row.createCell(cell_count++);
                            cell.setCellFormula("ROUND(SUM(" + sum + autumn_sum + "+" + sum + spring_sum + "),0)");
                            cell.setCellStyle(style12ThickBotTop);
                        }
                        cell = row.createCell(cell_count++);
                        cell.setCellFormula("H" + rownum + "+J" + rownum + "+L" + rownum + "+N" + rownum + "+P" + rownum +
                                "+R" + rownum + "+T" + rownum + "+V" + rownum + "+X" + rownum + "+Z" + rownum +
                                "+AB" + rownum + "+AD" + rownum + "+AF" + rownum + "+AH" + rownum + "+AL" + rownum + "+AJ" + rownum +
                                "+AN" + rownum + "+AP" + rownum + "+AR" + rownum + "+AT" + rownum + "+AV" + rownum);
                        cell.setCellStyle(style12ThickBotTop);
                        cell = row.createCell(cell_count);
                        cell.setCellFormula("I" + rownum + "+K" + rownum + "+M" + rownum + "+O" + rownum + "+Q" + rownum +
                                "+S" + rownum + "+U" + rownum + "+W" + rownum + "+Y" + rownum + "+AA" + rownum + "+AC" + rownum +
                                "+AE" + rownum + "+AG" + rownum + "+AI" + rownum + "+AK" + rownum + "+AM" + rownum + "+AO" + rownum +
                                "+AQ" + rownum + "+AS" + rownum + "+AU" + rownum + "+AW" + rownum);
                        cell.setCellStyle(style12ThickBotTopRight);

                        row = sheet.createRow(rownum++);
                        for (int c = 0; c < 51; c++) {
                            row.createCell(c);
                        }
                        row = sheet.createRow(rownum++);
                        cellRangeAddress = new CellRangeAddress(rownum - 1, rownum - 1, 1, 9);
                        sheet.addMergedRegion(cellRangeAddress);
                        cell = row.createCell(1);
                        cell.setCellValue("Затвердженно на засіданні кафедри \"____\"___________20__р. Протокол № ____");
                        cell.setCellStyle(style12I);
                        for (int c = 10; c < 33; c++) {
                            row.createCell(c);
                        }
                        cellRangeAddress = new CellRangeAddress(rownum - 1, rownum - 1, 33, 44);
                        sheet.addMergedRegion(cellRangeAddress);
                        cell = row.createCell(33);
                        cell.setCellValue("Затвідувач кафедри _________________________");
                        cell.setCellStyle(style12I);

                        row = sheet.createRow(rownum++);
                        for (int c = 0; c < 51; c++) {
                            row.createCell(c);
                        }
                        row = sheet.createRow(rownum);
                        cell = row.createCell(2);
                        cell.setCellValue(professor.getName());
                        cell.setCellStyle(style12I);
                        for (int c = 3; c < 51; c++) {
                            row.createCell(c);
                        }
                        sheet.setFitToPage(true);
                        sheet.getPrintSetup().setFitWidth((short) 1);
                        sheet.getPrintSetup().setFitHeight((short) 0);
                        sheet.getPrintSetup().setLandscape(true);
                        sheet = workbook.getSheetAt(1);
                        row = sheet.getRow(14);
                        cell = row.createCell(3);
                        cell.setCellValue(total_sum);
                        cell.setCellStyle(style14B);
                        sheet.setFitToPage(true);
                        sheet.getPrintSetup().setLandscape(true);
                        row = sheet.getRow(18);
                        cell = row.createCell(3);
                        cell.setCellFormula("SUM(D15:D18)");
                        cell.setCellStyle(style14Bot);
                    }
                    iS.close();
                    File someFile = new File(UkrainianToLatin.generateLat(professor.getName()) + " ind_plan.xlsx");
                    FileOutputStream outputStream = new FileOutputStream(someFile);
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();

                    professor.setIp_filename(someFile.getName());
                    professorService.save(professor);
                }
            }
            Faculty faculty = facultyService.ListAll().get(0);
            faculty.setIpzip_filename("someFileName");
            facultyService.save(faculty);
        } catch (IOException | EncryptedDocumentException ex) {
            ex.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - m);

        return "redirect:/";
    }

    private String getCellValue(XSSFWorkbook workbook, int sheetNum, int rowNum, int cellNum){
        XSSFSheet sheet = workbook.getSheetAt(sheetNum);
        XSSFRow row = sheet.getRow(rowNum);
        return row.getCell(cellNum).getStringCellValue();
    }

    private int writeKerivnictvo(int rownum, Professor professor, CellStyle style, CellStyle style12Bold, XSSFSheet sheet, boolean autumn, String[] ends1) {
        XSSFRow row;
        XSSFCell cell;
        for (String end : ends1) {
            row = sheet.createRow(rownum++);
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell = row.createCell(2);
            cell.setCellValue(end);
            cell.setCellStyle(style12Bold);
            for (int l = 3; l < 49; l++) {
                cell = row.createCell(l);
                cell.setCellValue(0);
                cell.setCellStyle(style);
            }
            for (int l = 3; l < 49; l++) {
                switch (end.trim()) {
                    case ("Аспіранти, докторанти"):
                        if (l == 29) {
                            cell = row.createCell(l);
                            cell.setCellFormula("F" + rownum + "*25");
                            cell.setCellStyle(style);
                        }
                        if (l == 5) {
                            if (professor.getAsp_num() != null && !professor.getAsp_num().equals("")) {
                                cell = row.createCell(l);
                                cell.setCellValue(Double.parseDouble(professor.getAsp_num()));
                                cell.setCellStyle(style);
                            }
                        }
                        break;
                    case ("Магістри професійні"):
                    case ("Магістри наукові"):
                        if (l == 23) {
                            cell = row.createCell(l);
                            cell.setCellFormula("F" + rownum + "*27");
                            cell.setCellStyle(style);
                        }
                        break;
                    case ("Бакалаври"):
                        if(autumn){
                            if (l == 17) {
                                cell = row.createCell(l);
                                cell.setCellFormula("F" + rownum + "*3");
                                cell.setCellStyle(style);
                            }
                        }else{
                            if (l == 23) {
                                cell = row.createCell(l);
                                cell.setCellFormula("F" + rownum + "*3");
                                cell.setCellStyle(style);
                            }
                        }
                        break;
                    case ("Курсові 5 курс"):
                        if (l == 17) {
                            cell = row.createCell(l);
                            cell.setCellFormula("F" + rownum + "*3");
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
            cell = row.createCell(49);
            cell.setCellFormula("H" + rownum + "+J" + rownum + "+L" + rownum + "+N" + rownum + "+P" + rownum +
                    "+R" + rownum + "+T" + rownum + "+V" + rownum + "+X" + rownum + "+Z" + rownum +
                    "+AB" + rownum + "+AD" + rownum + "+AF" + rownum + "+AH" + rownum + "+AL" + rownum + "+AJ" + rownum +
                    "+AN" + rownum + "+AP" + rownum + "+AR" + rownum + "+AT" + rownum + "+AV" + rownum);
            cell.setCellStyle(style);
            cell = row.createCell(50);
            cell.setCellFormula("I" + rownum + "+K" + rownum + "+M" + rownum + "+O" + rownum + "+Q" + rownum +
                    "+S" + rownum + "+U" + rownum + "+W" + rownum + "+Y" + rownum + "+AA" + rownum + "+AC" + rownum +
                    "+AE" + rownum + "+AG" + rownum + "+AI" + rownum + "+AK" + rownum + "+AM" + rownum + "+AO" + rownum +
                    "+AQ" + rownum + "+AS" + rownum + "+AU" + rownum + "+AW" + rownum);
            cell.setCellStyle(style);
        }
        return rownum;
    }

    private int writeHours(XSSFCell cell, int rownum, List<PSL_VM> psl_vmList, CellStyle style, XSSFCellStyle rowAutoHeightStyle, XSSFSheet sheet) {
        XSSFRow row;
        for (PSL_VM psl_vm : psl_vmList) {
            row = sheet.createRow(rownum++);
            row.createCell(0);
            cell.setCellStyle(style);
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell = row.createCell(2);
            cell.setCellValue(psl_vm.getDname());
            cell.setCellStyle(style);
            cell = row.createCell(3);
            cell.setCellStyle(style);


            cell = row.createCell(4);
            if (psl_vm.getCourse().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Double.parseDouble(psl_vm.getCourse()));
            }
            cell.setCellStyle(style);
            cell = row.createCell(5);
            if (psl_vm.getStudents_number().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Double.parseDouble(psl_vm.getStudents_number()));
            }
            cell.setCellStyle(style);
            cell = row.createCell(6);
            cell.setCellValue(psl_vm.getGroup_names());
            cell.setCellStyle(style);

            cell = row.createCell(7);
            if (psl_vm.getLec_hours().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Double.parseDouble(psl_vm.getLec_hours()));
                total_sum += Double.parseDouble(psl_vm.getLec_hours());
            }
            cell.setCellStyle(style);

            cell = row.createCell(9);
            if (psl_vm.getConsult_hours().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Double.parseDouble(psl_vm.getConsult_hours()));
                total_sum += Double.parseDouble(psl_vm.getConsult_hours());
            }
            cell.setCellStyle(style);

            cell = row.createCell(11);
            if (psl_vm.getLab_hours().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Double.parseDouble(psl_vm.getLab_hours()));
                total_sum += Double.parseDouble(psl_vm.getLab_hours());
            }
            cell.setCellStyle(style);

            cell = row.createCell(13);
            if (psl_vm.getPract_hours().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Double.parseDouble(psl_vm.getPract_hours()));
                total_sum += Double.parseDouble(psl_vm.getPract_hours());
            }
            cell.setCellStyle(style);

            cell = row.createCell(15);
            if (psl_vm.getInd_task_hours().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Double.parseDouble(psl_vm.getInd_task_hours()));
                total_sum += Double.parseDouble(psl_vm.getInd_task_hours());
            }
            cell.setCellStyle(style);
            cell = row.createCell(17);
            if (psl_vm.getCp_hours().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Double.parseDouble(psl_vm.getCp_hours()));
                total_sum += Double.parseDouble(psl_vm.getCp_hours());
            }
            cell.setCellStyle(style);
            cell = row.createCell(19);
            if (psl_vm.getZalik_hours().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Double.parseDouble(psl_vm.getZalik_hours()));
                total_sum += Double.parseDouble(psl_vm.getZalik_hours());
            }
            cell.setCellStyle(style);
            cell = row.createCell(21);
            if (psl_vm.getExam_hours().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Math.round(Double.parseDouble(psl_vm.getExam_hours())));
                total_sum += Math.round(Double.parseDouble(psl_vm.getExam_hours()));
            }
            cell.setCellStyle(style);
            cell = row.createCell(23);
            if (psl_vm.getDiploma_hours().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Double.parseDouble(psl_vm.getDiploma_hours()));
                total_sum += Double.parseDouble(psl_vm.getDiploma_hours());
            }
            cell.setCellStyle(style);
            cell = row.createCell(25);
            if (psl_vm.getDec_cell().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Double.parseDouble(psl_vm.getDec_cell()));
                total_sum += Double.parseDouble(psl_vm.getDec_cell());
            }
            cell.setCellStyle(style);
            cell = row.createCell(27);
            if (psl_vm.getNdrs().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Double.parseDouble(psl_vm.getNdrs()));
                total_sum += Double.parseDouble(psl_vm.getNdrs());
            }
            cell.setCellStyle(style);
            cell = row.createCell(29);
            if (psl_vm.getAspirant_hours().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Double.parseDouble(psl_vm.getAspirant_hours()));
                total_sum += Double.parseDouble(psl_vm.getAspirant_hours());
            }
            cell.setCellStyle(style);
            cell = row.createCell(31);
            if (psl_vm.getPractice().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Double.parseDouble(psl_vm.getPractice()));
                total_sum += Double.parseDouble(psl_vm.getPractice());
            }
            cell.setCellStyle(style);
            cell = row.createCell(33);
            cell.setCellValue(0);
            cell.setCellStyle(style);
            cell = row.createCell(35);
            if (psl_vm.getOther_forms_hours().isEmpty()) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(Double.parseDouble(psl_vm.getOther_forms_hours()));
                total_sum += Double.parseDouble(psl_vm.getOther_forms_hours());
            }
            cell.setCellStyle(style);

            for (int c = 8; c < 37; c += 2) {
                cell = row.createCell(c);
                cell.setCellValue(Double.parseDouble("0"));
                cell.setCellStyle(style);
            }
            for (int c = 37; c < 49; c++) {
                cell = row.createCell(c);
                cell.setCellValue(0);
                cell.setCellStyle(style);
            }

            cell = row.createCell(49);
            cell.setCellFormula("H" + rownum + "+J" + rownum + "+L" + rownum + "+N" + rownum + "+P" + rownum +
                    "+R" + rownum + "+T" + rownum + "+V" + rownum + "+X" + rownum + "+Z" + rownum +
                    "+AB" + rownum + "+AD" + rownum + "+AF" + rownum + "+AH" + rownum + "+AL" + rownum + "+AJ" + rownum +
                    "+AN" + rownum + "+AP" + rownum + "+AR" + rownum + "+AT" + rownum + "+AV" + rownum);
            cell.setCellStyle(style);
            cell = row.createCell(50);
            cell.setCellFormula("I" + rownum + "+K" + rownum + "+M" + rownum + "+O" + rownum + "+Q" + rownum +
                    "+S" + rownum + "+U" + rownum + "+W" + rownum + "+Y" + rownum + "+AA" + rownum + "+AC" + rownum +
                    "+AE" + rownum + "+AG" + rownum + "+AI" + rownum + "+AK" + rownum + "+AM" + rownum + "+AO" + rownum +
                    "+AQ" + rownum + "+AS" + rownum + "+AU" + rownum + "+AW" + rownum);
            cell.setCellStyle(style);
            row.setRowStyle(rowAutoHeightStyle);
        }
        return rownum;
    }
}
