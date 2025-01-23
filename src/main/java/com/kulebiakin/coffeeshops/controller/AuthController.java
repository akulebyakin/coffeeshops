package com.kulebiakin.coffeeshops.controller;

import com.kulebiakin.coffeeshops.entity.User;
import com.kulebiakin.coffeeshops.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("loginError", "Неверный логин или пароль!");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        // Check if email or login already exists
        if (userService.isEmailOrLoginExists(user.getEmail(), user.getLogin())) {
            model.addAttribute("registrationError", "Пользователь с таким email или login уже существует");
            return "register";
        }
        // Check if name is empty
        if (user.getName() == null || user.getName().isEmpty()) {
            model.addAttribute("error", "Имя не может быть пустым");
            return "register";
        }
        // Check if password contains at least 3 characters
        if (user.getPassword() == null || user.getPassword().length() < 3) {
            model.addAttribute("error", "Пароль должен содержать не менее 3 символов");
            return "register";
        }

        // Add user role by default
        user.setRole(User.Role.USER);
        userService.saveUser(user);

        // When registration is successful, redirect to the login page
        return "redirect:/login";
    }
}
