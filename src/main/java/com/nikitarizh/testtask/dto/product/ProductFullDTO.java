package com.nikitarizh.testtask.dto.product;

import com.nikitarizh.testtask.dto.tag.TagPreviewDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductFullDTO {
    private Integer id;

    private String description;

    private List<TagPreviewDTO> tags;
}
