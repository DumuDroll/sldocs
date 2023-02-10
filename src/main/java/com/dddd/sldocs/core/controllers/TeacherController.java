package com.dddd.sldocs.core.controllers;

import com.dddd.sldocs.auth.user_details.UserService;
import com.dddd.sldocs.core.entities.Teacher;
import com.dddd.sldocs.core.general.Dictionary;
import com.dddd.sldocs.core.services.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
//            Sender.Send(email, teacher.getIpFilename());
        } catch (NullPointerException ex) {
            return "error/noFilesYet";
        }
        return getTimeString(teacher);
    }


    @RequestMapping("/teacher/sendPslTo")
    public String sendPslTo(@RequestParam("name") String name, @RequestParam("email") String email) {
        Teacher teacher = teacherService.findByName(name);
        try {
//            Sender.Send(email, teacher.getPslFilename());
        } catch (NullPointerException ex) {
            return "error/noFilesYet";
        }
        return getTimeString(teacher);
    }

    @RequestMapping("/teacher/sendIpToAll")
    public String sendIpToAll() {
        List<Teacher> teachers = teacherService.listWithEmails();
        StringBuilder stringBuilder = new StringBuilder();
        for (Teacher teacher : teachers) {
//            Sender.Send(teacher.getEmailAddress(), teacher.getIpFilename());
            setEmailedDate(teacher, stringBuilder);
            stringBuilder = new StringBuilder();
        }
        return Dictionary.SUCCESS_EMAIL_SENT;
    }

    @RequestMapping("/teacher/sendPslToAll")
    public String sendPslToAll() {
        List<Teacher> teachers = teacherService.listWithEmails();
        StringBuilder stringBuffer = new StringBuilder();
        for (Teacher teacher : teachers) {
//            Sender.Send(teacher.getEmailAddress(), teacher.getPslFilename());
            setEmailedDate(teacher, stringBuffer);
            stringBuffer = new StringBuilder();
        }
        return "success/emailSent";
    }

    @GetMapping("/teacher/downloadIp")
    public ResponseEntity downloadIp(@RequestParam("profName") String profName) throws IOException {
        Teacher teacher = teacherService.findByName(userService.getUserByUsername(profName).getName());
//        File someFile = new File(teacher.getIpFilename());
//        return ResponseEntity.ok()
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .header(HttpHeaders.CONTENT_DISPOSITION, Dictionary.ATTACHMENT_FILENAME
//                        + rfc5987_encode(teacher.getIpFilename()) + "\"")
//                .body(FileUtils.readFileToByteArray(someFile));
        return null;
    }

    @GetMapping("/teacher/downloadPsl")
    public ResponseEntity downloadPsl(@RequestParam("profName") String profName) throws IOException {
        Teacher teacher = teacherService.findByName(userService.getUserByUsername(profName).getName());
//        File someFile = new File(teacher.getPslFilename());
        return null;
//        return ResponseEntity.ok()
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .header(HttpHeaders.CONTENT_DISPOSITION, Dictionary.ATTACHMENT_FILENAME
//                        + rfc5987_encode(teacher.getPslFilename()) + "\"")
//                .body(FileUtils.readFileToByteArray(someFile));
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
