package com.utcn.StackOverflow.repository;

import com.utcn.StackOverflow.entity.Answer;
import com.utcn.StackOverflow.entity.Post;
import com.utcn.StackOverflow.entity.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    @Query(value =
        "SELECT * FROM posts " +
        "JOIN questions ON posts.post_id = questions.post_id " +
        "ORDER BY posts.timestamp DESC",
        nativeQuery = true
    )
    public List<Question> getAllQuestionsSortedDescendingly();


    @Query(value=
            "SELECT * FROM posts p1 " +
            "JOIN answers ON p1.post_id = answers.post_id " +
            "WHERE answers.question_post_id = ?1",
            nativeQuery = true
    )
    public List<Answer> getAnswersOfQuestion(Long questionId);
}
