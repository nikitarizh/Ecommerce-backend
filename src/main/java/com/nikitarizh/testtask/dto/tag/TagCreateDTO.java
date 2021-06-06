package com.nikitarizh.testtask.dto.tag;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TagCreateDTO {

    @NotBlank
    @ApiModelProperty(example = "tag 2")
    private String value;
}
