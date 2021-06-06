package com.nikitarizh.testtask.dto.tag;

import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TagFullDTO {

    @ApiModelProperty(example = "2")
    private Integer id;

    @ApiModelProperty(example = "tag 1")
    private String value;

    @ApiModelProperty(example = "1")
    private List<ProductFullDTO> products;
}
