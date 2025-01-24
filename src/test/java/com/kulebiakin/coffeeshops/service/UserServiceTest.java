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
}