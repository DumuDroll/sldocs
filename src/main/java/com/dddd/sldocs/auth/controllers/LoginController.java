package com.dddd.sldocs.auth.controllers;

import com.dddd.sldocs.core.general.Dictionary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @GetMapping(path = "/login")
    public String viewLoginPage() {
        return Dictionary.LOGIN;
    }

    @PostMapping(path = "/login")
    public String login() {
        return Dictionary.LOGIN;
    }

    @GetMapping("/login_error")
    public String viewDefaultLoginErrorPage(Model model) {
        String error = "Щось пішло не так, спробуйте знов";
        model.addAttribute(Dictionary.ERROR, error);
        return Dictionary.LOGIN;
    }

    @GetMapping("/login_error_bad_credentials")
    public String viewBadCrLoginErrorPage(Model model) {
        String error = "Перевірте введені дані";
        model.addAttribute(Dictionary.ERROR, error);
        return Dictionary.LOGIN;
    }

    @GetMapping("/login_error_disabled")
    public String viewDisLoginErrorPage(Model model) {
        String error = "Акаунт не активовано, зверніться до адміністратора";
        model.addAttribute(Dictionary.ERROR, error);
        return Dictionary.LOGIN;
    }

}
