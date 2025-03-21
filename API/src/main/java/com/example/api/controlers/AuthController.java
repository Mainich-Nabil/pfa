package com.example.api.controlers;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jdk.jshell.execution.Util;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.entities.Utilisateur;
import com.example.api.services.ServiceUtilisateure;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ServiceUtilisateure serviceUtilisateur;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ServiceUtilisateure serviceUtilisateure;

    public AuthController(ServiceUtilisateure serviceUtilisateur,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                           ServiceUtilisateure serviceUtilisateure) {
        this.serviceUtilisateur = serviceUtilisateur;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.serviceUtilisateure = serviceUtilisateure;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Utilisateur utilisateur) {
        Map<String, String> response = new HashMap<>();
        try {



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
                response.put("message", "Email cannot be null");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            // Detailed error logging
            System.err.println("Registration error: " + e.getMessage());


            response.put("status","error");
            response.put("message", e.getMessage());
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
            response.put("status", "success");

            Utilisateur utilisateur1 = this.serviceUtilisateure.findByEmail(utilisateur.getEmail());
            response.put("nom", utilisateur1.getNom());
            response.put("prenom", utilisateur1.getPrenom());

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            System.err.println("Login error: " + e.getMessage());
            response.put("status", "error");
            response.put("message", "Mot de passe incorrect, veuillez vérifier le mot de passe ou l'email");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (UsernameNotFoundException e) {
            System.err.println("Login error: " + e.getMessage());
            response.put("status", "error");
            response.put("message", "Utilisateur n'existe pas");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            response.put("status", "error");
            response.put("message", "Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> resp = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            resp.put("status", "success");
        } else {
            resp.put("status", "error");
        }
        return ResponseEntity.ok(resp);
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