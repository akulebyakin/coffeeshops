package com.kulebiakin.coffeeshops.controller;

import com.kulebiakin.coffeeshops.entity.User;
import com.kulebiakin.coffeeshops.service.UserService;
import com.kulebiakin.coffeeshops.util.validation.PasswordValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/users")
@Slf4j
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
        users.forEach(user -> user.setAvatarBase64(userService.getAvatarBase64(user)));
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
        user.setAvatarBase64(userService.getAvatarBase64(user));
        model.addAttribute("user", user);
        return "user-edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult,
                             @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        log.info("Updating user with id: {}", user.getId());
        // Get existing user by id
        User existingUser = userService.findById(user.getId());
        if (existingUser == null) {
            log.debug("User not found");
            bindingResult.rejectValue("id", "error.user", "Пользователь не найден");
            return "user-edit";
        }

        // Check if email or login already exists
        if (userService.isEmailOrLoginExistsExcludingUser(user.getEmail(), user.getLogin(), user.getId())) {
            log.debug("User with email or login already exists");
            model.addAttribute("userEditingError", "Пользователь с таким email или login уже существует");
            return "user-edit";
        }

        // If password is entered check if password contains at least 3 characters
        if (user.getPassword() != null && !user.getPassword().isEmpty() &&
                !PasswordValidator.isValid(user.getPassword())) {
            log.debug("Password is not valid");
            bindingResult.rejectValue("password", "error.user", "Пароль должен содержать не менее 3 символов");
            return "user-edit";
        }

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                userService.updateAvatar(user, avatarFile);
            } catch (IOException e) {
                log.error("Error loading avatar", e);
                model.addAttribute("error", "Ошибка загрузки аватара");
                return "user-edit";
            }
        }

        if (bindingResult.hasErrors()) {
            return "user-edit";
        }

        userService.updateUser(user);
        log.info("User updated. ID: {}", user.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно изменен. ID = " + user.getId());
        return "redirect:/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Deleting user with id: {}", id);
        User existingUser = userService.findById(id);
        if (existingUser == null) {
            redirectAttributes.addFlashAttribute("error", "Пользователь не найден. ID = " + id);
            return "redirect:/users";
        }
        userService.deleteById(id);
        log.info("User deleted. ID = {}", id);
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно удалён. ID = " + id);
        return "redirect:/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/avatar/delete/{id}")
    public String deleteAvatar(@PathVariable Long id) {
        log.debug("Deleting avatar for user with id: {}", id);
        userService.deleteAvatar(id);
        log.debug("Avatar deleted for user with id: {}", id);
        return "redirect:/users/edit/" + id;
    }

}

