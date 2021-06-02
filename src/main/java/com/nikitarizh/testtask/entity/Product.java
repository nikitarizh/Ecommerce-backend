package com.nikitarizh.testtask.entity;

import com.nikitarizh.testtask.dto.product.ProductCreateDTO;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
public class Product {
    @Id
    private Integer id;

    private String description;

    @ManyToMany
    private List<Tag> tags;

    @ManyToMany (mappedBy = "orderedProducts")
    private List<User> orderedBy;

    public Product(ProductCreateDTO productCreateDTO) {
        this.description = productCreateDTO.getDescription();
        this.tags = productCreateDTO.getTags();
    }

    public Product() {

    }
}