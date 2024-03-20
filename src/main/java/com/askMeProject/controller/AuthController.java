package com.askMeProject.controller;

import com.askMeProject.common_constant.CommonConstant;
import com.askMeProject.dto.LoginRequest;
import com.askMeProject.dto.PasswordRequestUtil;
import com.askMeProject.dto.UserDTO;
import com.askMeProject.dto.response.LoginResponse;
import com.askMeProject.model.User;
import com.askMeProject.model.VerificationToken;
import com.askMeProject.service.impl.AuthServiceImplementation;
import io.swagger.annotations.ApiOperation;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServiceImplementation authServiceImplementation;



    @ApiOperation(value = "register", notes = "This method is used for user registration")

    @PostMapping("/register-patient")
    public ResponseEntity<UserDTO> registerPatient(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request){
        userDTO = authServiceImplementation.registerPatient(userDTO, request);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/register-doctor")
    public ResponseEntity<UserDTO> registerDoctor(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request){
        userDTO = authServiceImplementation.registerDoctor(userDTO, request);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }


    @PostMapping(path = "/login", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse login  = authServiceImplementation.login(loginRequest.getEmail(), loginRequest.getPassword());
        return new ResponseEntity<>(login, HttpStatus.OK);
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        VerificationToken theToken = authServiceImplementation.findByToken(token);
        if (theToken.getUser().isEnabled()){
            return CommonConstant.EXISTING_USER_VERIFICATION_MESSAGE;
        }
        String verificationResult = authServiceImplementation.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            return CommonConstant.VERIFICATION_MESSAGE_C;
        }
        return CommonConstant.TOKEN_NOT_VALID;
    }

    @PostMapping("/password-reset-request")
    public String resetPasswordRequest(@RequestBody PasswordRequestUtil passwordRequestUtil,
                                       final HttpServletRequest servletRequest)
            throws MessagingException, UnsupportedEncodingException {

        Optional<User> user = authServiceImplementation.findByEmail(passwordRequestUtil.getEmail());
        String passwordResetUrl = "";
        if (user.isPresent()) {
            String passwordResetToken = UUID.randomUUID().toString();
            authServiceImplementation.createPasswordResetTokenForUser(user.get(), passwordResetToken);
            passwordResetUrl = passwordResetEmailLink(user.get(), applicationUrl(servletRequest), passwordResetToken);
        }
        return passwordResetUrl;
    }

    private String passwordResetEmailLink(User user, String applicationUrl,
                                          String passwordToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl+"/register/reset-password?token="+passwordToken;
      //  eventListener.sendPasswordResetVerificationEmail(url);
       // log.info("Click the link to reset your password :  {}", url);
        return url;
    }
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordRequestUtil passwordRequestUtil,
                                @RequestParam("token") String token){
        String tokenVerificationResult = authServiceImplementation.validatePasswordResetToken(token);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")) {
            return "Invalid token password reset token";
        }
        Optional<User> theUser = Optional.ofNullable(authServiceImplementation.findUserByPasswordToken(token));
        if (theUser.isPresent()) {
            authServiceImplementation.changePassword(theUser.get(), passwordRequestUtil.getNewPassword());
            return "Password has been reset successfully";
        }
        return "Invalid password reset token";
    }
    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"
                +request.getServerPort()+request.getContextPath();
    }

}
