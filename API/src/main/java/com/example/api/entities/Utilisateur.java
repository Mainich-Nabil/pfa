package com.example.api.entities;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Utilisateur", nullable = false)
    private Integer id;

    @Column(name = "Nom", length = 50)
    private String nom;

    @Column(name = "Prenom", length = 50)
    private String prenom;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Mot_De_Passe")
    private String motDePasse;

    @OneToMany(mappedBy = "idUtilisateurFk")
    private Set<com.example.api.entities.Contact> contacts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUtilisateurFk")
    private Set<com.example.api.entities.EmailEnvoyer> emailEnvoyers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUtilisateurFk")
    private Set<com.example.api.entities.EmailRecu> emailRecus = new LinkedHashSet<>();

    public Utilisateur(String nom, String prenom, String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    public Utilisateur() {

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Set<com.example.api.entities.Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<com.example.api.entities.Contact> contacts) {
        this.contacts = contacts;
    }

    public Set<com.example.api.entities.EmailEnvoyer> getEmailEnvoyers() {
        return emailEnvoyers;
    }

    public void setEmailEnvoyers(Set<com.example.api.entities.EmailEnvoyer> emailEnvoyers) {
        this.emailEnvoyers = emailEnvoyers;
    }

    public Set<com.example.api.entities.EmailRecu> getEmailRecus() {
        return emailRecus;
    }

    public void setEmailRecus(Set<com.example.api.entities.EmailRecu> emailRecus) {
        this.emailRecus = emailRecus;
    }

    public Utilisateur get() {
        return this;
    }
}