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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {
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
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void showLoginPageWithError() {
        String error = "error";
        when(model.addAttribute("loginError", "Неверный логин или пароль!")).thenReturn(model);

        String viewName = authController.showLoginPage(model, error);

        assertEquals("login", viewName);
    }

    @Test
    void showLoginPageWithoutError() {
        String viewName = authController.showLoginPage(model, null);

        assertEquals("login", viewName);
    }

    @Test
    void showRegistrationForm() {
        when(model.addAttribute(eq("user"), any(User.class))).thenReturn(model);

        String viewName = authController.showRegistrationForm(model);

        assertEquals("register", viewName);
    }

    @Test
    void registerUserWithExistingEmailOrLogin() {
        User user = new User();
        when(userService.isEmailOrLoginExists(user.getEmail(), user.getLogin())).thenReturn(true);

        String viewName = authController.registerUser(user, bindingResult, avatarFile, model, redirectAttributes);

        assertEquals("register", viewName);
    }

    @Test
    void registerUserWithInvalidPassword() {
        User user = new User();
        user.setPassword(INVALID_PASSWORD);
        when(userService.isEmailOrLoginExists(user.getEmail(), user.getLogin())).thenReturn(false);

        String viewName = authController.registerUser(user, bindingResult, avatarFile, model, redirectAttributes);

        assertEquals("register", viewName);
    }

    @Test
    void registerUserWithAvatarUploadError() throws IOException {
        User user = new User();
        user.setPassword(VALID_PASSWORD);
        when(userService.isEmailOrLoginExists(user.getEmail(), user.getLogin())).thenReturn(false);
        when(avatarFile.isEmpty()).thenReturn(false);
        doThrow(IOException.class).when(avatarFile).getBytes();

        String viewName = authController.registerUser(user, bindingResult, avatarFile, model, redirectAttributes);

        assertEquals("register", viewName);
    }

    @Test
    void registerUserWithBindingErrors() {
        User user = new User();
        user.setPassword(VALID_PASSWORD);
        when(userService.isEmailOrLoginExists(user.getEmail(), user.getLogin())).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = authController.registerUser(user, bindingResult, avatarFile, model, redirectAttributes);

        assertEquals("register", viewName);
    }

    @Test
    void registerUserSuccessfully() {
        User user = new User();
        user.setPassword(VALID_PASSWORD);
        when(userService.isEmailOrLoginExists(user.getEmail(), user.getLogin())).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = authController.registerUser(user, bindingResult, avatarFile, model, redirectAttributes);

        assertEquals("redirect:/login", viewName);
    }
}
