package com.nikitarizh.testtask.dto.tag;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TagPreviewDTO {

    @ApiModelProperty(example = "2")
    private Integer id;

    @ApiModelProperty(example = "tag 1")
    private String value;
}
