package com.dddd.sldocs.core.controllers;

import com.dddd.sldocs.core.entities.Formulary;
import com.dddd.sldocs.core.entities.StudyLoad;
import com.dddd.sldocs.core.entities.Teacher;
import com.dddd.sldocs.core.general.Dictionary;
import com.dddd.sldocs.core.services.*;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Objects;

@Log4j2
@Controller
public class ReadExcelController {

    private final StudyloadRowService studyloadRowService;

    private final DisciplineService disciplineService;

    private final FormularyService formularyService;

    private final TeacherService teacherService;

    private final TeacherHoursService teacherHoursService;

    public ReadExcelController(StudyloadRowService studyloadRowService, DisciplineService disciplineService,
                               FormularyService formularyService, TeacherService teacherService,
                               TeacherHoursService teacherHoursService) {
        this.studyloadRowService = studyloadRowService;
        this.formularyService = formularyService;
        this.disciplineService = disciplineService;
        this.teacherService = teacherService;
        this.teacherHoursService = teacherHoursService;
    }

    @PostMapping("/uploadObs")
    public String uploadObsToLFS(@RequestParam("file") MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path path = Paths.get(fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error(e);
        }
        return readObsyag(file.getOriginalFilename());
    }

    @PostMapping("/uploadPPS")
    public String uploadPPSToLFS(@RequestParam("file") MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path path = Paths.get(fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error(e);
        }
        return readPPS(file.getOriginalFilename());
    }

    @PostMapping("/uploadPS")
    public String uploadPSToLFS(@RequestParam("file") MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path path = Paths.get(fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error(e);
        }
        return readPS(file.getOriginalFilename());
    }


    @RequestMapping("/readObsyag")
    public String readObsyag(@RequestParam("path") String path) {

        String[] parts = path.split("\\.");
        if (!parts[1].equals("xlsx")) {
            return Dictionary.ERROR_BAD_FILE;
        }

        try (FileInputStream fis = new FileInputStream(path);) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            String[] res = workbook.getSheetAt(1).getRow(3).getCell(3).toString()
                    .split(Dictionary.SPACE_REGEX);
            if (!res[0].equals("ПЛАН")) {
                return Dictionary.ERROR_BAD_FILE;
            }
            readFormulary(workbook);
            for (int i = 1; i < 3; i++) {
                readObsyagSheet(workbook, i);
            }
        } catch (Exception e) {
            log.error("Error reading obsyag");
            log.error(e);
        }

