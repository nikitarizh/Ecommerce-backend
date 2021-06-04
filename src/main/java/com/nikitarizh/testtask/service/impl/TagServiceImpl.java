package com.nikitarizh.testtask.service.impl;

import com.nikitarizh.testtask.dto.tag.TagFullDTO;
import com.nikitarizh.testtask.entity.Tag;
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
        Tag result = tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException(id));
        return TAG_MAPPER.mapToFullDTO(result);
    }

    @Override
    public List<Tag> findAllByIds(List<Integer> ids) {
        return ids.stream()
                .map((id) -> tagRepository.findById(id)
                        .orElseThrow(() -> new TagNotFoundException(id)))
                .collect(Collectors.toList());
    }
}
