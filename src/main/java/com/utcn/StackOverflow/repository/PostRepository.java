package com.utcn.StackOverflow.repository;

import com.utcn.StackOverflow.entity.Answer;
import com.utcn.StackOverflow.entity.Post;
import com.utcn.StackOverflow.entity.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    @Query(value =
        "SELECT * FROM posts " +
        "JOIN questions ON posts.post_id = questions.post_id " +
        "ORDER BY posts.timestamp DESC",
        nativeQuery = true
    )
    public List<Question> getAllQuestionsSortedDescendingly();

    @Query(value =
            "SELECT * FROM posts p1 " +
            "JOIN answers ON p1.post_id = answers.post_id " +
            "WHERE answers.question_post_id = ?1",
            nativeQuery = true
    )
    public List<Answer> getAnswersOfQuestion(Long questionId);

    @Query(value =
            "SELECT DISTINCT posts.*, questions.* " +
            "FROM posts " +
            "JOIN questions ON posts.post_id = questions.post_id " +
            "JOIN question_tags ON questions.post_id = question_tags.post_id " +
            "JOIN tags ON question_tags.tag_id = tags.tag_id " +
            "WHERE tags.tag_id IN :tags " +
            "GROUP BY posts.post_id, questions.post_id",
            nativeQuery = true
    )
    public List<Question> getQuestionsByTags(@Param("tags") List<String> tags);
    @Query(value =
            "SELECT DISTINCT * FROM posts " +
            "JOIN questions ON posts.post_id = questions.post_id " +
            "WHERE questions.title ILIKE CONCAT('%', :query, '%')",
    nativeQuery = true
    )
    public List<Question> searchQuestion(@Param("query") String query);

    @Query(value =
            "SELECT DISTINCT * FROM posts " +
            "JOIN questions ON posts.post_id = questions.post_id " +
            "WHERE posts.user_id = :author",
            nativeQuery = true
    )
    public List<Question> getAskedBy(@Param("author") Long author);

}
