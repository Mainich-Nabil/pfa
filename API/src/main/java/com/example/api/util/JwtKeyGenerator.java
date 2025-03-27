package com.example.api.util;

import io.jsonwebtoken.security.Keys;
import java.util.Base64;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        var secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        byte[] encodedKey = secretKey.getEncoded();
        System.out.println("Clé secrète générée : " + Base64.getEncoder().encodeToString(encodedKey));
        System.out.println("Longueur de la clé : " + encodedKey.length);
    }
}
