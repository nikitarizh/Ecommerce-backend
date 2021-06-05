package com.nikitarizh.testtask.dto.product;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class ProductCreateDTO {

    @NotBlank
    private String description;

    private List<Integer> tagIds;
}
