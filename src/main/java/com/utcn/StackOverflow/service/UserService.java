package com.utcn.StackOverflow.service;

import com.utcn.StackOverflow.DTOs.post.CreateQuestionDTO;
import com.utcn.StackOverflow.DTOs.users.UpdateUserDTO;
import com.utcn.StackOverflow.DTOs.users.UserDTO;
import com.utcn.StackOverflow.entity.*;
import com.utcn.StackOverflow.repository.UserRepository;
import com.utcn.StackOverflow.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PostService postService;
    public List<User> getUsers() {
        return (List<User>) this.userRepository.findAll();
    }

    public User insertUser(User user) {
        this.userRepository.save(user);
        this.userRoleRepository.saveAll(user.getRoles());
        return user;
    }

    public User insertUser(UserDTO userDTO) {

        User user = new User(
                userDTO.getUsername(),
                userDTO.getPassword()
        );

        Set<UserRole> userRoles = userDTO.getRoles().stream()
                .map(role -> UserRoleFactory.createUserRole(UserType.valueOf(role)).setUser(user))
                .collect(Collectors.toSet());

        user.setRoles(userRoles);
        this.userRepository.save(user);
        this.userRoleRepository.saveAll(user.getRoles());

        return user;
    }

    public User updateUser(UpdateUserDTO updateUserDTO){
        User user = userRepository.findById(updateUserDTO.getId()).get();

        user.setUsername(
            updateUserDTO.getUsername()
        );
        user.setPassword(updateUserDTO.getPassword());

        Set<UserRole> userRoles = updateUserDTO.getRoles().stream()
                .map(role -> UserRoleFactory.createUserRole(UserType.valueOf(role)).setUser(user))
                .collect(Collectors.toSet());
        user.setRoles(userRoles);
        this.userRepository.save(user);
        this.userRoleRepository.saveAll(user.getRoles());

        return user;
    }

    public Boolean deleteById(Long id) {
        Optional<User> maybeUser = userRepository.findById(id);
        if (maybeUser.isEmpty()) return false;
        try {
            User user = maybeUser.get();
            for (UserRole userRole : user.getRoles()){
                userRoleRepository.deleteById(userRole.getId());
            }
            this.userRepository.deleteById(id);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Optional<User> getUser(Long id){
        return userRepository.findById(id);
    }

    public boolean askQuestion(CreateQuestionDTO createQuestionDTO) {
        Optional<User> maybeUser = userRepository.findById(createQuestionDTO.getUserId());
        if (maybeUser.isEmpty()) return false;
        User user = maybeUser.get();


        Set<Tag> tags = createQuestionDTO.getTags().stream()
                .map(String::toUpperCase)
                .map(Tag::new)
                .collect(Collectors.toSet());


        Question question = new Question(
            user,
            createQuestionDTO.getTitle(),
            createQuestionDTO.getText(),
            createQuestionDTO.getPicturePath(),
            tags
        );
        for (Tag tag : tags) {
            tag.getQuestions().add(question);
        }
        postService.insertPost(question);
        return true;
    }
}
