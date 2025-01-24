package com.kulebiakin.coffeeshops.service;

import com.kulebiakin.coffeeshops.entity.User;
import com.kulebiakin.coffeeshops.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByLogin_UserExists_ReturnsUser() {
        User user = new User();
        user.setLogin("testuser");
        when(userRepository.findByLogin("testuser")).thenReturn(Optional.of(user));

        User result = userService.findByLogin("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getLogin());
    }

    @Test
    void findByLogin_UserDoesNotExist_ThrowsRuntimeException() {
        when(userRepository.findByLogin("nonexistentuser")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findByLogin("nonexistentuser"));
    }

    @Test
    void saveUser_EncodesPasswordAndSavesUser() {
        User user = new User();
        user.setPassword("plainpassword");
        when(passwordEncoder.encode("plainpassword")).thenReturn("encodedpassword");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);

        assertNotNull(result);
        assertEquals("encodedpassword", result.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void isEmailOrLoginExists_EmailOrLoginExists_ReturnsTrue() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        when(userRepository.existsByLogin("testuser")).thenReturn(false);

        boolean result = userService.isEmailOrLoginExists("test@example.com", "testuser");

        assertTrue(result);
    }

    @Test
    void isEmailOrLoginExists_EmailOrLoginDoesNotExist_ReturnsFalse() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByLogin("testuser")).thenReturn(false);

        boolean result = userService.isEmailOrLoginExists("test@example.com", "testuser");

        assertFalse(result);
    }

    @Test
    void findById_UserExists_ReturnsUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findById_UserDoesNotExist_ReturnsNull() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.findById(1L);

        assertNull(result);
    }

    @Test
    void deleteById_DeletesUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateUser_UserExists_UpdatesUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setPassword("oldpassword");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedpassword");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setPassword("newpassword");
        updatedUser.setName("newname");
        updatedUser.setLogin("newlogin");
        updatedUser.setEmail("newemail");
        updatedUser.setRole(User.Role.USER);

        userService.updateUser(updatedUser);

        assertEquals("newname", existingUser.getName());
        assertEquals("newlogin", existingUser.getLogin());
        assertEquals("newemail", existingUser.getEmail());
        assertEquals(User.Role.USER, existingUser.getRole());
        assertEquals("encodedpassword", existingUser.getPassword());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_UserDoesNotExist_ThrowsRuntimeException() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(user));
    }

    @Test
    void isEmailOrLoginExistsExcludingUser_EmailOrLoginExists_ReturnsTrue() {
        when(userRepository.existsByEmailAndIdNot("test@example.com", 1L)).thenReturn(true);
        when(userRepository.existsByLoginAndIdNot("testuser", 1L)).thenReturn(false);

        boolean result = userService.isEmailOrLoginExistsExcludingUser("test@example.com", "testuser", 1L);

        assertTrue(result);
    }

    @Test
    void isEmailOrLoginExistsExcludingUser_EmailOrLoginDoesNotExist_ReturnsFalse() {
        when(userRepository.existsByEmailAndIdNot("test@example.com", 1L)).thenReturn(false);
        when(userRepository.existsByLoginAndIdNot("testuser", 1L)).thenReturn(false);

        boolean result = userService.isEmailOrLoginExistsExcludingUser("test@example.com", "testuser", 1L);

        assertFalse(result);
    }
}