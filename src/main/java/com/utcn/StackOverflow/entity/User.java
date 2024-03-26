package com.utcn.StackOverflow.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    public User(String username, String password, Set<UserRole> roleSet) {
        this.username = username;
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

    @ManyToMany
    @JoinTable(name = "user_votes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<Post> votedPosts;

    @Column(name = "password_hash")
    private byte[] passwordHash;


    public User(String username, String password) {
        this.username = username;
        try {
            this.passwordHash = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
        } catch (NoSuchAlgorithmException ignored) {
        }
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        HashMap<String, Boolean> excludedFields = new HashMap<>();
        excludedFields.put("passwordHash", false);

        StringBuilder jsonBuilder = new StringBuilder("{");
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();
            switch (fieldName) {
                case "passwordHash", "votedPosts" -> {
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

        // Remove trailing comma if exists
        if (jsonBuilder.length() > 1) {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }

        jsonBuilder.append("}");

        return jsonBuilder.toString();
    }

    public User() {
    }
}
