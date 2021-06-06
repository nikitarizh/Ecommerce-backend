package com.nikitarizh.testtask.dto.product;

import com.nikitarizh.testtask.dto.tag.TagPreviewDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProductFullDTO {

    @ApiModelProperty(example = "1")
    private Integer id;

    @ApiModelProperty(example = "description of product 1")
    private String description;

    @ApiModelProperty(example = "2")
    private List<TagPreviewDTO> tags;
}
