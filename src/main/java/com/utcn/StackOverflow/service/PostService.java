package com.utcn.StackOverflow.service;

import com.utcn.StackOverflow.DTOs.post.CreateAnswerDTO;
import com.utcn.StackOverflow.DTOs.post.UpdatePostDTO;
import com.utcn.StackOverflow.entity.Answer;
import com.utcn.StackOverflow.entity.Post;
import com.utcn.StackOverflow.entity.Question;
import com.utcn.StackOverflow.entity.User;
import com.utcn.StackOverflow.repository.PostRepository;
import com.utcn.StackOverflow.repository.TagRepository;
import com.utcn.StackOverflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Post> getPosts() {
        return (List<Post>) this.postRepository.findAll();
    }

    public Post insertPost(Post post) {
        if (post instanceof Question question) {
            System.out.println(question.getAnswers());
            tagRepository.saveAll(question.getTags());
            postRepository.saveAll(question.getAnswers());
        }
        return postRepository.save(post);
    }


    public List<Post> getAllPosts() {
        return (List<Post>) postRepository.findAll();
    }

    public Boolean deleteById(Long id) {
        try {
            this.postRepository.deleteById(id);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Boolean answerQuestion(CreateAnswerDTO createAnswerDTO){
        Optional<Post> maybePost = this.getById(createAnswerDTO.getQuestionId());
        Optional<User> maybeUser = userRepository.findById(createAnswerDTO.getUserId());

        if (maybePost.isEmpty() || maybeUser.isEmpty())
            return false;

        Post post = maybePost.get();
        User user = maybeUser.get();

        if (post instanceof Question question) {
            Answer answer = new Answer(user, createAnswerDTO.getText(), createAnswerDTO.getPicturePath(), question);

            question.getAnswers().add(answer);
            this.insertPost(answer);
            this.insertPost(post);
            return true;
        }
        return false;
    }

    public List<Answer> getAnswers(Long questionId) {
        return postRepository.getAnswersOfQuestion(questionId);
    }

    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }

    public Boolean updatePost(UpdatePostDTO updatePostDTO) {

        Optional<Post> maybePost = this.getById(updatePostDTO.getPostId());
        Optional<User> maybeUser = userRepository.findById(updatePostDTO.getUserId());
        if (maybePost.isEmpty() || maybeUser.isEmpty()) return false;
        Post post = maybePost.get();
        User user = maybeUser.get();

        post.setBody(updatePostDTO.getText());
        post.setPicturePath(updatePostDTO.getPicturePath());

        if(post instanceof Question question) {
            String title = updatePostDTO.getTitle();
            question.setTitle(title);
        }

        this.insertPost(post);

        return true;
    }


    public List<Question> getAllQuestionsSortedDescendingly(){
        return this.postRepository.getAllQuestionsSortedDescendingly();
    }
}
