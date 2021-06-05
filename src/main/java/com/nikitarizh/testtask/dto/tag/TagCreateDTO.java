package com.nikitarizh.testtask.dto.tag;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TagCreateDTO {

    @NotBlank
    private String value;
}
