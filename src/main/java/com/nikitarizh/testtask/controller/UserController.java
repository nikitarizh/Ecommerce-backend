package com.nikitarizh.testtask.controller;

import com.nikitarizh.testtask.dto.user.UserCreateDTO;
import com.nikitarizh.testtask.dto.user.UserFullDTO;
import com.nikitarizh.testtask.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Api(description = "Controller to manage users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ApiOperation(value = "Register user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User is created"),
            @ApiResponse(code = 400, message = "Bad DTO")
    })
    public UserFullDTO register(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        return userService.create(userCreateDTO);
    }
}
