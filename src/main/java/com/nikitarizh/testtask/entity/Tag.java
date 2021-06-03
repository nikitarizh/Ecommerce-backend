package com.nikitarizh.testtask.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "value", nullable = false)
    private String value;

    @ManyToMany (mappedBy = "tags")
    @ToString.Exclude
    private List<Product> products;
}
