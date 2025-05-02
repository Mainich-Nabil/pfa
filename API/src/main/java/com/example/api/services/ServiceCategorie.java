package com.example.api.services;


import com.example.api.DTO.ContactDto;
import com.example.api.DTO.categorieUpdateDto;
import com.example.api.Repositories.CategorieRepository;
import com.example.api.Repositories.ContactRepository;
import com.example.api.entities.Categorie;
import com.example.api.entities.Contact;
import com.example.api.entities.Utilisateur;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ServiceCategorie {

    private final ContactRepository contactRepository;
    CategorieRepository categorieRepository;
    ContactDto contactDto = new ContactDto();
    ServiceContact serviceContact;
    ServiceUtilisateure serviceUtilisateur;

    @Autowired
    ServiceCategorie(CategorieRepository categorieRepository,
                     ServiceContact serviceContact, ServiceUtilisateure serviceUtilisateur, ContactRepository contactRepository) {
        this.categorieRepository = categorieRepository;
        this.serviceContact = serviceContact;
        this.serviceUtilisateur = serviceUtilisateur;
        this.contactRepository = contactRepository;
    }

    public boolean createCategorie(Categorie cat) {
        if (categorieRepository.existsByNom(cat.getNom())) {
            throw new RuntimeException("Categorie already exists");
        }
        categorieRepository.save(cat);
        return true;
    }

    public boolean deleteCategorie(String nom) {
        Utilisateur utilisateur = serviceUtilisateur.getAuthenticatedUser();
        Optional<Categorie> categorie = utilisateur.getCategories().stream()
                .filter(cat -> cat.getNom().equals(nom))
                .findFirst();
        if (categorie.isEmpty()){
            return false;
        }
        Categorie cat = categorie.get();

        Set<Contact> contacts = utilisateur.getContacts();
        for (Contact contact : contacts) {
            contact.getCategories().remove(cat);
        }

        utilisateur.getCategories().remove(cat);
        serviceUtilisateur.save(utilisateur);


        categorieRepository.delete(cat);
        categorieRepository.flush();
        return true;


    }

    public boolean deleteContactFromCat(String catName,String conEmail){
        Utilisateur utilisateur = serviceUtilisateur.getAuthenticatedUser();
        Optional<Categorie> categorie =  utilisateur.getCategories().stream()
                .filter(cat -> cat.getNom().equals(catName))
                .findFirst();
        if (categorie.isEmpty()){
            return false;
        }
        Categorie cat = categorie.get();
        Optional<Contact> contact =  utilisateur.getContacts().stream()
                .filter(con -> con.getEmail().equals(conEmail))
                .findFirst();
        if (contact.isEmpty()){
            return false;
        }
        Contact con = contact.get();
        cat.getContacts().remove(con);
        con.getCategories().remove(cat);

        contactRepository.save(con);
        categorieRepository.save(cat);

        categorieRepository.flush();
        return true;

    }

    public boolean updateCategorie(String oldName, categorieUpdateDto dto) {
        Utilisateur utilisateur = serviceUtilisateur.getAuthenticatedUser();
        Optional<Categorie> cat = utilisateur.getCategories().stream()
                .filter(categorie -> oldName.equals(categorie.getNom()))
                .findFirst();
        if (cat.isEmpty()){
            return false;
        }
        Categorie categorie = cat.get();
        categorie.setNom(dto.getNom());
        categorie.setDescription(dto.getDescription());
        categorieRepository.save(categorie);
        categorieRepository.flush();
        return true;
    }



    @Transactional
    public boolean addContactToCategorie(String name, ContactDto contactDto) {


        Utilisateur utilisateur = serviceUtilisateur.getAuthenticatedUser();


        Optional<Categorie> catOptional = utilisateur.getCategories().stream()

                .filter(c -> name.equals(c.getNom()))
                .findFirst();

        if (catOptional.isEmpty()) {

            return false;
        }

        Categorie cat = catOptional.get();

        if (cat.getContacts() == null) {

            cat.setContacts(new HashSet<>());
        }

        boolean contactExists = false;
        for (Contact existingContact : cat.getContacts()) {
            if (existingContact != null && existingContact.getEmail() != null
                    && existingContact.getEmail().equals(contactDto.getEmail())) {

                contactExists = true;
                break;
            }
        }

        if (contactExists) {
            return false;
        }



        Contact contact = null;
        try {
            contact = serviceContact.getContactEmail(contactDto.getEmail());
        } catch (Exception e) {


            return false;
        }

        if (contact == null) {

            return false;
        }



        cat.getContacts().add(contact);
        if (contact.getCategories() == null) {
            contact.setCategories(new HashSet<>());
        }

        contact.getCategories().add(cat);

        try {
            categorieRepository.save(cat);

            return true;
        } catch (Exception e) {


            return false;
        }
    }

}