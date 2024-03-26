package com.utcn.StackOverflow.service;

import com.utcn.StackOverflow.entity.Question;
import com.utcn.StackOverflow.entity.Tag;
import com.utcn.StackOverflow.entity.User;
import com.utcn.StackOverflow.entity.UserRole;
import com.utcn.StackOverflow.repository.UserRepository;
import com.utcn.StackOverflow.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public boolean askQuestion(Long userId, String title, String text, Set<Tag> tags) {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) return false;
        User user = maybeUser.get();


        Question question = new Question(user, title, text, "/mnt/pictures/example", tags);
        for (Tag tag : tags) {
            tag.getQuestions().add(question);
            System.out.println(tag.getQuestions());
        }
        postService.insertPost(question);
        return true;
    }
}
