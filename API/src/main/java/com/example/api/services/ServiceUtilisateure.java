package com.example.api.services;

import com.example.api.Repositories.UserRepository;
import com.example.api.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceUtilisateure {
    private final UserRepository userRepository;

    @Autowired
    public ServiceUtilisateure(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(Utilisateur utilisateur) {
        System.out.println("Saving user: " + utilisateur.getEmail());
        userRepository.save(utilisateur);
    }
}
