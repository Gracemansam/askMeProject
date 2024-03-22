package com.askMeProject.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    @NotNull(message = "Email is mandatory")
    @NotEmpty(message = "Email cannot be empty")
    @Size(min = 1, max = 50, message = "Email should be between 1 to 50 characters in length")
    private String email;
    private String phoneNumber;
    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    private String username;
    private String speciality;
    private String qualification;
    private String token;
    private boolean mfaEnabled;

    public UserDTO(String firstName, String email, String phoneNumber, String token) {
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.token = token;
    }
}
