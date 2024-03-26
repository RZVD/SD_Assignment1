package com.utcn.StackOverflow.controller;

import com.utcn.StackOverflow.DTOs.post.CreateAnswerDTO;
import com.utcn.StackOverflow.DTOs.post.CreateQuestionDTO;
import com.utcn.StackOverflow.DTOs.post.UpdatePostDTO;
import com.utcn.StackOverflow.entity.*;
import com.utcn.StackOverflow.service.PostService;
import com.utcn.StackOverflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class PostController {
    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @ResponseBody
    @GetMapping("/getAllQuestions")
    public String getQuestions() {
        return "{\"questions\":" + postService.getAllQuestionsSortedDescendingly().toString() + "}";
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
    public Boolean updatePost(@RequestParam Long postId, @RequestBody UpdatePostDTO updatePostDTO){
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
        System.out.println(createQuestionDTO.getTags());
        Set<Tag> tags = createQuestionDTO.getTags().stream()
                .map(String::toUpperCase)
                .map(Tag::new)
                .collect(Collectors.toSet());

        return userService.askQuestion(createQuestionDTO);
    }

}
