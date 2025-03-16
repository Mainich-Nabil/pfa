package com.example.api.controlers;

import com.example.api.entities.Utilisateur;
import com.example.api.services.ServiceUtilisateure;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ServiceUtilisateure serviceUtilisateur;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthController(ServiceUtilisateure serviceUtilisateur,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager) {
        this.serviceUtilisateur = serviceUtilisateur;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Utilisateur utilisateur) {
        Map<String, String> response = new HashMap<>();
        if(utilisateur.getEmail() != null){
            Utilisateur newUser = new Utilisateur(
                    utilisateur.getNom(),
                    utilisateur.getPrenom(),
                    utilisateur.getEmail(),
                    passwordEncoder.encode(utilisateur.getMotDePasse())
            );
            serviceUtilisateur.createUser(newUser);
            response.put("status","success");
            return ResponseEntity.ok(response);
        } else {
            response.put("status","error");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Utilisateur utilisateur) {
        Map<String, String> response = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            utilisateur.getEmail(),
                            utilisateur.getMotDePasse()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            response.put("status","success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status","error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutUser() {
        Map<String, String> response = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            SecurityContextHolder.clearContext();
            response.put("status","success");
        } else {
            response.put("status","error");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-session")
    public ResponseEntity<Map<String, String>> checkSession() {
        Map<String, String> response = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            response.put("status","success");
        } else {
            response.put("status","error");
        }
        return ResponseEntity.ok(response);
    }
}