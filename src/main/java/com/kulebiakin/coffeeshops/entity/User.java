package com.kulebiakin.coffeeshops.entity;

import com.kulebiakin.coffeeshops.util.validation.NotEmptyAndNotBlank;
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

    @Lob
    @Basic(fetch = FetchType.EAGER) // Fetch the avatar eagerly
    private byte[] avatar;

    @Transient // Not stored in the database
    private String avatarBase64;

    @NotEmptyAndNotBlank(
            emptyMessage = "Имя не может быть пустым",
            blankMessage = "Имя не может содержать только пробелы"
    )
    private String name;

    @Email(message = "Неверный формат email")
    @NotEmpty(message = "Email не может быть пустым")
    @Column(unique = true)
    private String email;

    @NotEmptyAndNotBlank(
            emptyMessage = "Логин не может быть пустым",
            blankMessage = "Логин не может содержать только пробелы"
    )
    @Column(unique = true)
    private String login;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        USER,
        ADMIN,
        MANAGER
    }
}

