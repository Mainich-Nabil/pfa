package com.example.api.services;


import com.example.api.DTO.ContactDto;
import com.example.api.Repositories.CategorieRepository;
import com.example.api.entities.Categorie;
import com.example.api.entities.Contact;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ServiceCategorie {
    CategorieRepository categorieRepository;
    ContactDto contactDto = new ContactDto();
    ServiceContact serviceContact;

    @Autowired
    ServiceCategorie(CategorieRepository categorieRepository,
                     ServiceContact serviceContact) {
        this.categorieRepository = categorieRepository;
        this.serviceContact = serviceContact;

    }

    public boolean createCategorie(Categorie cat) {
        if (categorieRepository.existsByNom(cat.getNom())) {
            throw new RuntimeException("Categorie already exists");
        }
        categorieRepository.save(cat);
        return true;
    }

    public boolean deleteCategorie(String nom) {
        if (categorieRepository.existsByNom(nom)) {
            categorieRepository.delete(categorieRepository.findByNom(nom));
            return true;
        }
        return false;
    }

    public boolean updateCategorie(String oldName, Categorie newCat) {
        if (categorieRepository.existsByNom(oldName)) {
            Categorie oldCategorie = categorieRepository.findByNom(oldName);
            categorieRepository.delete(oldCategorie);
            categorieRepository.save(newCat);
            return true;
        }
        return false;

    }

    public List<ContactDto> getContacts(String name) {
        List<Contact> contacts = categorieRepository.findContactsByCategoryName(name);
        List<ContactDto> contactDtos = new ArrayList<>();
        for (Contact contact : contacts) {
            contactDtos.add(contactDto.fromEntity(contact));
        }
        return contactDtos;
    }


    @Transactional
    public boolean addContactToCategorie(String name, Set<ContactDto> contacts) {
        // 1. Add more detailed validation
        if (name == null || !categorieRepository.existsByNom(name)) {
            return false;
        }

        if (contacts == null || contacts.isEmpty()) {
            return false;
        }

        Categorie cat = categorieRepository.findByNom(name);
        if (cat == null) {
            return false;
        }

        if (cat.getContacts() == null) {
            cat.setContacts(new HashSet<>());
        }

        boolean anyadded = false;

        for (ContactDto contactDto : contacts) {
            // 2. Add null check for contactDto
            if (contactDto == null || contactDto.getEmail() == null) {
                continue;
            }

            // 3. Fix the contact comparison logic
            boolean contactExists = false;
            for (Contact existingContact : cat.getContacts()) {
                if (existingContact != null && contactDto.getEmail().equals(existingContact.getEmail())) {
                    contactExists = true;
                    break;
                }
            }

            if (!contactExists) {
                Contact contact = serviceContact.getContactEmail(contactDto.getEmail());

                // 4. Check if contact was found
                if (contact != null) {
                    cat.getContacts().add(contact);

                    // Initialize categories set if null
                    if (contact.getCategories() == null) {
                        contact.setCategories(new HashSet<>());
                    }

                    contact.getCategories().add(cat);
                    anyadded = true;
                }
            }
        }

        if (anyadded) {
            categorieRepository.save(cat);
            try {
                categorieRepository.flush(); // Explicitly flush changes to the database
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return anyadded;
    }
}