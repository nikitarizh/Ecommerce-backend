package com.nikitarizh.testtask.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private UserType userType;

    private String nickname;

    private String password;

    private String email;

    @ManyToMany
    @ToString.Exclude
    private List<Product> orderedProducts;
}