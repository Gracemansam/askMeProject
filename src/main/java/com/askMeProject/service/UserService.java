package com.askMeProject.service;


import com.askMeProject.dto.LoginRequest;
import com.askMeProject.dto.UserDTO;
import com.askMeProject.dto.response.AuthResponse;
import com.askMeProject.model.User;
import com.askMeProject.model.VerificationToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {


    ResponseEntity<AuthResponse> registerPatient(UserDTO userDto, HttpServletRequest request);

    ResponseEntity<AuthResponse> registerDoctor(UserDTO userDto, HttpServletRequest request);

    ResponseEntity<AuthResponse> signIn(LoginRequest loginRequest);

    Authentication authenticate(String username, String password);

    public User findUserById(Long userId) ;
	
	public User findUserProfileByJwt(String jwt) ;
	
//	public List<User> findAllUsers();

    void saveUserVerificationToken(User theUser, String token);

    String validateToken(String theToken);

    VerificationToken findByToken(String token);
}
