// Utilitaire pour la gestion des tokens JWT (génération, extraction, validation)
package com.openclassrooms.mddapi.security;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.util.Date;

@Component // Indique que cette classe est un composant Spring
public class JwtUtil {
    @Value("${oc.app.jwtSecret}")
    private String jwtSecret; // Clé secrète pour signer les tokens

    @Value("${oc.app.jwtExpirationMs}")
    private long jwtExpirationMs; // Durée de validité du token en millisecondes

    // Génère un token JWT pour un utilisateur donné
    public String generateToken(String username) {
        Key key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.builder()
                .setSubject(username) // Ajoute le nom d'utilisateur comme sujet du token
                .setIssuedAt(new Date()) // Date de création du token
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Date d'expiration
                .signWith(key, SignatureAlgorithm.HS256) // Signe le token avec la clé secrète
                .compact();
    }

    // Extrait le nom d'utilisateur à partir d'un token JWT
    public String getUsernameFromToken(String token) {
        Key key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Valide un token JWT (signature et expiration)
    public boolean validateJwtToken(String token) {
        try {
            Key key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true; // Le token est valide
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Le token est invalide ou expiré
        }
    }
}