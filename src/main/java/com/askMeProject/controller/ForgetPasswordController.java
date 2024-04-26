package com.askMeProject.controller;


import com.askMeProject.service.implementation.ForgetPasswordService;
import com.askMeProject.service.implementation.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class ForgetPasswordController {
    private  final UserServiceImplementation userServiceImplementation;

    private final ForgetPasswordService forgetPasswordService;

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email) {
        try {
            forgetPasswordService.initiateForgetPassword(email);
            return "OTP sent successfully";
        } catch (Exception e) {
            return "Error sending OTP: " + e.getMessage();
        }
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email, @RequestParam String enteredOtp) {
        try {
            if (forgetPasswordService.verifyOtp(email, enteredOtp)) {
                forgetPasswordService.passwordResetLink(email, enteredOtp);
                return "OTP verified successfully, and password reset link sent to email";
            } else {
                return "Invalid OTP";
            }
        } catch (Exception e) {
            return "Error verifying OTP: " + e.getMessage();
        }
    }

    @PostMapping("/reset-password/{email}")
    public String resetPassword(@PathVariable String email, @RequestParam String newPassword) {
        try {
            forgetPasswordService.resetPassword(email,newPassword);
            return "Password reset successfully";
        } catch (Exception e) {
            return "Error resetting password: " + e.getMessage();
        }
    }


}
