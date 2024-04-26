package com.askMeProject.service.implementation;

import com.askMeProject.common_constant.CommonConstant;
import com.askMeProject.exception.BusinessException;
import com.askMeProject.exception.ErrorModel;
import com.askMeProject.model.User;
import com.askMeProject.repository.UserRepository;

import com.askMeProject.utils.ApplicationUrl;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public class ForgetPasswordService {

    private OtpManager otpManager;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;




    public ForgetPasswordService(OtpManager otpManager) {
        this.otpManager = otpManager;
    }

    public void initiateForgetPassword(String email) throws MessagingException, UnsupportedEncodingException {
        UserNotFound(email);
        String otp = otpManager.generateOtp(email);
        sendOtpToUser(email, otp);
    }

    public boolean verifyOtp(String identifier, String enteredOtp) {

        return otpManager.verifyOtp(identifier, enteredOtp);
    }

    // Other methods for email verification, password reset, etc.

    private void sendOtpToUser(String email, String otp) throws MessagingException, UnsupportedEncodingException {
        String subject = "OTP for password reset";
        String mailContent = "Your OTP for password reset is: " + otp;

        emailService.sendEmail( email, subject, mailContent);
        System.out.println("Sending OTP to " + email + ": " + otp);

    }
    public String resetPassword(String email,String newPassword) {
        Optional<User> user =userRepository.findByEmail(email);
        if(!user.isPresent()){
            ErrorModel error = new ErrorModel();
            error.setCode(CommonConstant.USER_NOT_FOUND_CODE);
            error.setMessage(CommonConstant.USER_NOT_FOUND);
            throw new BusinessException(error);
        }
        user.get().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user.get());
        return "Password reset successfully";
    }
    public String passwordResetLink(String email,String enteredOtp) throws MessagingException, UnsupportedEncodingException {
        UserNotFound(email);

        String resetPasswordEndpoint = "/forgot-password/reset-password";
        String resetPasswordLink = ApplicationUrl.BASE_URL + resetPasswordEndpoint + "/" + email + "/" + enteredOtp;

        String subject = "Password Reset Link";
        String mailContent = "Please click the link below to reset your password:\n" + resetPasswordLink;
        emailService.sendEmail( email, subject, mailContent);
        return "Password reset link sent successfully";
    }

    private void UserNotFound(String email) {
        Optional<User> user =userRepository.findByEmail(email);
        if(!user.isPresent()){
            ErrorModel error = new ErrorModel();
            error.setCode(CommonConstant.USER_NOT_FOUND_CODE);
            error.setMessage(CommonConstant.USER_NOT_FOUND);
            throw new BusinessException(error);
        }
    }


}


