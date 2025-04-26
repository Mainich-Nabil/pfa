package com.example.api.Repositories;

import com.example.api.DTO.ContactDto;
import com.example.api.entities.Categorie;
import com.example.api.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    @Query("SELECT c FROM Contact c JOIN c.categories cat WHERE cat.nom = :name")
    List<Contact> findContactsByCategoryName(String name);

    boolean existsByNom(String name);
    Categorie findByNom(String name);
}
