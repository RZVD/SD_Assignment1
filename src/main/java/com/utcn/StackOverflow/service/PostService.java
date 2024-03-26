package com.utcn.StackOverflow.service;

import com.utcn.StackOverflow.entity.Answer;
import com.utcn.StackOverflow.entity.Post;
import com.utcn.StackOverflow.entity.Question;
import com.utcn.StackOverflow.repository.PostRepository;
import com.utcn.StackOverflow.repository.TagRepository;
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

    public List<Post> getPosts() {
        return (List<Post>) this.postRepository.findAll();
    }

    public Post insertPost(Post post) {
        if (post instanceof Question question) {
            tagRepository.saveAll(question.getTags());
            postRepository.saveAll(question.getAnswers());
        }
        return this.postRepository.save(post);
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


    public List<Answer> getAnswers(Long questionId) {
        return postRepository.getAnswersOfQuestion(questionId);
    }

    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }

    public List<Question> getAllQuestionsSortedDescendingly(){
        return this.postRepository.getAllQuestionsSortedDescendingly();
    }
}
