package com.nikitarizh.testtask.dto.user;

import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.entity.UserRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserFullDTO {

    @ApiModelProperty(example = "3")
    private Integer id;

    @ApiModelProperty(example = "UserRole.ROLE_USER")
    private UserRole role;

    @ApiModelProperty(example = "bob")
    private String nickname;

    @ApiModelProperty(example = "bob@example.com")
    private String email;

    @ApiModelProperty(example = "1")
    private List<ProductFullDTO> products;
}