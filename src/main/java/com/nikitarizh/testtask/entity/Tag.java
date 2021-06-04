package com.nikitarizh.testtask.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "value", nullable = false)
    @NotBlank
    private String value;

    @ManyToMany (mappedBy = "tags")
    @ToString.Exclude
    private List<Product> products;
}
