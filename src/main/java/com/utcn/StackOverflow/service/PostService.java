package com.utcn.StackOverflow.service;

import com.utcn.StackOverflow.DTOs.post.CreateAnswerDTO;
import com.utcn.StackOverflow.DTOs.post.GetByTagsDTO;
import com.utcn.StackOverflow.DTOs.post.UpdatePostDTO;
import com.utcn.StackOverflow.DTOs.post.VoteDTO;
import com.utcn.StackOverflow.entity.*;
import com.utcn.StackOverflow.repository.PostRepository;
import com.utcn.StackOverflow.repository.TagRepository;
import com.utcn.StackOverflow.repository.UserRepository;
import com.utcn.StackOverflow.repository.VoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoteRepository voteRepository;

    public List<Post> getPosts() {
        return (List<Post>) this.postRepository.findAll();
    }
    @Transactional
    public Post insertPost(Post post) {
        if (post instanceof Question question) {
            System.out.println(question.getAnswers());
            System.out.println(question.getTags());
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
        if(user.getRoles().stream().noneMatch(userRole -> userRole instanceof Moderator) && !Objects.equals(post.getAuthor().getUserId(), user.getUserId())) return false;
        post.setBody(updatePostDTO.getText());
        post.setPicturePath(updatePostDTO.getPicturePath());

        if(post instanceof Question question) {
            String title = updatePostDTO.getTitle();
            var tags = question.getTags();
            question.setTitle(title);
            question.setTags(tags);
        }

        this.insertPost(post);

        return true;
    }


    public List<Question> getAllQuestionsSortedDescendingly(){
        return this.postRepository.getAllQuestionsSortedDescendingly();
    }

    public List<Question> getQuestionsByTags(GetByTagsDTO getByTagsDTO) {
        return this.postRepository.getQuestionsByTags(getByTagsDTO.getTags());
    }

    public List<Question> searchQuestions(String query) {
        return this.postRepository.searchQuestion(query);
    }

    public List<Question> getAskedBy(Long user_id) {
        return this.postRepository.getAskedBy(user_id);
    }

    public Integer votePost(VoteDTO voteDTO) {
        Optional<User> maybeUser = userRepository.findById(voteDTO.getUserId());
        Optional<Post> maybePost = postRepository.findById(voteDTO.getPostId());
        int voteWeight = voteDTO.getVoteWeight();
        if (maybeUser.isEmpty() || maybePost.isEmpty()) return 0;

        //System.out.println(voteWeight);
        Post post = maybePost.get();
        User user = maybeUser.get();

        if(Objects.equals(post.getAuthor().getUserId(), user.getUserId())) return 0;


        Optional<Vote> existingVote = voteRepository.findByUserIdAndPostId(user.getUserId(), post.getId());
        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();
            if (existingVote.get().getVoteWeight() == voteWeight){
                return 0;
            }
//            vote.setVoteWeight(voteWeight);
//            post.addVote(vote, true);
//            user.getVotes().add(vote);
//            this.postRepository.save(post);
//            this.userRepository.save(user);
//            this.voteRepository.save(vote);
//            return voteWeight;
            return 0;

        }

        Vote newVote = new Vote(post, user, voteWeight);
        int r = post.addVote(newVote);

        user.getVotes().add(newVote);
        post.addVote(newVote);
        this.postRepository.save(post);
        this.userRepository.save(user);
        this.voteRepository.save(newVote);


        return r;
    }

}
