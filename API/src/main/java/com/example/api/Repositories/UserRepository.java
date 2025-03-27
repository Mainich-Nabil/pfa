package com.example.api.Repositories;


import com.example.api.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Utilisateur, Integer> {
    Utilisateur findByEmail(String email);
    boolean existsByEmail(String email);
}
