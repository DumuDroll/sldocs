package com.dddd.sldocs.core.controllers;

import com.dddd.sldocs.auth.user_details.UserService;
import com.dddd.sldocs.core.entities.Professor;
import com.dddd.sldocs.core.general.Dictionary;
import com.dddd.sldocs.core.services.ProfessorService;
import com.dddd.sldocs.core.general.utils.email.Sender;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dddd.sldocs.core.general.utils.email.Sender.rfc5987_encode;

@Controller
public class ProfessorController {

    private final ProfessorService professorService;
    private final UserService userService;

    public ProfessorController(ProfessorService professorService, UserService userService) {
        this.professorService = professorService;
        this.userService = userService;
    }

    @RequestMapping("/professors")
    public String viewProfessorsPage(Model model) {
        List<Professor> professors = professorService.listAllOrderName();
        model.addAttribute(Dictionary.PROFESSORS, professors);
        return Dictionary.PROFESSORS;
    }

    @RequestMapping("/professor/save")
    public String saveAndViewProfessorsPage(@RequestParam("professorId") long professorId, @RequestParam("name") String name,
                                            @RequestParam("fullName") String fullName,
                                            @RequestParam("emailAddress") String emailAddress, Model model) {
        Professor professor = professorService.getByID(professorId);
        professor.setName(name);
        professor.setFullName(fullName);
        professor.setEmailAddress(emailAddress);
        professorService.save(professor);
        List<Professor> professors = professorService.listAllOrderName();
        model.addAttribute("professors", professors);
        return Dictionary.PROFESSORS;
    }

    @RequestMapping("/professors/docs")
    public String viewProfDocsSendPage(Model model) {
        List<Professor> professors = professorService.listAllOrderName();
        model.addAttribute("professors", professors);
        return "professors_docs";
    }

    @RequestMapping("/professor/sendIpTo")
    public String sendIpTo(@RequestParam("name") String name, @RequestParam("email") String email) {
        Professor professor = professorService.findByName(name);
        try {
            Sender.Send(email, professor.getIpFilename());
        } catch (NullPointerException ex) {
            return "error/noFilesYet";
        }
        return getTimeString(professor);
    }


    @RequestMapping("/professor/sendPslTo")
    public String sendPslTo(@RequestParam("name") String name, @RequestParam("email") String email) {
        Professor professor = professorService.findByName(name);
        try {
            Sender.Send(email, professor.getPslFilename());
        } catch (NullPointerException ex) {
            return "error/noFilesYet";
        }
        return getTimeString(professor);
    }

    @RequestMapping("/professor/sendIpToAll")
    public String sendIpToAll() {
        List<Professor> professors = professorService.listWithEmails();
        StringBuilder stringBuffer = new StringBuilder();
        for (Professor professor : professors) {
            Sender.Send(professor.getEmailAddress(), professor.getIpFilename());
            setEmailedDate(professor, stringBuffer);
            stringBuffer = new StringBuilder();
        }
        return Dictionary.SUCCESS_EMAIL_SENT;
    }

    @RequestMapping("/professor/sendPslToAll")
    public String sendPslToAll() {
        List<Professor> professors = professorService.listWithEmails();
        StringBuilder stringBuffer = new StringBuilder();
        for (Professor professor : professors) {
            Sender.Send(professor.getEmailAddress(), professor.getPslFilename());
            setEmailedDate(professor, stringBuffer);
            stringBuffer = new StringBuilder();
        }
        return "success/emailSent";
    }

    @GetMapping("/professor/downloadIp")
    public ResponseEntity downloadIp(@RequestParam("profName") String profName) throws IOException {
        Professor professor = professorService.findByName(userService.getUserByUsername(profName).getName());
        File someFile = new File(professor.getIpFilename());
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.CONTENT_DISPOSITION, Dictionary.ATTACHMENT_FILENAME
                        + rfc5987_encode(professor.getIpFilename()) + "\"")
                .body(FileUtils.readFileToByteArray(someFile));
    }

    @GetMapping("/professor/downloadPsl")
    public ResponseEntity downloadPsl(@RequestParam("profName") String profName) throws IOException {
        Professor professor = professorService.findByName(userService.getUserByUsername(profName).getName());
        File someFile = new File(professor.getPslFilename());
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.CONTENT_DISPOSITION, Dictionary.ATTACHMENT_FILENAME
                        + rfc5987_encode(professor.getPslFilename()) + "\"")
                .body(FileUtils.readFileToByteArray(someFile));
    }

    private String getTimeString(Professor professor) {
        StringBuilder stringBuffer = new StringBuilder();
        setEmailedDate(professor, stringBuffer);
        return "success/emailSent";
    }

    private void setEmailedDate(Professor professor, StringBuilder stringBuffer) {
        Pattern pattern = Pattern.compile(Dictionary.TIME_REGEX);
        Matcher matcher = pattern.matcher(java.time.LocalDateTime.now().toString());
        while (matcher.find()) {
            stringBuffer.append(matcher.group(1)).append(" ").append(matcher.group(3));
        }
        professor.setEmailedDate(stringBuffer.toString());
        professorService.save(professor);
    }

}
