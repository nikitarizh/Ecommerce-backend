package com.nikitarizh.testtask.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String description;

    @ManyToMany
    private List<Tag> tags;

    @ManyToMany (mappedBy = "orderedProducts")
    private List<User> orderedBy;
}