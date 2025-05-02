package com.example.api.controlers;

import com.example.api.DTO.ContactDto;
import com.example.api.entities.Contact;
import com.example.api.entities.updaterequest;

import com.example.api.services.ServiceContact;
import com.example.api.services.ServiceUtilisateure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final ServiceUtilisateure serviceUtilisateure;
    private final ServiceContact serviceContact;

    @Autowired
    public ContactController(ServiceUtilisateure serviceUtilisateure, ServiceContact serviceContact) {

        this.serviceUtilisateure = serviceUtilisateure;
        this.serviceContact = serviceContact;
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String,String>> AddContact(@RequestBody Set<Contact> contacts) {
        Map<String,String> response = new HashMap<>();
        try {
            boolean isadded = serviceUtilisateure.addContactsToUser(contacts);
            if (isadded) {
                response.put("status", "success");
                response.put("message", "Contacts added");
            } else {
                response.put("status", "failed");
                response.put("message", "No contacts added (may be duplicates)");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error adding contacts");
            System.err.println(e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String,String>> DeleteContact(@RequestBody Contact contact) {
        Map<String,String> response = new HashMap<>();
        String email = contact.getEmail();
        boolean isdeleted = false;
        try {
            isdeleted = serviceUtilisateure.deleteContactByEmail(email);
            if (isdeleted){
                response.put("status", "success");
                response.put("message", "Contact deleted");
            }else {
                response.put("status", "error");
                response.put("message", "Contact not deleted");
            }

        }catch (Exception e){
            response.put("status", "error");
            System.out.println(e);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String,String>> UpdateContact(@RequestBody Contact contact) {
        Map<String,String> response = new HashMap<>();
        boolean isupdated = false;
        try {
            isupdated = serviceUtilisateure.updateContact(contact);
            if (isupdated){
                response.put("status", "success");
                response.put("message", "Contact updated successfully");
            }else {
                response.put("status", "failed");
                response.put("message", "Failed to update contact");
            }
        }catch (Exception e){
            response.put("status", "error");
            System.out.println(e);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-to-categorie/{conName}")
    public ResponseEntity<Map<String,String>> AddToCategory(@PathVariable String conName, @RequestBody String catname){
        Map<String,String> response = new HashMap<>();
        try {
            boolean isadded = serviceContact.addCategorieToContact(conName,catname);
            if (isadded){
                response.put("status", "success");
                response.put("message", "Categorie added");

            }else{
                response.put("status", "failed");
                response.put("message", "Categorie not added");
            }
        }catch (Exception e){
            response.put("status", "error");
            System.out.println(e);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-contacts")
    public ResponseEntity<Set<ContactDto>> getContacts() {
        Set<Contact> contacts = serviceUtilisateure.getContacts();
        Set<ContactDto> contactDtos = new HashSet<>();

        for (Contact contact : contacts) {
            ContactDto contactDto = ContactDto.fromEntity(contact);
            contactDtos.add(contactDto);

        }

        return ResponseEntity.ok(contactDtos);
    }

    @GetMapping("/get-contacts/{catName}")
    public ResponseEntity<Set<ContactDto>> getContact(@PathVariable String catName){
        Set<ContactDto> res = new HashSet<>();

        try {
            res = serviceContact.getContactByCategorie(catName);
        }catch (Exception e){
            System.out.println("error"+e.getMessage());
        }

        return ResponseEntity.ok(res);
    }





}
