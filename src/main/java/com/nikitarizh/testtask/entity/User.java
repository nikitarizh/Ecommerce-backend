package com.nikitarizh.testtask.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type = UserType.ROLE_USER;

    @Column(name = "nickname", nullable = false, unique = true)
    @NotBlank
    private String nickname;

    @Column(name = "password", nullable = false)
    @NotBlank
    private String password;

    @Column(name = "email", nullable = false)
    @NotBlank
    private String email;

    @ManyToMany
    @ToString.Exclude
    @JoinTable(
            name = "user_product",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")}
    )
    private List<Product> orderedProducts;
}