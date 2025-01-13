package com.kulebiakin.coffeeshops.controller;

import com.kulebiakin.coffeeshops.entity.User;
import com.kulebiakin.coffeeshops.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model,
                                @RequestParam(value = "error", required = false) String error) {
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
    public String registerUser(@ModelAttribute("user") @Valid User user,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        // Проверка уникальности login и email
        if (userService.isEmailOrLoginExists(user.getEmail(), user.getLogin())) {
            model.addAttribute("registrationError", "Пользователь с таким email или login уже существует");
            return "register";
        }

        // Присваиваем роль USER по умолчанию
        user.setRole(User.Role.USER);
        userService.saveUser(user);

        // При успешной регистрации перенаправляем на страницу логина
        return "redirect:/login";
    }
}
