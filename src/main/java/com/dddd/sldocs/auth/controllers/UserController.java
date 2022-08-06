package com.dddd.sldocs.auth.controllers;

import com.dddd.sldocs.auth.entities.User;
import com.dddd.sldocs.auth.user_details.UserService;
import com.dddd.sldocs.core.general.Dictionary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/users")
    public String viewUserPage(Model model) {
        model.addAttribute("users", userService.listAll());
        return "users";
    }

    @PostMapping(path = "/user/activate")
    public String enableUser(@RequestParam("username") String username) {
        User user = userService.getUserByUsername(username);
        user.setEnabled(true);
        userService.save(user);
        return Dictionary.REDIRECT_TO_USERS_PAGE;
    }

    @PostMapping(path = "/user/deactivate")
    public String disableUser(@RequestParam("username") String username) {
        User user = userService.getUserByUsername(username);
        user.setEnabled(false);
        userService.save(user);
        return Dictionary.REDIRECT_TO_USERS_PAGE;
    }

    @PostMapping(path = "/user/activateAll")
    public String enableAllUsers(@RequestParam("username") String username) {
        List<User> users = userService.listAll();
        for (User user : users) {
            if (!user.getUsername().equals(username)) {
                user.setEnabled(true);
                userService.save(user);
            }
        }

        return Dictionary.REDIRECT_TO_USERS_PAGE;
    }

    @PostMapping(path = "/user/deactivateAll")
    public String disableAllUsers(@RequestParam("username") String username) {
        List<User> users = userService.listAll();
        for (User user : users) {
            if (!user.getUsername().equals(username)) {
                user.setEnabled(false);
                userService.save(user);
            }
        }
        return Dictionary.REDIRECT_TO_USERS_PAGE;
    }
}
