package com.utcn.StackOverflow.DTOs.users;

import lombok.Getter;
import lombok.Setter;

public class LoginDTO {
    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;
}
