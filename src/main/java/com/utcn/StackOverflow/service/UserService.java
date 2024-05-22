package com.utcn.StackOverflow.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.*;
import com.twilio.type.PhoneNumber;
import com.utcn.StackOverflow.DTOs.post.CreateQuestionDTO;
import com.utcn.StackOverflow.DTOs.users.BanUserDTO;
import com.utcn.StackOverflow.DTOs.users.UpdateUserDTO;
import com.utcn.StackOverflow.DTOs.users.UserDTO;
import com.utcn.StackOverflow.entity.*;
import com.utcn.StackOverflow.repository.PostRepository;
import com.utcn.StackOverflow.repository.UserRepository;
import com.utcn.StackOverflow.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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
    @Autowired
    private PostRepository postRepository;

    public List<User> getUsers() {
        return (List<User>) this.userRepository.findAll();
    }


    private String TWILIO_AUTH = System.getenv("TWILIO_AUTH");
    private String TWILIO_SID  = System.getenv("TWILIO_SID");
    private String TWILIO_PHONE_NUMBER = System.getenv("TWILIO_PHONE_NUMBER");



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

        user.setUsername(updateUserDTO.getUsername());

        if (!updateUserDTO.getPassword().isEmpty()){
            user.setPassword(updateUserDTO.getPassword());
        }

        Set<UserRole> userRoles = updateUserDTO.getRoles().stream()
                .map(role -> UserRoleFactory.createUserRole(UserType.valueOf(role), user))
                .collect(Collectors.toSet());

        user.setRoles(userRoles);
        this.userRoleRepository.saveAll(userRoles);
        this.userRepository.save(user);

        return user;
    }

    public Boolean deleteById(Long id) {
        Optional<User> maybeUser = userRepository.findById(id);
        if (maybeUser.isEmpty()) return false;
        try {
            User user = maybeUser.get();
            if(user.isBanned()) return false;
            postService.getAllPosts()
                    .stream().filter(post -> post.getAuthor().getUserId().equals(id))
                    .forEach(post -> postRepository.delete(post));

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

        if(user.isBanned()) return false;

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

    private void notifyUser(User user) {
        Twilio.init(TWILIO_SID, TWILIO_AUTH);

        Message message = Message.creator(
            new PhoneNumber(user.getPhoneNumber()),
            new PhoneNumber(TWILIO_PHONE_NUMBER),

            "You have been banned from the forum"
        ).create();
    }


    public Boolean banUser(BanUserDTO banUserDTO) {
        Optional<User> maybeBanningUser = userRepository.findById(banUserDTO.getBanningUserId());
        Optional<User> maybeBannedUser = userRepository.findById(banUserDTO.getBannedUserId());

        if (maybeBanningUser.isEmpty() || maybeBannedUser.isEmpty()) return false;

        User banningUser = maybeBanningUser.get();
        User bannedUser = maybeBannedUser.get();


        if ( banningUser.isBanned() || banningUser.getRoles().stream().noneMatch(role -> role instanceof Moderator)) {
            return false;
        }

        bannedUser.ban();

        notifyUser(bannedUser);
        return true;
    }

    public Optional<User> getUserByUsername(String username) {
        return this.userRepository.getUserByUsername(username);
    }

    public Boolean unbanUser(BanUserDTO banUserDTO) {
        Optional<User> maybeBanningUser = userRepository.findById(banUserDTO.getBanningUserId());
        Optional<User> maybeBannedUser = userRepository.findById(banUserDTO.getBannedUserId());

        if (maybeBanningUser.isEmpty() || maybeBannedUser.isEmpty()) return false;

        User banningUser = maybeBanningUser.get();
        User bannedUser = maybeBannedUser.get();

        if (banningUser.getRoles().stream().noneMatch(role -> role instanceof Moderator)) {
            return false;
        }

        bannedUser.unban();

        return true;
    }
}
