package com.kulebiakin.coffeeshops.service;import com.kulebiakin.coffeeshops.entity.User;import com.kulebiakin.coffeeshops.repository.UserRepository;import jakarta.validation.Valid;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import org.springframework.web.multipart.MultipartFile;import java.io.IOException;import java.util.Base64;import java.util.List;@Service@Transactionalpublic class UserService {    private final UserRepository userRepository;    private final PasswordEncoder passwordEncoder;    @Autowired    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {        this.userRepository = userRepository;        this.passwordEncoder = passwordEncoder;    }    public User findByLogin(String login) {        return userRepository.findByLogin(login).orElseThrow(() ->                new RuntimeException("User not found with login: " + login));    }    public List<User> findAll() {        return userRepository.findAll();    }    public User saveUser(User user) {        user.setPassword(encodePassword(user.getPassword()));        return userRepository.save(user);    }    // Check if email or login already exists    public boolean isEmailOrLoginExists(String email, String login) {        return userRepository.existsByEmail(email) || userRepository.existsByLogin(login);    }    public User findById(Long id) {        return userRepository.findById(id).orElse(null);    }    public void deleteById(Long id) {        userRepository.deleteById(id);    }    public void updateUser(@Valid User user) {        User existingUser = userRepository.findById(user.getId()).orElseThrow(() ->                new RuntimeException("User not found with id: " + user.getId()));        mapToExistingUser(user, existingUser);        userRepository.save(existingUser);    }    private void mapToExistingUser(@Valid User user, User existingUser) {        if (user.getPassword() != null && !user.getPassword().isEmpty()) {            existingUser.setPassword(encodePassword(user.getPassword()));        }        if (user.getAvatar() != null) {            existingUser.setAvatar(user.getAvatar());        }        existingUser.setName(user.getName());        existingUser.setLogin(user.getLogin());        existingUser.setEmail(user.getEmail());        existingUser.setRole(user.getRole());    }    public boolean isEmailOrLoginExistsExcludingUser(String email, String login, Long userId) {        return userRepository.existsByEmailAndIdNot(email, userId) ||                userRepository.existsByLoginAndIdNot(login, userId);    }    private String encodePassword(String password) {        return passwordEncoder.encode(password);    }    public void deleteAvatar(Long userId) {        User user = userRepository.findById(userId).orElseThrow(() ->                new RuntimeException("User not found with id: " + userId));        user.setAvatar(null);        userRepository.save(user);    }    public void updateAvatar(User user, MultipartFile avatarFile) throws IOException {        if (avatarFile != null && !avatarFile.isEmpty()) {            user.setAvatar(avatarFile.getBytes());        }    }    public String getAvatarBase64(User user) {        if (user.getAvatar() == null) {            return null;        }        return "data:image/png;base64," + Base64.getEncoder().encodeToString(user.getAvatar());    }}