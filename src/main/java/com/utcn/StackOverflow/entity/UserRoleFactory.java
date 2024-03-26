package com.utcn.StackOverflow.entity;

public class UserRoleFactory {
    public static UserRole createUserRole(UserType userType) {
        return switch (userType) {
            case REGULAR -> new RegularUser();
            case MODERATOR -> new Moderator();
        };
    }
}
