package com.utcn.StackOverflow.repository;

import com.utcn.StackOverflow.entity.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Long> {
}
