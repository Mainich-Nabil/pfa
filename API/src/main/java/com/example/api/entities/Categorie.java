package com.example.api.entities;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "categorie")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Cat", nullable = false)
    private Integer id;

    @Column(name = "Nom_Cat", length = 50)
    private String nomCat;

    @Lob
    @Column(name = "Description")
    private String description;

    @OneToMany(mappedBy = "idCatFk")
    private Set<com.example.api.entities.Contact> contacts = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomCat() {
        return nomCat;
    }

    public void setNomCat(String nomCat) {
        this.nomCat = nomCat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<com.example.api.entities.Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<com.example.api.entities.Contact> contacts) {
        this.contacts = contacts;
    }

}