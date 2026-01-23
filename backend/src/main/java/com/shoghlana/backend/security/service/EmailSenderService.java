package com.shoghlana.backend.security.service;

public interface EmailSenderService {
    void send(String to , String content , String title);
}
