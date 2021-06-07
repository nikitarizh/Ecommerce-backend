package com.nikitarizh.testtask.controller;

import com.nikitarizh.testtask.dto.tag.TagCreateDTO;
import com.nikitarizh.testtask.dto.tag.TagPreviewDTO;
import com.nikitarizh.testtask.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
@Api(description = "Controller to read, create, update and delete tags")
public class TagController {

    private final TagService tagService;

    @GetMapping
    @ApiOperation(value = "Get tags")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 404, message = "Tags not found")
    })
    public List<TagPreviewDTO> getAll() {
        return tagService.findAll();
    }

    @PostMapping
    @ApiOperation(value = "Create tag")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tag is created"),
            @ApiResponse(code = 400, message = "Bad DTO"),
            @ApiResponse(code = 403, message = "Access forbidden"),
            @ApiResponse(code = 401, message = "Unauthorized access"),
    })
    public TagPreviewDTO create(@Valid @RequestBody TagCreateDTO tagCreateDTO) {
        return tagService.create(tagCreateDTO);
    }

    @DeleteMapping("/{tagId}")
    @ApiOperation(value = "Delete tag")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tag is deleted"),
            @ApiResponse(code = 400, message = "Bad DTO or product that is in cart of at least one user has this tag"),
            @ApiResponse(code = 401, message = "Unauthorized access"),
            @ApiResponse(code = 403, message = "Access forbidden"),
            @ApiResponse(code = 404, message = "Tag with specified id doesn't exist")
    })
    public void delete(@PathVariable Integer tagId) {
        tagService.delete(tagId, false);
    }

    @DeleteMapping("force/{tagId}")
    @ApiOperation(value = "Force delete tag")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tag is deleted"),
            @ApiResponse(code = 400, message = "Bad DTO or product that is in cart of at least one user has this tag"),
            @ApiResponse(code = 401, message = "Unauthorized access"),
            @ApiResponse(code = 403, message = "Access forbidden"),
            @ApiResponse(code = 404, message = "Tag with specified id doesn't exist")
    })
    public void forceDelete(@PathVariable Integer tagId) {
        tagService.delete(tagId, true);
    }
}
