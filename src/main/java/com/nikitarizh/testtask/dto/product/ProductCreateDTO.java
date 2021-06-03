package com.nikitarizh.testtask.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class ProductCreateDTO {
    private String description;

    private List<Integer> tagIds;
}
