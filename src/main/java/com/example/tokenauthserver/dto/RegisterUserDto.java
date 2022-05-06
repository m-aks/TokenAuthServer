package com.example.tokenauthserver.dto;

import com.example.tokenauthserver.model.User;
import lombok.Data;

@Data
public class RegisterUserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public User toUser(){
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
