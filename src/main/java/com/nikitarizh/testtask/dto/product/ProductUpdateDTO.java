package com.nikitarizh.testtask.dto.product;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class ProductUpdateDTO {

    @Min(1)
    private Integer id;

    @NotBlank
    private String description;

    private List<Integer> tagIds;
}
