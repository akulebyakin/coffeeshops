package com.kulebiakin.coffeeshops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coffee_shops")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Название не может быть пустым")
    private String name;

    private String address;
    private String phone;
    private String email;
    private String website;
    private String description;
    private String image;

    @Min(0)
    @Max(50)
    private int rating;

    // Connection to the user who added the coffeeshop
    @ManyToOne
    @JoinColumn(name = "added_by", nullable = false)
    private User addedBy;

}

