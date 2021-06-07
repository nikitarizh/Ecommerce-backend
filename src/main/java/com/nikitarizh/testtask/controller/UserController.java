package com.nikitarizh.testtask.controller;

import com.nikitarizh.testtask.dto.user.UserCreateDTO;
import com.nikitarizh.testtask.dto.user.UserFullDTO;
import com.nikitarizh.testtask.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping
    @ApiOperation(value = "List all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 401, message = "Unauthorized access"),
            @ApiResponse(code = 403, message = "Access forbidden"),
    })
    public List<UserFullDTO> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    @ApiOperation(value = "Find user by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 401, message = "Unauthorized access"),
            @ApiResponse(code = 403, message = "Access forbidden"),
    })
    public UserFullDTO getById(@PathVariable Integer userId) {
        return userService.findById(userId);
    }

    @PutMapping("/admins")
    @ApiOperation(value = "Authorize user to be an admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User is authorized to be admin"),
            @ApiResponse(code = 401, message = "Unauthorized access"),
            @ApiResponse(code = 404, message = "User with specified id doesn't exist")
    })
    public UserFullDTO addAdmin(@RequestBody Integer userId) {
        return userService.addAdmin(userId);
    }
}
