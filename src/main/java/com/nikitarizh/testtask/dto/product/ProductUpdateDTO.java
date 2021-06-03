package com.nikitarizh.testtask.dto.product;

import com.nikitarizh.testtask.entity.Tag;
import lombok.Data;

import java.util.List;

@Data
public class ProductUpdateDTO {
    private Integer id;

    private String description;

    private List<Integer> tagIds;
}
