package com.example.api.Repositories;

import com.example.api.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    Contact findByEmail(String email);
    Contact findByEmailAndUtilisateurId(String email, Integer utilisateurId);
}
