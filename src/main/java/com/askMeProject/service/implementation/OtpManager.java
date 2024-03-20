package com.askMeProject.service.implementation;


import com.askMeProject.model.OtpData;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service

public class OtpManager {
//    private static final long OTP_EXPIRATION_TIME_MS = 5 * 60 * 1000; // 5 minutes
    private Map<String, OtpData> otpDataMap = new HashMap<>();

    public String generateOtp(String identifier) {
        String otp = generateRandomOtp();
        otpDataMap.put(identifier, new OtpData(otp, System.currentTimeMillis()));
        return otp;
    }

    public boolean verifyOtp(String identifier, String enteredOtp) {
        OtpData otpData = otpDataMap.get(identifier);
        if (otpData != null && !otpData.isExpired() && otpData.getOtp().equals(enteredOtp)) {
            otpDataMap.remove(identifier); // Remove the OTP after successful verification
            return true;
        }
        return false;
    }

    private String generateRandomOtp() {
       String otp = UUID.randomUUID().toString();
        return otp;
    }


}

