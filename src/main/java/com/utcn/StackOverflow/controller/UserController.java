package com.utcn.StackOverflow.controller;

import com.google.gson.Gson;
import com.utcn.StackOverflow.DTOs.post.CreateQuestionDTO;
import com.utcn.StackOverflow.DTOs.users.UpdateUserDTO;
import com.utcn.StackOverflow.DTOs.users.UserDTO;
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


    @PostMapping("/saveUser")
    public String saveUser(@RequestBody UserDTO userDTO){
        return userService
                .insertUser(userDTO)
                .toString();
    }

    @PutMapping("/updateUser")
    public String updateUser(@RequestBody UpdateUserDTO updateUserDTO){
        return userService
                .updateUser(updateUserDTO)
                .toString();

    }
    @ResponseBody
    @DeleteMapping("/deleteUser")
    public Boolean deleteUserById(@RequestParam Long id) {
        return userService.deleteById(id);
    }
}
