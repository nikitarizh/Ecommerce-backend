package com.nikitarizh.testtask.dto.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class ProductUpdateDTO {

    @Min(1)
    @ApiModelProperty(example = "1")
    private Integer id;

    @NotBlank
    @ApiModelProperty(example = "new description")
    private String description;

    @ApiModelProperty(example = "2")
    private List<Integer> tagIds;
}
