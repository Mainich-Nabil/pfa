package com.example.api.services;

import com.example.api.DTO.CategorieDto;
import com.example.api.DTO.ContactDto;
import com.example.api.Repositories.CategorieRepository;
import com.example.api.Repositories.ContactRepository;
import com.example.api.entities.Categorie;
import com.example.api.entities.Contact;
import com.example.api.entities.Utilisateur;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceContact {
    private final ContactRepository contactRepository;
    private final CategorieRepository categorieRepository;
    private final ServiceUtilisateure serviceUtilisateur;

    @Autowired
    public ServiceContact(ContactRepository contactRepository,
                          CategorieRepository categorieRepository
                            , ServiceUtilisateure serviceUtilisateur) {
        this.contactRepository = contactRepository;
        this.categorieRepository = categorieRepository;
        this.serviceUtilisateur = serviceUtilisateur;
    }

    public void AddContact(Contact contact) {
        contactRepository.save(contact);
    }

    public Contact getContactEmail(String email) {
        Utilisateur utilisateur = serviceUtilisateur.getAuthenticatedUser();

        if (utilisateur == null || utilisateur.getContacts() == null) {
            return null;
        }


        Optional<Contact> matchingContact = utilisateur.getContacts().stream()
                .filter(contact -> contact != null
                        && contact.getEmail() != null
                        && contact.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst();

        return matchingContact.orElse(null);
    }



    public boolean addCategorieToContact(String email, String catName){
        Contact contact = getContactEmail(email);
        if (contact!=null){
            contact.getCategories().add(categorieRepository.findByNom(catName));
            contactRepository.save(contact);
            return true;
        }
        return false;
    }

    public Set<ContactDto> getContactByCategorie(String catName) {
        if (catName == null) {
            return Collections.emptySet();
        }

        Utilisateur utilisateur = serviceUtilisateur.getAuthenticatedUser();
        if (utilisateur == null || utilisateur.getContacts() == null) {
            return Collections.emptySet();
        }

        return utilisateur.getContacts().stream()
                .filter(contact -> contact != null && contact.getCategories() != null)
                .filter(contact -> contact.getCategories().stream()
                        .anyMatch(categorie -> catName.equals(categorie.getNom())))
                .map(ContactDto::fromEntity)
                .collect(Collectors.toSet());
    }

}
