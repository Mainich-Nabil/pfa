package com.example.api.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "categorie")  // Ajout du nom de table explicite
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)  // Contraintes ajoutées
    private String nom;

    @Column(length = 500)  // Longueur maximale pour la description
    private String description;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<Contact> contacts = new HashSet<>();

    // Constructeurs
    public Categorie() {
    }

    public Categorie(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    // equals() et hashCode() basés sur l'id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categorie categorie = (Categorie) o;
        return id != null && id.equals(categorie.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // toString() (optionnel mais utile pour le débogage)
    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    // Méthodes utilitaires pour gérer la relation bidirectionnelle
    public void addContact(Contact contact) {
        this.contacts.add(contact);
        contact.getCategories().add(this);
    }

    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.getCategories().remove(this);
    }
}