        return "success/obsyagToDB";
    }

    @RequestMapping("/readPPS")
    public String readPPS(@RequestParam("path") String path) {
        String[] parts = path.split("\\.");
        if (!parts[1].equals("xlsx")) {
            return Dictionary.ERROR_BAD_FILE;
        }

        try (FileInputStream fis = new FileInputStream(path)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            readPPSSheet(workbook);
        } catch (Exception e) {
            log.error("error reading pps");
            log.error(e);
        }

        return "success/ppsToDB";
    }

    @RequestMapping("/readPS")
    public String readPS(@RequestParam("path") String path) {
        String[] parts = path.split("\\.");
        if (!parts[1].equals("xlsx")) {
            return Dictionary.ERROR_BAD_FILE;
        }
        try (FileInputStream fis = new FileInputStream(path)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            readPSSheet(workbook);
        } catch (Exception e) {
            log.error(e);
        }

        return "success/psToDB";
    }

    private void readFormulary(XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row;
        ArrayList<Object> formularyValues = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
            row = sheet.getRow(i);
            readCell(formularyValues, row.getCell(1));
        }
        Formulary formulary = new Formulary();
        try {
            formulary.setFolderName(formularyValues.get(0).toString());
            formulary.setFileName(formularyValues.get(1).toString());
            formulary.setDepartmentShortName(formularyValues.get(2).toString());
            formulary.setDepartmentFullNameGenitiveCase(formularyValues.get(3).toString());
            formulary.setAcademicYear(formularyValues.get(4).toString());
            formulary.setDepartmentHeadTittle(formularyValues.get(5).toString());
            formulary.setDepartmentHeadPositionName(formularyValues.get(6).toString());
            formulary.setDepartmentHeadFullName(formularyValues.get(7).toString());
            formulary.setInstitute(formularyValues.get(8).toString());
            formulary.setDepartmentFullNameNominativeCase(formularyValues.get(9).toString());
            formulary.setProtocolNumber(formularyValues.get(10).toString());
            formulary.setProtocolDate(formularyValues.get(11).toString());
            formulary.setApprovedByTittle(formularyValues.get(12).toString());
            formulary.setApprovedByPosition(formularyValues.get(13).toString());
            formulary.setApprovedByFullName(formularyValues.get(14).toString());
            formularyService.save(formulary);
        } catch (Exception e) {
            log.error("Error reading formulary");
            log.error(e);
        }
    }

    public void readObsyagSheet(XSSFWorkbook workbook, int sheetNum) {
        XSSFSheet sheet = workbook.getSheetAt(sheetNum);
        DataFormatter df = new DataFormatter();
        StudyLoad studyLoad = new StudyLoad();
        int rows = 10;
        XSSFRow row;
        while (true) {

            row = sheet.getRow(rows);
            try {
                if (df.formatCellValue(row.getCell(1)).equals("")) {
                    break;
                }
                rows++;
            } catch (NullPointerException ex) {
                break;
            }
        }
        int cols = sheet.getRow(0).getLastCellNum();

        ArrayList<Object> excelRow = new ArrayList<>();
        ArrayList<Object> depFacSem = new ArrayList<>();
        try {
            row = sheet.getRow(6);
            depFacSem.add(row.getCell(0));
            depFacSem.add(row.getCell(17));
            if (row.getCell(32).toString().equals("ОСІННІЙ")) {
                depFacSem.add("1");
            } else {
                depFacSem.add("2");
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int p = 0; p < 2; p++) {
                String[] values = depFacSem.get(p).toString().split(Dictionary.SPACE_REGEX);

                for (int i = 1; i < values.length; i++) {
                    stringBuilder.append(values[i]).append(" ");
                }
                depFacSem.set(p, stringBuilder);
                stringBuilder = new StringBuilder();
            }
            row = sheet.getRow(3);
            String[] values;
            for (int pp = 0; pp < row.getLastCellNum(); pp++) {
                if ((row.getCell(pp) != null) && !(row.getCell(pp).getStringCellValue().equals(""))) {
                    values = row.getCell(pp).getStringCellValue().split(Dictionary.SPACE_REGEX);
                    depFacSem.add(values[6]);
                }
            }

            for (int r = 10; r < rows; r++) {
                row = sheet.getRow(r);
                for (int c = 0; c < cols + 1; c++) {
                    XSSFCell cell = row.getCell(c);
                    readCell(excelRow, cell);
                }
                if (excelRow.get(5).toString().equals("асп")) {
                    break;
                }
                studyLoad.getStudyloadRow().setCourse(excelRow.get(3).toString());
                studyLoad.getStudyloadRow().setStudentsNumber(excelRow.get(4).toString());
                studyLoad.getStudyloadRow().setSemester(depFacSem.get(2).toString());
                studyLoad.getStudyloadRow().setGroupNames(excelRow.get(5).toString());
                studyLoad.getStudyloadRow().setNumberOfSubgroups(excelRow.get(7).toString());
                studyLoad.getStudyloadRow().setLecHours(excelRow.get(17).toString());
                studyLoad.getStudyloadRow().setConsultsHours(excelRow.get(18).toString());
                studyLoad.getStudyloadRow().setLabHours(excelRow.get(19).toString());
                studyLoad.getStudyloadRow().setPractHours(excelRow.get(20).toString());
                studyLoad.getStudyloadRow().setIndTaskHours(excelRow.get(21).toString());
                studyLoad.getStudyloadRow().setCpHours(excelRow.get(22).toString());
                studyLoad.getStudyloadRow().setZalikHours(excelRow.get(23).toString());
                studyLoad.getStudyloadRow().setExamHours(excelRow.get(24).toString());
                studyLoad.getStudyloadRow().setDiplomaHours(excelRow.get(25).toString());
                studyLoad.getStudyloadRow().setDecCell(excelRow.get(26).toString());
                studyLoad.getStudyloadRow().setNdrs(excelRow.get(27).toString());
                studyLoad.getStudyloadRow().setAspirantHours(excelRow.get(28).toString());
                studyLoad.getStudyloadRow().setPractice(excelRow.get(29).toString());
                studyLoad.getStudyloadRow().setOtherFormsHours(excelRow.get(31).toString());
                studyLoad.getStudyloadRow().setYear(depFacSem.get(3).toString());

                if (teacherService.findByName(excelRow.get(36).toString().trim()) == null) {
                    if (!(excelRow.get(36).toString().equals("") || excelRow.get(36).toString().equals("курсові"))) {
                        studyLoad.getTeacher().setName(excelRow.get(36).toString().trim());
                        teacherService.save(studyLoad.getTeacher());
                        studyLoad.getStudyloadRow().setTeacher(studyLoad.getTeacher());
                    }
                } else {
                    studyLoad.getStudyloadRow().setTeacher(teacherService.findByName(excelRow.get(36).toString().trim()));
                }

                if (disciplineService.findByName(excelRow.get(1).toString().trim()) == null) {
                    studyLoad.getDiscipline().setName(excelRow.get(1).toString().trim());
                    disciplineService.save(studyLoad.getDiscipline());
                    studyLoad.getStudyloadRow().setDiscipline(studyLoad.getDiscipline());
                } else {
                    studyLoad.getStudyloadRow().setDiscipline(disciplineService.findByName(excelRow.get(1).toString().trim()));
                }


                studyloadRowService.save(studyLoad.getStudyloadRow());
                excelRow = new ArrayList<>();
                studyLoad = new StudyLoad();
            }
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    private void readPPSSheet(XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.getSheetAt(0);
        DataFormatter df = new DataFormatter();
        int rows = 3;
        XSSFRow row;
        while (true) {

            row = sheet.getRow(rows);
            try {
                if (df.formatCellValue(row.getCell(1)).equals("")) {
                    break;
                }
                rows++;
            } catch (NullPointerException ex) {
                break;
            }
        }
        ArrayList<Object> arrayList = new ArrayList<>();
        for (int r = 3; r < rows; r++) {
            row = sheet.getRow(r);
            for (int c = 0; c < 9; c++) {
                XSSFCell cell = row.getCell(c);
                readCell(arrayList, cell);
            }


            Teacher teacher = teacherService.findByName(arrayList.get(1).toString().trim());

            if (teacher == null) {
                teacher = new Teacher();
                teacher.setName(arrayList.get(1).toString());
            }
            teacher.setFullName(arrayList.get(2).toString());
            teacher.setStavka(arrayList.get(3).toString());
            teacher.setPosada(arrayList.get(4).toString());
            teacher.setNaukStupin(arrayList.get(5).toString());
            teacher.setVchZvana(arrayList.get(6).toString());
            teacher.setNote(arrayList.get(7).toString());
            teacher.setEmailAddress(arrayList.get(8).toString());
            teacherService.save(teacher);
            arrayList = new ArrayList<>();
        }

    }

    private void readPSSheet(XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.getSheetAt(1);
        DataFormatter df = new DataFormatter();
        int rows = 3;
        XSSFRow row;
        while (true) {

            row = sheet.getRow(rows);
            try {
                if (df.formatCellValue(row.getCell(1)).equals("")) {
                    break;
                }
                rows++;
            } catch (NullPointerException ex) {
                break;
            }
        }
        ArrayList<Object> arrayList = new ArrayList<>();
        for (int r = 3; r < rows; r++) {
            row = sheet.getRow(r);
            for (int c = 0; c < 8; c++) {
                XSSFCell cell = row.getCell(c);
                readCell(arrayList, cell);
            }


            Teacher teacher = teacherService.findByName(arrayList.get(1).toString().trim());

            if (teacher == null) {
                teacher = new Teacher();
                teacher.setName(arrayList.get(1).toString());
            }
            teacher.getTeacherHours().setBachNum(arrayList.get(4).toString());
            teacher.getTeacherHours().setFifthCourseNum(arrayList.get(5).toString());
            teacher.getTeacherHours().setMasterProfNum(arrayList.get(6).toString());
            teacher.getTeacherHours().setMasterScNum(arrayList.get(7).toString());
            teacherService.save(teacher);
            arrayList = new ArrayList<>();
        }

    }

    private void readCell(ArrayList<Object> arrayList, XSSFCell cell) {
        if (cell == null) {
            arrayList.add("");
        } else {
            switch (cell.getCellType()) {
                case STRING:
                    arrayList.add(cell.getStringCellValue());
                    break;
                case NUMERIC:
                    arrayList.add(cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    arrayList.add(cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    switch (cell.getCachedFormulaResultType()) {
                        case NUMERIC:
                            arrayList.add(cell.getNumericCellValue());
                            break;
                        case STRING:
                            arrayList.add(cell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    arrayList.add("");
                    break;
            }
        }
    }
}
