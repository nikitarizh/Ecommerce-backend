package com.nikitarizh.testtask.dto.user;

import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserFullDTO {
    private Integer id;

    private String nickname;

    private String email;

    private List<ProductFullDTO> products;
}