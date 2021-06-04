package com.nikitarizh.testtask.mapper;

import com.nikitarizh.testtask.dto.tag.TagCreateDTO;
import com.nikitarizh.testtask.dto.tag.TagFullDTO;
import com.nikitarizh.testtask.dto.tag.TagPreviewDTO;
import com.nikitarizh.testtask.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper {

    TagMapper TAG_MAPPER = Mappers.getMapper(TagMapper.class);

    TagFullDTO mapToFullDTO(Tag tag);

    TagPreviewDTO mapToPreviewDTO(Tag tag);

    Tag mapToEntity(TagCreateDTO tagFullDTO);
}
