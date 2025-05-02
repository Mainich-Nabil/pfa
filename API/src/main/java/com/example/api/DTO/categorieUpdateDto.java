package com.example.api.DTO;

public class categorieUpdateDto {
    String nom;
    String description;
    public categorieUpdateDto(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }
    public String getNom() {
        return nom;
    }
    public String getDescription() {
        return description;
    }

}
