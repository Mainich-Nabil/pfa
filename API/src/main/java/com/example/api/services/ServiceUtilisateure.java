package com.example.api.services;

import com.example.api.Repositories.CategorieRepository;
import com.example.api.Repositories.ContactRepository;
import com.example.api.Repositories.UserRepository;
import com.example.api.entities.Categorie;
import com.example.api.entities.Contact;
import com.example.api.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ServiceUtilisateure {
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategorieRepository categorieRepository;


    @Autowired
    public ServiceUtilisateure(UserRepository userRepository, ContactRepository contactRepository, PasswordEncoder passwordEncoder, CategorieRepository categorieRepository) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.passwordEncoder = passwordEncoder;
        this.categorieRepository = categorieRepository;
    }

    public void save(Utilisateur utilisateur) {
        userRepository.save(utilisateur);
    }

    public void createUser(Utilisateur utilisateur) {
        if (userRepository.existsByEmail(utilisateur.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        String encodedPassword = passwordEncoder.encode(utilisateur.getMotDePasse());
        utilisateur.setMotDePasse(encodedPassword);
        userRepository.save(utilisateur);


    }

    public Utilisateur findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public Set<Contact> getContacts() {
        Utilisateur utilisateur = getAuthenticatedUser();
        if (utilisateur != null) {
            return utilisateur.getContacts();
        }
        return null;
    }

    public Set<Categorie> getCategories(){
        Utilisateur utilisateur = getAuthenticatedUser();
        if (utilisateur != null) {
            return utilisateur.getCategories();
        }
        return null;
    }

    public boolean addContactsToUser(Set<Contact> contacts) {
        Utilisateur utilisateur = getAuthenticatedUser();
        if (utilisateur == null || contacts == null) {
            return false;
        }

        Set<Contact> userContacts = utilisateur.getContacts();
        if (userContacts == null) {
            userContacts = new HashSet<>();
            utilisateur.setContacts(userContacts);
        }

        boolean anyAdded = false;
        for (Contact contact : contacts) {
            boolean emailExists = false;
            for (Contact existing : userContacts) {
                if (existing.getEmail().equals(contact.getEmail())) {
                    emailExists = true;
                    break;
                }
            }
            if (!emailExists) {
                contact.setUtilisateur(utilisateur);
                userContacts.add(contact);
                anyAdded = true;
            }
        }

        if (anyAdded) {
            userRepository.save(utilisateur);
        }
        return anyAdded;
    }

    public boolean deleteContactByEmail(String email) {
        Utilisateur utilisateur = getAuthenticatedUser();
        if (utilisateur != null) {
            Contact contact = contactRepository.findByEmailAndUtilisateurId(email,utilisateur.getId());
            if (contact != null) {
                utilisateur.getContacts().remove(contact);
                userRepository.save(utilisateur);
                return true;
            }
            return false;
        }
        return false;
    }





    public boolean updateContact(Contact newContact) {
        Utilisateur utilisateur = getAuthenticatedUser();
        if (utilisateur != null) {
            Contact contact = contactRepository.findByEmail(newContact.getEmail());
            if (contact != null) {
                contact.setEmail(newContact.getEmail());
                contact.setFirstName(newContact.getFirstName());
                contact.setLastName(newContact.getLastName());
                contactRepository.save(contact);
                return true ;
            }
            return false;

        }
        return false;
    }

    public boolean updateCategorie(Categorie newCategorie,String oldNom) {
        Utilisateur utilisateur = getAuthenticatedUser();
        if (utilisateur != null) {
            Categorie categorie = categorieRepository.findByNom(oldNom);
            if (categorie != null) {
                categorie.setNom(newCategorie.getNom());
                categorie.setDescription(newCategorie.getDescription());
                categorieRepository.save(categorie);
                return true ;
            }
            return false;

        }
        return false;
    }

    public boolean addCategorieToUser(Categorie categorie) {
        Utilisateur utilisateur = getAuthenticatedUser();
        if (utilisateur == null || categorie == null) {
            return false;
        }

        Set<Categorie> userCategories = utilisateur.getCategories();
        if (userCategories == null) {
            userCategories = new HashSet<>();
            utilisateur.setCategories(userCategories);
        }

        boolean anyAdded = false;
        boolean categoryExists = false;  // Renamed from emailExists for clarity

        for (Categorie existing : userCategories) {
            if (existing.getNom().equals(categorie.getNom())) {
                categoryExists = true;
                break;
            }
        }

        if (!categoryExists) {
            categorie.setUtilisateur(utilisateur);
            userCategories.add(categorie);
            anyAdded = true;
        }

        if (anyAdded) {
            userRepository.save(utilisateur);
        }

        return anyAdded;
    }


    public Utilisateur getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Early check for anonymous user
        if (authentication == null || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        String email = null;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        if (email != null && !"anonymousUser".equals(email)) {
            return userRepository.findByEmail(email);
        }

        return null;
    }



}
