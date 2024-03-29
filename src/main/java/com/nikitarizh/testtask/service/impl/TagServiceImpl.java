package com.nikitarizh.testtask.service.impl;

import com.nikitarizh.testtask.dto.tag.TagCreateDTO;
import com.nikitarizh.testtask.dto.tag.TagFullDTO;
import com.nikitarizh.testtask.dto.tag.TagPreviewDTO;
import com.nikitarizh.testtask.entity.Tag;
import com.nikitarizh.testtask.exception.TagIsUsedException;
import com.nikitarizh.testtask.exception.TagNotFoundException;
import com.nikitarizh.testtask.repository.TagRepository;
import com.nikitarizh.testtask.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.nikitarizh.testtask.mapper.TagMapper.TAG_MAPPER;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    @Transactional(readOnly = true)
    public TagFullDTO findById(Integer id) {
        Tag result = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));
        return TAG_MAPPER.mapToFullDTO(result);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> findAllByIds(List<Integer> ids) {
        return ids.stream()
                .map((id) -> tagRepository.findById(id)
                        .orElseThrow(() -> new TagNotFoundException(id)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagPreviewDTO> findAll() {
        return tagRepository.findAll()
                .stream()
                .map(TAG_MAPPER::mapToPreviewDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TagPreviewDTO create(TagCreateDTO tagCreateDTO) {
        Tag newTag = tagRepository.save(TAG_MAPPER.mapToEntity(tagCreateDTO));
        return TAG_MAPPER.mapToPreviewDTO(newTag);
    }

    @Override
    @Transactional
    public void delete(Integer id, boolean force) {
        Tag tagToDelete = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));

        if (!force && tagToDelete.getProducts().size() > 0) {
            throw new TagIsUsedException(tagToDelete);
        }

        tagToDelete.getProducts().forEach(product -> product.getTags().remove(tagToDelete));

        tagRepository.deleteById(id);
    }
}
