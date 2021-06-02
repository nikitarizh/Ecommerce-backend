package com.nikitarizh.testtask.dto.tag;

import com.nikitarizh.testtask.entity.Product;
import lombok.Data;

import java.util.List;

@Data
public class ProductUpdateDTO {
    private Integer id;

    private String value;
}
