package com.dddd.sldocs.core.controllers;

import com.dddd.sldocs.core.entities.Teacher;
import com.dddd.sldocs.core.entities.StudyLoad;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
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

    private final DepartmentService departmentService;

    private final DisciplineService disciplineService;

    private final FacultyService facultyService;

    private final TeacherService teacherService;

    public ReadExcelController(StudyloadRowService studyloadRowService, DepartmentService departmentService,
                               DisciplineService disciplineService, FacultyService facultyService,
                               TeacherService teacherService) {
        this.studyloadRowService = studyloadRowService;
        this.departmentService = departmentService;
        this.disciplineService = disciplineService;
        this.facultyService = facultyService;
        this.teacherService = teacherService;
    }

    @PostMapping("/uploadObs")
    public String uploadObsToLFS(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path path = Paths.get(fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readObsyag(file.getOriginalFilename());
    }

    @PostMapping("/uploadPPS")
    public String uploadPPSToLFS(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path path = Paths.get(fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readPPS(file.getOriginalFilename());
    }

    @PostMapping("/uploadPS")
    public String uploadPSToLFS(@RequestParam("file") MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path path = Paths.get(fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readPS(file.getOriginalFilename());
    }


    @RequestMapping("/readObsyag")
    public String readObsyag(@RequestParam("path") String path) throws IOException {

        String[] parts = path.split("\\.");
        if (!parts[1].equals("xlsx")) {
            return Dictionary.ERROR_BAD_FILE;
        }

        try (FileInputStream fis = new FileInputStream(path);) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            String[] res = workbook.getSheetAt(0).getRow(3).getCell(3).toString()
                    .split(Dictionary.SPACE_REGEX);
            if (!res[0].equals("ПЛАН")) {
                return Dictionary.ERROR_BAD_FILE;
            }
            for (int i = 0; i < 2; i++) {
                readObsyagSheet(workbook, i);
            }
        } catch (Exception e) {
            log.error("Error reading obsyag");
            log.error(e);
        }

        return "success/obsyagToDB";
    }

    @RequestMapping("/readPPS")
    public String readPPS(@RequestParam("path") String path) throws IOException {
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

    public void readObsyagSheet(XSSFWorkbook workbook, int sheetNum) {
        XSSFSheet sheet = workbook.getSheetAt(sheetNum);
        DataFormatter df = new DataFormatter();
        StudyLoad studyLoad = new StudyLoad();
        int rows = 10;
        XSSFRow row;
        while (true) {

            row = sheet.getRow(rows);
            try {
                if (df.formatCellValue(row.getCell(3)).equals("")) {
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
            if (facultyService.findByName(depFacSem.get(1).toString()) == null) {
                studyLoad.getDepartment().setName(depFacSem.get(0).toString());
                studyLoad.getFaculty().setName(depFacSem.get(1).toString());
                studyLoad.getFaculty().getDepartments().add(studyLoad.getDepartment());
                studyLoad.getDepartment().setFaculty(studyLoad.getFaculty());
                facultyService.save(studyLoad.getFaculty());
                departmentService.save(studyLoad.getDepartment());
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
                studyLoad.getStudyloadRow().setDepartment(departmentService.findByName(depFacSem.get(0).toString()));

                if (teacherService.findByName(excelRow.get(36).toString().trim()) == null) {
                    if (!(excelRow.get(36).toString().equals("") || excelRow.get(36).toString().equals("курсові"))) {
                        studyLoad.getTeacher().setName(excelRow.get(36).toString().trim());
                        teacherService.save(studyLoad.getTeacher());
                        studyLoad.getStudyloadRow().getTeachers().add(studyLoad.getTeacher());
                    }
                } else {
                    studyLoad.getStudyloadRow().getTeachers().add(teacherService.findByName(excelRow.get(36).toString()));
                }

                if (disciplineService.findByName(excelRow.get(1).toString()) == null) {
                    studyLoad.getDiscipline().setName(excelRow.get(1).toString());
                    disciplineService.save(studyLoad.getDiscipline());
                    studyLoad.getStudyloadRow().getDisciplines().add(studyLoad.getDiscipline());
                } else {
                    studyLoad.getStudyloadRow().getDisciplines().add(disciplineService.findByName(excelRow.get(1).toString()));
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
            teacher.setBachNum(arrayList.get(4).toString());
            teacher.setFifthCourseNum(arrayList.get(5).toString());
            teacher.setMasterProfNum(arrayList.get(6).toString());
            teacher.setMasterScNum(arrayList.get(7).toString());
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
