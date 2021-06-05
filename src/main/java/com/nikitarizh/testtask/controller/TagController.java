package com.nikitarizh.testtask.controller;

import com.nikitarizh.testtask.dto.tag.TagCreateDTO;
import com.nikitarizh.testtask.dto.tag.TagPreviewDTO;
import com.nikitarizh.testtask.dto.tag.TagUpdateDTO;
import com.nikitarizh.testtask.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public List<TagPreviewDTO> getAll() {
        return tagService.findAll();
    }

    @PostMapping
    public TagPreviewDTO create(@Valid @RequestBody TagCreateDTO tagCreateDTO) {
        return tagService.create(tagCreateDTO);
    }

    @PutMapping
    public TagPreviewDTO update(@Valid @RequestBody TagUpdateDTO tagUpdateDTO) {
        return tagService.update(tagUpdateDTO);
    }

    @DeleteMapping("/{tagId}")
    public void delete(@PathVariable Integer tagId) {
        tagService.delete(tagId);
    }
}
