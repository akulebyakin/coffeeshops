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

    @NotEmpty(message = "Имя не может быть пустым")
    private String name;

    @Email(message = "Неверный формат email")
    @NotEmpty(message = "Email не может быть пустым")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Логин не может быть пустым")
    @Column(unique = true)
    private String login;

    @NotEmpty(message = "Пароль не может быть пустым")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        USER,
        ADMIN
    }
}

