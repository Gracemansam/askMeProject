package com.askMeProject.controller;


import com.askMeProject.common_constant.CommonConstant;
import com.askMeProject.dto.LoginRequest;
import com.askMeProject.dto.TwoFAVerificationRequest;
import com.askMeProject.dto.UserDTO;
import com.askMeProject.dto.response.AuthResponse;
import com.askMeProject.model.VerificationToken;
import com.askMeProject.service.implementation.UserServiceImplementation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class AuthController {

    private final UserServiceImplementation userServiceImplementation;

    @Autowired
    public AuthController(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }

    @PostMapping("/register-patient")
    public ResponseEntity<AuthResponse> registerPatient(@Valid @RequestBody UserDTO userDto, HttpServletRequest request){

            // Call the createUserHandler method from the service layer
            ResponseEntity<AuthResponse> responseEntity = userServiceImplementation.registerPatient(userDto,request);
            return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);

    }

    @PostMapping("/register-doctor")
    public ResponseEntity<AuthResponse> registerDoctor(@Valid @RequestBody UserDTO userDto, HttpServletRequest request){

        // Call the createUserHandler method from the service layer
        ResponseEntity<AuthResponse> responseEntity = userServiceImplementation.registerDoctor(userDto,request);
        return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);

    }


    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Call the signIn method from the service layer
            ResponseEntity<AuthResponse> responseEntity = userServiceImplementation.signIn(loginRequest);
            return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return an error response
            return new ResponseEntity<>(new AuthResponse(e.getMessage() , false), HttpStatus.UNAUTHORIZED);
        }
    }

//    @GetMapping("/verifyEmail")
//    public String verifyEmail(@RequestParam("token") String token){
//        VerificationToken theToken = userServiceImplementation.findByToken(token);
//        if (theToken.getUser().isEnabled()){
//            return CommonConstant.EXISTING_USER_VERIFICATION_MESSAGE;
//        }
//        String verificationResult = userServiceImplementation.validateToken(token);
//        if (verificationResult.equalsIgnoreCase("valid")){
//            return CommonConstant.VERIFICATION_MESSAGE_C;
//        }
//        return CommonConstant.TOKEN_NOT_VALID;
//    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(
            @RequestBody TwoFAVerificationRequest verificationRequest
    ) {
        return ResponseEntity.ok(userServiceImplementation.verifyCode(verificationRequest));
    }


}
