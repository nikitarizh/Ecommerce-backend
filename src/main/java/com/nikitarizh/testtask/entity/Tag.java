package com.nikitarizh.testtask.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
public class Tag {

    @Id
    private Long id;

    private String value;

    @ManyToMany (mappedBy = "tags")
    private List<Product> products;
}
