package com.utcn.StackOverflow.entity;


import jakarta.persistence.Entity;

@Entity
public class Moderator extends UserRole {
    public Moderator() {
        super();
    }

    @Override
    public String toString() {
        return "MODERATOR";
    }

}
