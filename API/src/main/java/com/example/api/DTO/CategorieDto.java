package com.example.api.DTO;

import com.example.api.entities.Categorie;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class CategorieDto {
    private Long id;
    private String nom;
    private String description;

    @JsonIgnoreProperties("categories")
    private Set<ContactDto> contacts;


    public static CategorieDto fromEntity(Categorie categorie) {
        return fromEntity(categorie, true);
    }

    public static CategorieDto fromEntity(Categorie categorie, boolean includeContacts) {
        CategorieDto dto = new CategorieDto();
        dto.setId(categorie.getId());
        dto.setNom(categorie.getNom());
        dto.setDescription(categorie.getDescription());


        if (includeContacts && categorie.getContacts() != null) {
            dto.setContacts(categorie.getContacts().stream()
                    .map(contact -> ContactDto.fromEntity(contact, false))
                    .collect(Collectors.toSet())
            );
        }
        return dto;
    }
}