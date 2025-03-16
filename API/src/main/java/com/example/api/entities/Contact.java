package com.example.api.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Contact", nullable = false)
    private Integer id;

    @Column(name = "Email", length = 100)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "Id_Cat_FK")
    private com.example.api.entities.Categorie idCatFk;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Id_Utilisateur_FK")
    private com.example.api.entities.Utilisateur idUtilisateurFk;

    @OneToMany(mappedBy = "idContactFk")
    private Set<com.example.api.entities.EmailEnvoyer> emailEnvoyers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idContactFk")
    private Set<com.example.api.entities.EmailRecu> emailRecus = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public com.example.api.entities.Categorie getIdCatFk() {
        return idCatFk;
    }

    public void setIdCatFk(com.example.api.entities.Categorie idCatFk) {
        this.idCatFk = idCatFk;
    }

    public com.example.api.entities.Utilisateur getIdUtilisateurFk() {
        return idUtilisateurFk;
    }

    public void setIdUtilisateurFk(com.example.api.entities.Utilisateur idUtilisateurFk) {
        this.idUtilisateurFk = idUtilisateurFk;
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

}