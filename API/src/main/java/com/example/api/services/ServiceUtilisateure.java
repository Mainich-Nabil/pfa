package com.example.api.services;

import com.example.api.Repositories.UserRepository;
import com.example.api.entities.Utilisateur;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceUtilisateure {
    private UserRepository userRepository;

    @Autowired
    public ServiceUtilisateure(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void createUser(Utilisateur utilisateur) {
        userRepository.save(utilisateur);
    }


}

