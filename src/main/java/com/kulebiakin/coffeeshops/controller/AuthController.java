package com.kulebiakin.coffeeshops.controller;

import com.kulebiakin.coffeeshops.entity.User;
import com.kulebiakin.coffeeshops.service.UserService;
import com.kulebiakin.coffeeshops.util.validation.PasswordValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
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
    public String registerUser(@ModelAttribute("user") @Valid User user,
                               BindingResult bindingResult,
                               @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        // Check if email or login already exists
        if (userService.isEmailOrLoginExists(user.getEmail(), user.getLogin())) {
            model.addAttribute("registrationError", "Пользователь с таким email или login уже существует");
            return "register";
        }

        // Check if password contains at least 3 characters
        if (!PasswordValidator.isValid(user.getPassword())) {
            bindingResult.rejectValue("password", "error.user", "Пароль должен содержать не менее 3 символов");
            return "register";
        }

        try {
            if (avatarFile != null && !avatarFile.isEmpty()) {
                user.setAvatar(avatarFile.getBytes());
            }
        } catch (IOException e) {
            model.addAttribute("registrationError", "Ошибка загрузки аватара");
            return "register";
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        // Add user role by default
        user.setRole(User.Role.USER);
        userService.saveUser(user);

        // When registration is successful, redirect to the login page
        redirectAttributes.addFlashAttribute("successMessage",
                "Пользователь успешно зарегистрирован. LOGIN = " + user.getLogin() + ", ID = " + user.getId());
        return "redirect:/login";
    }
}
