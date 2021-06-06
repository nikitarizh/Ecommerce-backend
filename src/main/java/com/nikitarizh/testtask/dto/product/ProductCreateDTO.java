package com.nikitarizh.testtask.dto.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class ProductCreateDTO {

    @NotBlank(message = "description can't be blank")
    @ApiModelProperty(example = "description of product 1")
    private String description;

    @ApiModelProperty(example = "2")
    private List<Integer> tagIds;
}
