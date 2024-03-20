package com.askMeProject.dto;

import lombok.Data;

@Data
public class PasswordRequestUtil {
    private String email;
    private String oldPassword;
    private String newPassword;
}

