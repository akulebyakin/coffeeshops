package com.kulebiakin.coffeeshops.repository;

import com.kulebiakin.coffeeshops.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
}

