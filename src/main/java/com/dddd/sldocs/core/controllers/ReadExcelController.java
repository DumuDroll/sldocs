package com.dddd.sldocs.core.controllers;

import com.dddd.sldocs.core.entities.Professor;
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

    private final CurriculumService curriculumService;

    private final DepartmentService departmentService;

    private final DisciplineService disciplineService;

    private final FacultyService facultyService;

    private final ProfessorService professorService;

    public ReadExcelController(CurriculumService curriculumService, DepartmentService departmentService,
                               DisciplineService disciplineService, FacultyService facultyService,
                               ProfessorService professorService) {
        this.curriculumService = curriculumService;
        this.departmentService = departmentService;
        this.disciplineService = disciplineService;
        this.facultyService = facultyService;
        this.professorService = professorService;
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

        return "success/psToDB";
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

        return "success/ppsToDB";
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

        ArrayList<Object> arrayList = new ArrayList<>();
        ArrayList<Object> dep_fac_sem = new ArrayList<>();
        try {
            row = sheet.getRow(6);
            dep_fac_sem.add(row.getCell(0));
            dep_fac_sem.add(row.getCell(17));
            if (row.getCell(32).toString().equals("ОСІННІЙ")) {
                dep_fac_sem.add("1");
            } else {
                dep_fac_sem.add("2");
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int p = 0; p < 2; p++) {
                String[] values = dep_fac_sem.get(p).toString().split(Dictionary.SPACE_REGEX);

                for (int i = 1; i < values.length; i++) {
                    stringBuilder.append(values[i]).append(" ");
                }
                dep_fac_sem.set(p, stringBuilder);
                stringBuilder = new StringBuilder();
            }
            row = sheet.getRow(3);
            String[] values;
            for (int pp = 0; pp < row.getLastCellNum(); pp++) {
                if ((row.getCell(pp) != null) && !(row.getCell(pp).getStringCellValue().equals(""))) {
                    values = row.getCell(pp).getStringCellValue().split(Dictionary.SPACE_REGEX);
                    dep_fac_sem.add(values[6]);
                }
            }
            if (facultyService.findByName(dep_fac_sem.get(1).toString()) == null) {
                studyLoad.getDepartment().setName(dep_fac_sem.get(0).toString());
                studyLoad.getFaculty().setName(dep_fac_sem.get(1).toString());
                studyLoad.getFaculty().getDepartments().add(studyLoad.getDepartment());
                studyLoad.getDepartment().setFaculty(studyLoad.getFaculty());
                facultyService.save(studyLoad.getFaculty());
                departmentService.save(studyLoad.getDepartment());
            }

            for (int r = 10; r < rows; r++) {
                row = sheet.getRow(r);
                for (int c = 0; c < cols + 1; c++) {
                    XSSFCell cell = row.getCell(c);
                    readCell(arrayList, cell);
                }
                if (arrayList.get(5).toString().equals("асп")) {
                    break;
                }
                studyLoad.getCurriculum().setCourse(arrayList.get(3).toString());
                studyLoad.getCurriculum().setStudentsNumber(arrayList.get(4).toString());
                studyLoad.getCurriculum().setSemester(dep_fac_sem.get(2).toString());
                studyLoad.getCurriculum().setGroupNames(arrayList.get(5).toString());
                studyLoad.getCurriculum().setNumberOfSubgroups(arrayList.get(7).toString());
                studyLoad.getCurriculum().setLecHours(arrayList.get(17).toString());
                studyLoad.getCurriculum().setConsultsHours(arrayList.get(18).toString());
                studyLoad.getCurriculum().setLabHours(arrayList.get(19).toString());
                studyLoad.getCurriculum().setPractHours(arrayList.get(20).toString());
                studyLoad.getCurriculum().setIndTaskHours(arrayList.get(21).toString());
                studyLoad.getCurriculum().setCpHours(arrayList.get(22).toString());
                studyLoad.getCurriculum().setZalikHours(arrayList.get(23).toString());
                studyLoad.getCurriculum().setExamHours(arrayList.get(24).toString());
                studyLoad.getCurriculum().setDiplomaHours(arrayList.get(25).toString());
                studyLoad.getCurriculum().setDecCell(arrayList.get(26).toString());
                studyLoad.getCurriculum().setNdrs(arrayList.get(27).toString());
                studyLoad.getCurriculum().setAspirantHours(arrayList.get(28).toString());
                studyLoad.getCurriculum().setPractice(arrayList.get(29).toString());
                studyLoad.getCurriculum().setOtherFormsHours(arrayList.get(31).toString());
                studyLoad.getCurriculum().setYear(dep_fac_sem.get(3).toString());
                studyLoad.getCurriculum().setDepartment(departmentService.findByName(dep_fac_sem.get(0).toString()));

                if (professorService.findByName(arrayList.get(36).toString().trim()) == null) {
                    if (!(arrayList.get(36).toString().equals("") || arrayList.get(36).toString().equals("курсові"))) {
                        studyLoad.getProfessor().setName(arrayList.get(36).toString().trim());
                        professorService.save(studyLoad.getProfessor());
                        studyLoad.getCurriculum().getProfessors().add(studyLoad.getProfessor());
                    }
                } else {
                    studyLoad.getCurriculum().getProfessors().add(professorService.findByName(arrayList.get(36).toString()));
                }

                if (disciplineService.findByName(arrayList.get(1).toString()) == null) {
                    studyLoad.getDiscipline().setName(arrayList.get(1).toString());
                    disciplineService.save(studyLoad.getDiscipline());
                    studyLoad.getCurriculum().getDisciplines().add(studyLoad.getDiscipline());
                } else {
                    studyLoad.getCurriculum().getDisciplines().add(disciplineService.findByName(arrayList.get(1).toString()));
                }


                curriculumService.save(studyLoad.getCurriculum());
                arrayList = new ArrayList<>();
                studyLoad = new StudyLoad();
            }
        } catch (NullPointerException ex) {
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


            Professor professor = professorService.findByName(arrayList.get(1).toString().trim());

            if (professor == null) {
                professor = new Professor();
                professor.setName(arrayList.get(1).toString());
            }
            professor.setFullName(arrayList.get(2).toString());
            professor.setStavka(arrayList.get(3).toString());
            professor.setPosada(arrayList.get(4).toString());
            professor.setNaukStupin(arrayList.get(5).toString());
            professor.setVchZvana(arrayList.get(6).toString());
            professor.setNote(arrayList.get(7).toString());
            professor.setEmailAddress(arrayList.get(8).toString());
            professorService.save(professor);
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


            Professor professor = professorService.findByName(arrayList.get(1).toString().trim());

            if (professor == null) {
                professor = new Professor();
                professor.setName(arrayList.get(1).toString());
            }
            professor.setBachNum(arrayList.get(4).toString());
            professor.setFifthCourseNum(arrayList.get(5).toString());
            professor.setMasterProfNum(arrayList.get(6).toString());
            professor.setMasterScNum(arrayList.get(7).toString());
            professorService.save(professor);
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
