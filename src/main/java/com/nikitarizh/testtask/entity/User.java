package com.nikitarizh.testtask.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    private Integer id;

    private UserType userType;

    private String nickname;

    private String password;

    private String email;

    @ManyToMany
    private List<Product> orderedProducts;
}