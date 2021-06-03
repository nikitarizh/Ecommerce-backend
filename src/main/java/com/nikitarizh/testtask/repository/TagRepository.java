package com.nikitarizh.testtask.repository;

import com.nikitarizh.testtask.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
}
