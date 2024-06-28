package ru.s3v3nny.endpointwithsql.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class TokenUtil {
    public String generateToken() {
        byte[] randomBytes = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(randomBytes);
        Base64.Encoder base64Encoder = Base64.getEncoder();
        return base64Encoder.encodeToString(randomBytes);
    }
}
