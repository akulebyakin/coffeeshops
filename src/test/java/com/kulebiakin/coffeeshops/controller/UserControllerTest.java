package com.kulebiakin.coffeeshops.controller;

import com.kulebiakin.coffeeshops.entity.User;
import com.kulebiakin.coffeeshops.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private static final String VALID_PASSWORD = "123";
    private static final String INVALID_PASSWORD = "12";

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MultipartFile avatarFile;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listUsersSuccessfully() {
        List<User> users = Collections.singletonList(new User());
        when(userService.findAll()).thenReturn(users);

        String viewName = userController.listUsers(model);

        assertEquals("users", viewName);
        verify(model).addAttribute("users", users);
    }

    @Test
    void editUserWithValidId() {
        User user = new User();
        when(userService.findById(1L)).thenReturn(user);

        String viewName = userController.editUser(1L, model);

        assertEquals("user-edit", viewName);
        verify(model).addAttribute("user", user);
    }

    @Test
    void editUserWithInvalidId() {
        when(userService.findById(1L)).thenReturn(null);

        String viewName = userController.editUser(1L, model);

        assertEquals("redirect:/users", viewName);
    }

    @Test
    void updateUserWithExistingEmailOrLogin() {
        User user = new User();
        when(userService.findById(user.getId())).thenReturn(user);
        when(userService.isEmailOrLoginExistsExcludingUser(user.getEmail(), user.getLogin(), user.getId())).thenReturn(true);

        String viewName = userController.updateUser(user, bindingResult, avatarFile, model, redirectAttributes);

        assertEquals("user-edit", viewName);
    }

    @Test
    void updateUserWithInvalidPassword() {
        User user = new User();
        user.setPassword(INVALID_PASSWORD);
        when(userService.findById(user.getId())).thenReturn(user);
        when(userService.isEmailOrLoginExistsExcludingUser(user.getEmail(), user.getLogin(), user.getId())).thenReturn(false);

        String viewName = userController.updateUser(user, bindingResult, avatarFile, model, redirectAttributes);

        assertEquals("user-edit", viewName);
    }

    @Test
    void updateUserWithAvatarUploadError() throws IOException {
        User user = new User();
        user.setPassword(VALID_PASSWORD);
        when(userService.findById(user.getId())).thenReturn(user);
        when(userService.isEmailOrLoginExistsExcludingUser(user.getEmail(), user.getLogin(), user.getId())).thenReturn(false);
        when(avatarFile.isEmpty()).thenReturn(false);
        doThrow(IOException.class).when(userService).updateAvatar(user, avatarFile);

        String viewName = userController.updateUser(user, bindingResult, avatarFile, model, redirectAttributes);

        assertEquals("user-edit", viewName);
    }

    @Test
    void updateUserWithBindingErrors() {
        User user = new User();
        user.setPassword(VALID_PASSWORD);
        when(userService.findById(user.getId())).thenReturn(user);
        when(userService.isEmailOrLoginExistsExcludingUser(user.getEmail(), user.getLogin(), user.getId())).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = userController.updateUser(user, bindingResult, avatarFile, model, redirectAttributes);

        assertEquals("user-edit", viewName);
    }

    @Test
    void updateUserSuccessfully() {
        User user = new User();
        user.setPassword(VALID_PASSWORD);
        when(userService.findById(user.getId())).thenReturn(user);
        when(userService.isEmailOrLoginExistsExcludingUser(user.getEmail(), user.getLogin(), user.getId())).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = userController.updateUser(user, bindingResult, avatarFile, model, redirectAttributes);

        assertEquals("redirect:/users", viewName);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Пользователь успешно изменен. ID = " + user.getId());
    }

    @Test
    void deleteUserWithValidId() {
        User user = new User();
        when(userService.findById(1L)).thenReturn(user);

        String viewName = userController.deleteUser(1L, redirectAttributes);

        assertEquals("redirect:/users", viewName);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Пользователь успешно удалён. ID = 1");
    }

    @Test
    void deleteUserWithInvalidId() {
        when(userService.findById(1L)).thenReturn(null);

        String viewName = userController.deleteUser(1L, redirectAttributes);

        assertEquals("redirect:/users", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Пользователь не найден. ID = 1");
    }

    @Test
    void deleteAvatarSuccessfully() {
        String viewName = userController.deleteAvatar(1L);

        assertEquals("redirect:/users/edit/1", viewName);
        verify(userService).deleteAvatar(1L);
    }
}
