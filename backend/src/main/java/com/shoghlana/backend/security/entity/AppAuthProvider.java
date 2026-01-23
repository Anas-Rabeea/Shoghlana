package com.shoghlana.backend.security.entity;

// Authentication Mechanism AuthManager >  authprovider > UserDetailsService > UserDetails
public enum AppAuthProvider {
    EMAIL, // email
    PHONE,
    GOOGLE, // OAuth2
    FACEBOOK; // OAuth2
}
