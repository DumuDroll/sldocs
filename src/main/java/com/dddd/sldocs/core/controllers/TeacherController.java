package com.dddd.sldocs.core.controllers;

import com.dddd.sldocs.auth.user_details.UserService;
import com.dddd.sldocs.core.entities.Teacher;
import com.dddd.sldocs.core.general.Dictionary;
import com.dddd.sldocs.core.general.utils.email.Sender;
import com.dddd.sldocs.core.services.TeacherService;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dddd.sldocs.core.general.utils.email.Sender.rfc5987_encode;

@Controller
public class TeacherController {

    private final TeacherService teacherService;
    private final UserService userService;

    public TeacherController(TeacherService teacherService, UserService userService) {
        this.teacherService = teacherService;
        this.userService = userService;
    }

    @RequestMapping("/teachers")
    public String viewTeachersPage(Model model) {
        List<Teacher> teachers = teacherService.listAllOrderName();
        model.addAttribute(Dictionary.TEACHERS, teachers);
        return Dictionary.TEACHERS;
    }

    @RequestMapping("/teacher/save")
    public String saveAndViewTeachersPage(@RequestParam("teacherId") long teacherId, @RequestParam("name") String name,
                                          @RequestParam("fullName") String fullName,
                                          @RequestParam("emailAddress") String emailAddress, Model model) {
        Teacher teacher = teacherService.getByID(teacherId);
        teacher.setName(name);
        teacher.setFullName(fullName);
        teacher.setEmailAddress(emailAddress);
        teacherService.save(teacher);
        List<Teacher> teachers = teacherService.listAllOrderName();
        model.addAttribute("teachers", teachers);
        return Dictionary.TEACHERS;
    }

    @RequestMapping("/teachers/docs")
    public String viewTeachersDocsSendPage(Model model) {
        List<Teacher> teachers = teacherService.listAllOrderName();
        model.addAttribute("teachers", teachers);
        return "teachers_docs";
    }

    @RequestMapping("/teacher/sendIpTo")
    public String sendIpTo(@RequestParam("name") String name, @RequestParam("email") String email) {
        Teacher teacher = teacherService.findByName(name);
        try {
            Sender.Send(email, Dictionary.INDIVIDUAL_PLANS_FOLDER, teacher.getTeacherHours().getIpFilename());
        } catch (Exception ex) {
            return "error/noFilesYet";
        }
        return getTimeString(teacher);
    }


    @RequestMapping("/teacher/sendPslTo")
    public String sendPslTo(@RequestParam("name") String name, @RequestParam("email") String email) {
        Teacher teacher = teacherService.findByName(name);
        try {
            Sender.Send(email, Dictionary.PERSONAL_STUDYLOAD_FOLDER, teacher.getTeacherHours().getPslFilename());
        } catch (Exception ex) {
            return "error/noFilesYet";
        }
        return getTimeString(teacher);
    }

    @RequestMapping("/teacher/sendIpToAll")
    public String sendIpToAll() throws MessagingException, UnsupportedEncodingException {
        List<Teacher> teachers = teacherService.listWithEmails();
        StringBuilder stringBuilder = new StringBuilder();
        for (Teacher teacher : teachers) {
            Sender.Send(teacher.getEmailAddress(), Dictionary.INDIVIDUAL_PLANS_FOLDER, teacher.getTeacherHours().getIpFilename());
            setEmailedDate(teacher, stringBuilder);
            stringBuilder = new StringBuilder();
        }
        return Dictionary.SUCCESS_EMAIL_SENT;
    }

    @RequestMapping("/teacher/sendPslToAll")
    public String sendPslToAll() throws MessagingException, UnsupportedEncodingException {
        List<Teacher> teachers = teacherService.listWithEmails();
        StringBuilder stringBuffer = new StringBuilder();
        for (Teacher teacher : teachers) {
            Sender.Send(teacher.getEmailAddress(), Dictionary.PERSONAL_STUDYLOAD_FOLDER, teacher.getTeacherHours().getPslFilename());
            setEmailedDate(teacher, stringBuffer);
            stringBuffer = new StringBuilder();
        }
        return "success/emailSent";
    }

    @GetMapping("/teacher/downloadIp")
    public ResponseEntity downloadIp(@RequestParam("teacherName") String teacherName) throws IOException {
        Teacher teacher = teacherService.findByName(userService.getUserByUsername(teacherName).getName());
        File someFile = new File(teacher.getTeacherHours().getIpFilename());
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.CONTENT_DISPOSITION, Dictionary.ATTACHMENT_FILENAME
                        + rfc5987_encode(teacher.getTeacherHours().getIpFilename()) + "\"")
                .body(FileUtils.readFileToByteArray(someFile));
    }

    @GetMapping("/teacher/downloadPsl")
    public ResponseEntity downloadPsl(@RequestParam("teacherName") String teacherName) throws IOException {
        Teacher teacher = teacherService.findByName(userService.getUserByUsername(teacherName).getName());
        File someFile = new File(teacher.getTeacherHours().getPslFilename());
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.CONTENT_DISPOSITION, Dictionary.ATTACHMENT_FILENAME
                        + rfc5987_encode(teacher.getTeacherHours().getPslFilename()) + "\"")
                .body(FileUtils.readFileToByteArray(someFile));
    }

    private String getTimeString(Teacher teacher) {
        StringBuilder stringBuilder = new StringBuilder();
        setEmailedDate(teacher, stringBuilder);
        return "success/emailSent";
    }

    private void setEmailedDate(Teacher teacher, StringBuilder stringBuilder) {
        Pattern pattern = Pattern.compile(Dictionary.TIME_REGEX);
        Matcher matcher = pattern.matcher(java.time.LocalDateTime.now().toString());
        while (matcher.find()) {
            stringBuilder.append(matcher.group(1)).append(" ").append(matcher.group(3));
        }
        teacher.setEmailedDate(stringBuilder.toString());
        teacherService.save(teacher);
    }

}
