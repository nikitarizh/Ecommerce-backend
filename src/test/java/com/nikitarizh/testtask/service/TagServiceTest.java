package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.AbstractTest;
import com.nikitarizh.testtask.dto.tag.TagCreateDTO;
import com.nikitarizh.testtask.dto.tag.TagFullDTO;
import com.nikitarizh.testtask.dto.tag.TagPreviewDTO;
import com.nikitarizh.testtask.entity.Tag;
import com.nikitarizh.testtask.exception.TagNotFoundException;
import com.nikitarizh.testtask.utils.DataGenerator;
import com.nikitarizh.testtask.utils.DataManipulator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.nikitarizh.testtask.mapper.TagMapper.TAG_MAPPER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TagServiceTest extends AbstractTest {

    private final TagService tagService;

    @Autowired
    public TagServiceTest(DataManipulator dataManipulator, TagService tagService) {
        super(dataManipulator);

        this.tagService = tagService;
    }

    @Test
    @Transactional
    public void findAll_happyPath() {
        // GIVEN
        List<Tag> generatedTags = dataManipulator.saveTags(DataGenerator.generateValidTagsList());
        List<TagPreviewDTO> generatedDTOs = generatedTags.stream()
                .map(TAG_MAPPER::mapToPreviewDTO)
                .collect(Collectors.toList());

        // WHEN
        List<TagPreviewDTO> foundDTOs = tagService.findAll();

        // THEN
        assertEquals(generatedDTOs, foundDTOs);
    }

    @Test
    @Transactional
    public void findAllByIds_happyPath() {
        // GIVEN
        List<Tag> generatedTags = dataManipulator.saveTags(DataGenerator.generateValidTagsList());

        // some other tags
        dataManipulator.saveTags(DataGenerator.generateValidTagsList());

        List<Integer> generatedIds = generatedTags.stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        // WHEN
        List<Tag> foundTags = tagService.findAllByIds(generatedIds);

        // THEN
        assertEquals(generatedTags, foundTags);
    }

    @Test
    @Transactional
    public void findAllByIds_notExists() {
        // GIVEN
        List<Tag> generatedTags = dataManipulator.saveTags(DataGenerator.generateValidTagsList());
        List<Integer> generatedIds = generatedTags.stream()
                .map((tag) -> tag.getId() + 1)
                .collect(Collectors.toList());

        // THEN
        assertThrows(TagNotFoundException.class, () -> tagService.findAllByIds(generatedIds));
    }

    @Test
    @Transactional
    public void findById_happyPath() {
        // GIVEN
        Tag generatedTag = dataManipulator.saveTag(DataGenerator.generateValidTag());
        TagFullDTO generatedFullDTO = TAG_MAPPER.mapToFullDTO(generatedTag);

        // WHEN
        TagFullDTO foundTagDTO = tagService.findById(generatedTag.getId());

        // THEN
        assertEquals(generatedFullDTO, foundTagDTO);
    }

    @Test
    public void findById_notExists() {
        // GIVEN
        Tag generatedTag = dataManipulator.saveTag(DataGenerator.generateValidTag());

        // THEN
        assertThrows(TagNotFoundException.class, () -> tagService.findById(generatedTag.getId() + 1));
    }

    @Test
    public void create_happyPath() {
        // GIVEN
        TagCreateDTO tagCreateDTO = DataGenerator.generateValidTagCreateDTO();

        // WHEN
        TagPreviewDTO createdDTO = tagService.create(tagCreateDTO);

        // THEN
        assertEquals(tagCreateDTO.getValue(), createdDTO.getValue());
    }

    @Test
    public void delete_happyPath() {
        // GIVEN
        Tag generatedTag = dataManipulator.saveTag(DataGenerator.generateValidTag());

        // WHEN
        tagService.delete(generatedTag.getId(), false);

        // THEN
        assertThrows(TagNotFoundException.class, () -> tagService.findById(generatedTag.getId()));
    }

    @Test
    public void delete_notExists() {
        // GIVEN
        Tag generatedTag = dataManipulator.saveTag(DataGenerator.generateValidTag());

        // THEN
        assertThrows(TagNotFoundException.class, () -> tagService.delete(generatedTag.getId() + 1, false));
    }
}
