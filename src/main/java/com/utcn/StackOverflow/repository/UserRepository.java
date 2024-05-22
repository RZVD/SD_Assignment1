package com.utcn.StackOverflow.repository;

import com.utcn.StackOverflow.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query( value =
        "SELECT u.* FROM users u " +
        "JOIN user_role ur ON u.user_id = ur.user_id " +
        "WHERE u.username = :username",
        nativeQuery = true
    )
    public Optional<User> getUserByUsername(@Param("username") String username);
}
