package com.kulebiakin.coffeeshops.controller;

import com.kulebiakin.coffeeshops.entity.User;
import com.kulebiakin.coffeeshops.service.UserService;
import com.kulebiakin.coffeeshops.util.validation.PasswordValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // List of all users (only for ADMIN)
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    // Edit user by id (can be used by both ADMIN and USER)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        return "user-edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult,
                             Model model) {
        // Get existing user by id
        User existingUser = userService.findById(user.getId());
        if (existingUser == null) {
            bindingResult.rejectValue("id", "error.user", "Пользователь не найден");
            return "user-edit";
        }

        // Check if email or login already exists
        if (userService.isEmailOrLoginExistsExcludingUser(user.getEmail(), user.getLogin(), user.getId())) {
            model.addAttribute("userEditingError", "Пользователь с таким email или login уже существует");
            return "user-edit";
        }

        // If password is entered check if password contains at least 3 characters
        if (user.getPassword() != null && !user.getPassword().isEmpty() &&
                !PasswordValidator.isValid(user.getPassword())) {
            bindingResult.rejectValue("password", "error.user", "Пароль должен содержать не менее 3 символов");
            return "user-edit";
        }

        if (bindingResult.hasErrors()) {
            return "user-edit";
        }

        userService.updateUser(user);
        return "redirect:/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

}

