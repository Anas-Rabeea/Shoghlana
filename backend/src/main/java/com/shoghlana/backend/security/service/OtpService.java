package com.shoghlana.backend.security.service;

public interface OtpService {

    boolean verifyOtp(String otp , String incomingPhone);
    void sendPhoneVerificationOtp(String phone);
    String generateOTP(int tokenLength);

}
