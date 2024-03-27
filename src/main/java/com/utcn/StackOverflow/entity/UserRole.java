package com.utcn.StackOverflow.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class UserRole {
    public UserRole(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public UserRole setUser(User user) {
        this.user = user;
        return this;
    }

    public UserRole() {
    }

    @Id
    @Column(name = "user_role_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
