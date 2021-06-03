package com.nikitarizh.testtask.mapper;

import com.nikitarizh.testtask.dto.tag.TagFullDTO;
import com.nikitarizh.testtask.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ProductMapper.class)
public interface TagMapper {

    TagMapper TAG_MAPPER = Mappers.getMapper(TagMapper.class);

    TagFullDTO mapToFullDTO(Tag tag);

    Tag mapToEntity(TagFullDTO tagFullDTO);
}
