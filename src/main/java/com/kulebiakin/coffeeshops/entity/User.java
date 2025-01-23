package com.kulebiakin.coffeeshops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Email(message = "Wrong email format")
    @NotEmpty(message = "Email cannot be empty")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "login cannot be empty")
    @Column(unique = true)
    private String login;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        USER,
        ADMIN,
        MANAGER
    }
}

