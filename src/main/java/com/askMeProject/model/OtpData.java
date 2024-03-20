package com.askMeProject.model;

import lombok.Getter;


public  class OtpData {
    private static final long OTP_EXPIRATION_TIME_MS = 5 * 60 * 1000;

    @Getter
    private final String otp;
    private final long timestamp;

    public OtpData(String otp, long timestamp) {
        this.otp = otp;
        this.timestamp = timestamp;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - timestamp > OTP_EXPIRATION_TIME_MS;
    }
}
