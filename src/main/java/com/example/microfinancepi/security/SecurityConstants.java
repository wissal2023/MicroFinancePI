package com.example.microfinancepi.security;

public class SecurityConstants {

    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SECRET_KEY = "hexapod2223";
    public static final long EXPIRATION_TIME = 86400_000; // in millisecond
    private SecurityConstants() {
        throw new IllegalStateException("Utility class");
    }
}
