package com.example.api.Repositories;

import com.example.api.entities.EmailEnvoyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailEnvoyerRepository extends JpaRepository<EmailEnvoyer,Integer> {
}
