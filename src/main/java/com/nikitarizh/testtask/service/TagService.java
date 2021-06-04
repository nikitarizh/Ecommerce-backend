package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.dto.tag.TagCreateDTO;
import com.nikitarizh.testtask.dto.tag.TagFullDTO;
import com.nikitarizh.testtask.dto.tag.TagPreviewDTO;
import com.nikitarizh.testtask.dto.tag.TagUpdateDTO;
import com.nikitarizh.testtask.entity.Tag;

import java.util.List;

public interface TagService {

    TagFullDTO findById(Integer id);

    List<Tag> findAllByIds(List<Integer> ids);

    List<TagPreviewDTO> findAll();

    TagPreviewDTO create(TagCreateDTO tagCreateDTO);

    TagPreviewDTO update(TagUpdateDTO tagUpdateDTO);

    void delete(Integer id);
}
