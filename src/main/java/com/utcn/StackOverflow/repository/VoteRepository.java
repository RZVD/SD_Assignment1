package com.utcn.StackOverflow.repository;

import com.utcn.StackOverflow.entity.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoteRepository extends CrudRepository<Vote, Long> {
    @Query(value =
        "SELECT * FROM votes " +
        "WHERE votes.post_id = :post_id AND votes.user_id = :user_id ",
        nativeQuery = true
    )
    Optional<Vote> findByUserIdAndPostId(@Param("user_id") Long userId,@Param("post_id") Long postId);
}