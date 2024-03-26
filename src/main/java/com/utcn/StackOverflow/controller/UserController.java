package com.utcn.StackOverflow.controller;

import com.google.gson.Gson;
import com.utcn.StackOverflow.entity.*;
import com.utcn.StackOverflow.service.UserRoleService;
import com.utcn.StackOverflow.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    private final Gson gson = new Gson();

    @ResponseBody
    @GetMapping("/getAll")

    public String getAllUsers(){
        return "{\"users\":" +  userService.getUsers().toString() + "}\n";
    }

    @ResponseBody
    @GetMapping("/get/{id}")
    public String getUserByID(@PathVariable Long id){
        return userService
                .getUser(id)
                .map(User::toString)
                .orElse("{}");
    }


    @GetMapping("/saveRZVD")
    public boolean saveRZVD(){
        UserRole m = new Moderator();
        Set<UserRole> ur = new HashSet<>();

        ur.add(m);
        User u = new User("RZVD", "abc", ur);

        m.setUser(u);
        this.userService.insertUser(u);
        this.userRoleService.save(m);
        return true;
    }
    @ResponseBody
    @PutMapping("/saveUser")
    public String saveUser(HttpServletRequest request, HttpServletResponse response){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User u = new User(username, password);
        u.setRoles(Arrays.stream(request.getParameter("roles").split(",")).
                map(role -> UserRoleFactory.createUserRole(UserType.valueOf(role)).setUser(u))
                .collect(Collectors.toSet()));

        this.userService.insertUser(u);
        return String.format("%s %s %s", username, password, u.getRoles());
    }
    @ResponseBody
    @GetMapping("/deleteUser/{id}")
    public Boolean deleteUserById(@PathVariable Long id) {
        return userService.deleteById(id);
    }

    @ResponseBody
    @GetMapping("/ask/{id}")
    public Boolean askQuestion(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id) {
       String title = request.getParameter("title");
       String text  = request.getParameter("text");

       Set<Tag> tags = Arrays.stream(request.getParameter("tags").split(","))
               .map(String::toUpperCase)
               .map(Tag::new)
               .collect(Collectors.toSet());

       return userService.askQuestion(id, title, text, tags);
    }
}
