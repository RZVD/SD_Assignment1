package com.utcn.StackOverflow.repository;

import com.utcn.StackOverflow.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
