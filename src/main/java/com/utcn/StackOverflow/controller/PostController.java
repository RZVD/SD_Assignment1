package com.utcn.StackOverflow.controller;

import com.utcn.StackOverflow.DTOs.post.*;
import com.utcn.StackOverflow.DTOs.users.LoginDTO;
import com.utcn.StackOverflow.entity.Question;
import com.utcn.StackOverflow.service.PostService;
import com.utcn.StackOverflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RequestMapping("/posts")
public class PostController {
    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @ResponseBody
    @GetMapping("/getAllQuestions")
    public ResponseEntity<String> getQuestions() {
        return ResponseEntity.ok("{\"questions\":" + postService.getAllQuestionsSortedDescendingly().toString() + "}");
//        return "{\"questions\":" + postService.getAllQuestionsSortedDescendingly().toString() + "}";
//        return postService.getAllQuestionsSortedDescendingly();
    }

    @ResponseBody
    @GetMapping("/getAll")
    public String getPosts() {
        return "{\"posts\":" + postService.getAllPosts().toString() + "}";
    }

    @ResponseBody
    @DeleteMapping("/delete")
    public Boolean deleteQuestion(@RequestParam Long id) {
        return postService.deleteById(id);
    }

    @ResponseBody
    @GetMapping("/answers")
    public String getAnswersOfQuestion(@RequestParam("questionId") Long questionId){
        return postService.getAnswers(questionId).toString();
    }

    @ResponseBody
    @PutMapping("/update")
    public Boolean updatePost(@RequestBody UpdatePostDTO updatePostDTO){
        return postService.updatePost(updatePostDTO);
    }

    @ResponseBody
    @PostMapping("/answer")
    public Boolean answer(@RequestBody CreateAnswerDTO createAnswerDTO) {
        return postService.answerQuestion(createAnswerDTO);
    }

    @ResponseBody
    @PostMapping("/ask")
    public Boolean askQuestion(@RequestBody CreateQuestionDTO createQuestionDTO) {
        return userService.askQuestion(createQuestionDTO);
    }

    @ResponseBody
    @PostMapping("/getByTags")
    public String getByTags(@RequestBody GetByTagsDTO getByTagsDTO) {
        return postService.getQuestionsByTags(getByTagsDTO).toString();
    }

    @ResponseBody
    @PostMapping("/search")
    public String searchQuestion(@RequestParam String query) {
        return postService.searchQuestions(query).toString();
    }

    @ResponseBody
    @GetMapping("/getAskedBy")
    public String getAskedBy(@RequestParam Long author) {
        return postService.getAskedBy(author).toString();

    }
    @ResponseBody
    @PostMapping("/vote")
    public String vote(@RequestBody VoteDTO voteDTO) {
        return "{\"status\":" + postService.votePost(voteDTO)+ "}";
    }

    @ResponseBody
    @GetMapping("/getPost")
    public String getById(@RequestParam Long id) {
        return postService.getById(id).get().toString();
    }

}
