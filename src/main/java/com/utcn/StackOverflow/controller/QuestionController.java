package com.utcn.StackOverflow.controller;

import com.utcn.StackOverflow.entity.Answer;
import com.utcn.StackOverflow.entity.Post;
import com.utcn.StackOverflow.entity.Question;
import com.utcn.StackOverflow.entity.User;
import com.utcn.StackOverflow.service.PostService;
import com.utcn.StackOverflow.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class QuestionController {
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
    @GetMapping("/delete/{id}")
    public Boolean deleteQuestion(@PathVariable Long id) {
        return postService.deleteById(id);
    }

    @ResponseBody
    @GetMapping("/{questionId}/answers")
    public String getAnswersOfQuestion(@PathVariable("questionId") Long questionId){
        return postService.getAnswers(questionId).toString();
    }


    @ResponseBody
    @GetMapping("/{postId}/update")
    public Boolean updatePost(@PathVariable Long postId, HttpServletRequest request, HttpServletResponse response){
        String text = request.getParameter("text");
        String picturePath = request.getParameter("picture");
        Long userId = Long.parseLong(request.getParameter("whoami") );

        Optional<Post> maybePost = postService.getById(postId);
        Optional<User> maybeUser = userService.getUser(userId);
        if (maybePost.isEmpty() || maybeUser.isEmpty()) return false;
        Post post = maybePost.get();
        User user = maybeUser.get();

        post.setBody(text);
        post.setPicturePath(picturePath);

        if(post instanceof Question question) {
            String title = request.getParameter("title");
            question.setTitle(title);
        }

        postService.insertPost(post);

        return true;
    }

    @ResponseBody
    @GetMapping("/{question_id}/answer/")
    public Boolean answer(@PathVariable(name = "question_id") Long questionId, HttpServletRequest request, HttpServletResponse response) {

        String text = request.getParameter("text");
        String picturePath = request.getParameter("picture");
        Long userId = Long.parseLong(request.getParameter("whoami") );

        Optional<Post> maybePost = postService.getById(questionId);
        Optional<User> maybeUser = userService.getUser(userId);
        if (maybePost.isEmpty() || maybeUser.isEmpty()) return false;

        Post post = maybePost.get();
        User user = maybeUser.get();

        if (post instanceof Question question) {
            Answer answer = new Answer(user, text, picturePath ,question);

            question.getAnswers().add(answer);
            postService.insertPost(answer);
            postService.insertPost(post);
            return true;
        }
        return false;
    }

}
