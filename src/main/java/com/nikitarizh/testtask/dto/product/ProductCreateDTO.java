package com.nikitarizh.testtask.dto.product;

import com.nikitarizh.testtask.entity.Tag;
import lombok.Data;

import java.util.List;

@Data
public class ProductCreateDTO {
    private String description;

    private List<Tag> tags;
}
