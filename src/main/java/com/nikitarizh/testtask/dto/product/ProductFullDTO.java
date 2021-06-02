package com.nikitarizh.testtask.dto.product;

import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.Tag;
import lombok.Data;

import java.util.List;

@Data
public class ProductFullDTO {
    private Integer id;

    private String description;

    private List<Tag> tags;

    public ProductFullDTO(Product product) {
        this.id = product.getId();
        this.description = product.getDescription();
        this.tags = product.getTags();
    }

    public ProductFullDTO() {
    }
}
