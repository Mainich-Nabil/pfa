package com.example.api.DTO;

import com.example.api.entities.Contact;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ContactDto {
    private Long idContact;
    private String firstName;
    private String lastName;
    private String email;

    @JsonIgnoreProperties("contacts")
    private Set<CategorieDto> categories;

    // Method to convert a Contact entity to DTO
    public static ContactDto fromEntity(Contact contact) {
        return fromEntity(contact, true);
    }

    // Overloaded method with a flag to control category conversion
    public static ContactDto fromEntity(Contact contact, boolean includeCategories) {
        ContactDto dto = new ContactDto();
        dto.setIdContact(contact.getIdContact());
        dto.setFirstName(contact.getFirstName());
        dto.setLastName(contact.getLastName());
        dto.setEmail(contact.getEmail());

        // Only convert categories if requested and they exist
        if (includeCategories && contact.getCategories() != null) {
            dto.setCategories(contact.getCategories().stream()
                    .map(categorie -> CategorieDto.fromEntity(categorie, false))
                    .collect(Collectors.toSet())
            );
        }
        return dto;
    }
}
