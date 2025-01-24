package com.kulebiakin.coffeeshops.config;

import com.kulebiakin.coffeeshops.entity.User;
import com.kulebiakin.coffeeshops.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword("password");
        user.setRole(User.Role.USER);

        when(userRepository.findByLogin("testuser")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        when(userRepository.findByLogin("nonexistentuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("nonexistentuser"));
    }

    @Test
    void loadUserByUsername_UserExistsWithAdminRole_ReturnsUserDetailsWithAdminRole() {
        User user = new User();
        user.setLogin("adminuser");
        user.setPassword("adminpassword");
        user.setRole(User.Role.ADMIN);

        when(userRepository.findByLogin("adminuser")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("adminuser");

        assertNotNull(userDetails);
        assertEquals("adminuser", userDetails.getUsername());
        assertEquals("adminpassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_UserExistsWithNullRole_ThrowsUsernameNotFoundException() {
        User user = new User();
        user.setLogin("nullroleuser");
        user.setPassword("password");
        user.setRole(null);

        when(userRepository.findByLogin("nullroleuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("nullroleuser"));
    }
}