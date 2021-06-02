package com.nikitarizh.testtask.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Product {

    @Id
    private Integer id;

    private String description;

    @ManyToMany
    private List<Tag> tags;

    @ManyToMany
    private List<User> orderedBy;
}