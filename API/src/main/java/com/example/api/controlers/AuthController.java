package com.example.api.controlers;

import com.example.api.entities.Utilisateur;
import com.example.api.services.ServiceUtilisateure;
import com.example.api.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ServiceUtilisateure serviceUtilisateur;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthController(ServiceUtilisateure serviceUtilisateur,
                          AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil) {
        this.serviceUtilisateur = serviceUtilisateur;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil; // Inject JwtTokenUtil
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Utilisateur user) {
        Map<String, String> response = new HashMap<>();
        try {
            serviceUtilisateur.createUser(user);
            response.put("status", "success");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
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

            // Generate JWT Token using JwtTokenUtil
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);

            Utilisateur foundUser = serviceUtilisateur.findByEmail(utilisateur.getEmail());

            response.put("status", "success");
            response.put("token", token);
            response.put("nom", foundUser.getNom());
            response.put("prenom", foundUser.getPrenom());

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            response.put("status", "error");
            response.put("message", "Mot de passe incorrect, veuillez vérifier le mot de passe ou l'email");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (UsernameNotFoundException e) {
            response.put("status", "error");
            response.put("message", "Utilisateur n'existe pas");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutUser() {
        Map<String, String> response = new HashMap<>();
        try {
            // Clear the security context
            SecurityContextHolder.clearContext();

            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Logout failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/check-session")
    public ResponseEntity<Map<String, String>> checkSession(
            @RequestHeader(name = "Authorization", required = false) String authHeader) {
        Map<String, String> response = new HashMap<>();

        try {
            // Check if Authorization header is present and starts with "Bearer "
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.put("status", "error");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Extract the token by removing "Bearer " prefix
            String token = authHeader.substring(7);

            // Get the current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Validate the token
            if (jwtTokenUtil.validateToken(token, userDetails)) {
                response.put("status", "success");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}