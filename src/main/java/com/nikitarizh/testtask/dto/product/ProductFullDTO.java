package com.nikitarizh.testtask.dto.product;

import com.nikitarizh.testtask.dto.tag.TagPreviewDTO;
import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.Tag;
import lombok.Data;

import java.util.List;

@Data
public class ProductFullDTO {
    private Integer id;

    private String description;

    private List<TagPreviewDTO> tags;
}
