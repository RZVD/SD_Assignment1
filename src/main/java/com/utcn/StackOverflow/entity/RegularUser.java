package com.utcn.StackOverflow.entity;

import jakarta.persistence.Entity;

@Entity
public class RegularUser extends UserRole{
    @Override
    public String toString() {
        return "REGULAR";
    }

    public RegularUser() {
        super();
    }
}
