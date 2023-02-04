package com.dddd.sldocs.core.controllers;


import com.dddd.sldocs.core.entities.Faculty;
import com.dddd.sldocs.core.general.Dictionary;
import com.dddd.sldocs.core.services.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.dddd.sldocs.core.general.utils.email.Sender.rfc5987_encode;

@Log4j2
@Controller
public class IndexController {
    private final StudyloadRowService studyloadRowService;
    private final DepartmentService departmentService;
    private final DisciplineService disciplineService;
    private final FacultyService facultyService;
    private final ProfessorService professorService;
    private final SpecialtyService specialtyService;


    public IndexController(StudyloadRowService studyloadRowService, DepartmentService departmentService,
                           DisciplineService disciplineService, FacultyService facultyService,
                           ProfessorService professorService, SpecialtyService specialtyService) {
        this.studyloadRowService = studyloadRowService;
        this.departmentService = departmentService;
        this.disciplineService = disciplineService;
        this.facultyService = facultyService;
        this.professorService = professorService;
        this.specialtyService = specialtyService;
    }

    @GetMapping(path = "/")
    public String viewIndexPage(Model model) {
        String easfn = "";
        String pslfn = "";
        String ipzipfn = "";
        try {
            easfn = facultyService.listAll().get(0).getEasFilename();
            pslfn = facultyService.listAll().get(0).getPslFilename();
            ipzipfn = facultyService.listAll().get(0).getIndPlanZipFilename();
        } catch (IndexOutOfBoundsException ex) {
            log.info("no faculties");
        }
        boolean eas = true;
        boolean psl = true;
        boolean ip = true;
        if (easfn == null || easfn.isEmpty()) {
            eas = false;
        }
        if (pslfn == null || pslfn.isEmpty()) {
            psl = false;
        }
        if (ipzipfn == null || ipzipfn.isEmpty()) {
            ip = false;
        }
        model.addAttribute("eas", eas);
        model.addAttribute("psl", psl);
        model.addAttribute("ip", ip);
        return "index";
    }

    @GetMapping(path = "/help")
    public String viewHelpPage() {
        return "help";
    }

    @GetMapping(path = "/delete")
    public String viewDeletePage() {
        return "confirmation/deleteAllConf";
    }

    @GetMapping(path = "/deleteWOProfs")
    public String viewDeleteWOprofsPage() {
        return "confirmation/deleteWOprofsConf";
    }

    @GetMapping(path = "/deleteAll")
    public String deleteAll(Model model) {
        studyloadRowService.deleteAll();
        departmentService.deleteAll();
        disciplineService.deleteAll();
        facultyService.deleteAll();
        professorService.deleteAll();
        specialtyService.deleteAll();
        return "success/deleteAllSuc";
    }


    @GetMapping(path = "/deleteWithoutProfs")
    public String deleteWithoutProfs(Model model) {
        studyloadRowService.deleteAll();
        departmentService.deleteAll();
        disciplineService.deleteAll();
        facultyService.deleteAll();
        specialtyService.deleteAll();
        return "success/deleteWOprofsSuc";
    }

    @GetMapping("/downloadEAS")
    public ResponseEntity downloadEAS() throws IOException {
        Faculty faculty = facultyService.listAll().get(0);
        File file = new File(Dictionary.RESULTS_FOLDER + faculty.getEasFilename());
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.CONTENT_DISPOSITION, Dictionary.ATTACHMENT_FILENAME + rfc5987_encode(faculty.getEasFilename()) + "\"")
                .body(FileUtils.readFileToByteArray(file));
    }

    @GetMapping("/downloadPSL")
    public ResponseEntity downloadPSL() throws IOException {

        Faculty faculty = facultyService.listAll().get(0);
        File file = new File(Dictionary.RESULTS_FOLDER + faculty.getPslFilename());
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.CONTENT_DISPOSITION, Dictionary.ATTACHMENT_FILENAME + rfc5987_encode(faculty.getPslFilename()) + "\"")
                .body(FileUtils.readFileToByteArray(file));
    }

    @GetMapping(value = "/downloadIp", produces = "application/zip")
    public ResponseEntity downloadIpZip() throws IOException {
        File zipFile = new File(Dictionary.RESULTS_FOLDER + "Ind_plans.zip");
        FileOutputStream fos = new FileOutputStream(zipFile);
        try (ZipOutputStream zipOS = new ZipOutputStream(fos)) {
            List<String> fileNames = professorService.listIpFilenames();
            for (String fileName : fileNames) {
                File someFile = new File(Dictionary.RESULTS_FOLDER + fileName);
                writeToZipFile(Dictionary.RESULTS_FOLDER + someFile.getName(), zipOS);
            }
        }
        Faculty faculty = facultyService.listAll().get(0);
        faculty.setIndPlanZipFilename(zipFile.getName());
        facultyService.save(faculty);
        fos.close();
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.CONTENT_DISPOSITION, Dictionary.ATTACHMENT_FILENAME + rfc5987_encode(facultyService.listAll().get(0).getIndPlanZipFilename()) + "\"")
                .body(FileUtils.readFileToByteArray(zipFile));
    }

    public static void writeToZipFile(String path, ZipOutputStream zipStream) {
        File aFile = new File(path);
        try (FileInputStream fis = new FileInputStream(aFile)) {
            ZipEntry zipEntry = new ZipEntry(path);
            zipStream.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipStream.write(bytes, 0, length);
            }

            zipStream.closeEntry();
        } catch (IOException e) {
            log.error(e);
        }
    }
}
