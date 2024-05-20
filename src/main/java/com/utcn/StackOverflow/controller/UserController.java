package com.utcn.StackOverflow.controller;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.utcn.StackOverflow.DTOs.users.BanUserDTO;
import com.utcn.StackOverflow.DTOs.users.UpdateUserDTO;
import com.utcn.StackOverflow.DTOs.users.UserDTO;
import com.utcn.StackOverflow.entity.User;
import com.utcn.StackOverflow.service.UserRoleService;
import com.utcn.StackOverflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.twilio.rest.api.v2010.account.Message;



@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @ResponseBody
    @GetMapping("/getAll")

    public String getAllUsers(){
        return "{\"users\":" +  userService.getUsers().toString() + "}\n";
    }

    @ResponseBody
    @GetMapping("/get")
    public String getUserByID(@RequestParam Long id){
        return userService
                .getUser(id)
                .map(User::toString)
                .orElse("{}");
    }

    @ResponseBody
    @GetMapping("/getByUsername")
    public String getUserByUsername(@RequestParam String username){
        return userService
                .getUserByUsername(username)
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


    @ResponseBody
    @PostMapping("/ban")
    public Boolean banUser(@RequestBody BanUserDTO banUerDTO) {
        return userService.banUser(banUerDTO);

    }
}
