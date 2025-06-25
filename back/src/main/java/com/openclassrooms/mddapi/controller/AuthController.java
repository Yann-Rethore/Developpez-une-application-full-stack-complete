// Contrôleur REST pour la gestion de l'authentification (inscription et connexion)
package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.RegisterRequestDto;
import com.openclassrooms.mddapi.dto.LoginRequestDto;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.UserService;
import com.openclassrooms.mddapi.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController // Indique que cette classe est un contrôleur REST
@RequestMapping("/api/auth") // Préfixe pour tous les endpoints d'authentification
public class AuthController {
    @Autowired
    private UserService userService; // Service pour la gestion des utilisateurs

    @Autowired
    private JwtUtil jwtUtil; // Utilitaire pour la gestion des tokens JWT

    // Endpoint POST /api/auth/register pour inscrire un nouvel utilisateur
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequestDto request) {
        return userService.register(request);
    }

    // Endpoint POST /api/auth/login pour connecter un utilisateur et générer un token JWT
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDto request) {
        User user = userService.authenticate(request.getIdentifier(), request.getPassword());
        String token = jwtUtil.generateToken(user.getUsername());
        if (token == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid credentials");
            return ResponseEntity.status(401).body(error); // Retourne une erreur 401 si les identifiants sont invalides
        }
        Map<String, String> response = new HashMap<>();
        response.put("token", token); // Retourne le token JWT en cas de succès
        return ResponseEntity.ok(response);
    }
}