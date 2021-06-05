package com.nikitarizh.testtask.dto.tag;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class TagUpdateDTO {

    @Min(1)
    private Integer id;

    @NotBlank
    private String value;
}
