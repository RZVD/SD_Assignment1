package com.utcn.StackOverflow.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    Boolean banned;

    @Setter
    @Getter
    private String phoneNumber;

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    private Float score;
    public Float updateScore(Float points) {
        this.score += points;
        return this.score;
    }
    public Boolean isBanned() {
        return this.banned;
    }


    public void ban() {
        banned = true;
    }
    public void unban() {
        banned = false;
    }

    @OneToMany(mappedBy = "user")
    public Set<Vote> votes;

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) {
        try {
            this.passwordHash = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
        } catch (NoSuchAlgorithmException ignored) {
        }
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public User(String username, String password, Set<UserRole> roleSet) {
        this.username = username;
        this.banned = false;
        this.roles = roleSet;
        try {
            this.passwordHash = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
        } catch (NoSuchAlgorithmException ignored) {
        }
    }

    @Id
    @Column(name = "user_id")
    @JsonProperty("userId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username")
    @JsonProperty("username")
    private String username;

    @OneToMany
    @JoinColumn(name = "user_id")
    @JsonProperty("userRoles")
    private Set<UserRole> roles;

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    @Column(name = "password_hash")
    private byte[] passwordHash;


    public User(String username, String password) {
        this.username = username;
        this.score = 0.0f;
        this.banned = false;
        try {
            this.passwordHash = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
        } catch (NoSuchAlgorithmException ignored) {}
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        StringBuilder jsonBuilder = new StringBuilder("{");
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();
            switch (fieldName) {
                case "passwordHash", "votes", "votedPosts", "roles" -> {
                    continue;
                }
                default -> {
                    try {
                        // Access private fields
                        field.setAccessible(true);

                        // Get field name and value
                        Object value = field.get(this);

                        // Append field name and value to JSON string
                        jsonBuilder.append("\"").append(fieldName).append("\":\"");

                        // Escape quotes in the field value
                        if (value != null) {
                            String fieldValue = value.toString().replace("\"", "\\\"");
                            jsonBuilder.append(fieldValue);
                        }

                        jsonBuilder.append("\",");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        jsonBuilder.append("\"roles\":")
            .append(this.getRoles().toString())
            .append(",");

        // Remove trailing comma if exists
        if (jsonBuilder.length() > 1) {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }

        jsonBuilder.append("}");

        return jsonBuilder.toString();
    }

    public User() {
        this.banned=false;
    }
}
