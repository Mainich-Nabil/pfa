package com.example.api.controlers;

import com.example.api.entities.Contact;
import com.example.api.entities.updaterequest;

import com.example.api.services.ServiceUtilisateure;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final ServiceUtilisateure serviceUtilisateure;

    public ContactController(ServiceUtilisateure serviceUtilisateure) {

        this.serviceUtilisateure = serviceUtilisateure;
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
    public ResponseEntity<Map<String,String>> DeleteContact(@RequestBody String email) {
        Map<String,String> response = new HashMap<>();
        boolean isdeleted = false;
        try {
            isdeleted = serviceUtilisateure.deleteContactByEmail(email);
            if (isdeleted){
                response.put("status", "success");
            }else {
                response.put("status", "error");
            }

        }catch (Exception e){
            response.put("status", "error");
            System.out.println(e);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String,String>> UpdateContact(@RequestBody updaterequest request) {
        Map<String,String> response = new HashMap<>();
        boolean isupdated = false;
        try {
            isupdated = serviceUtilisateure.updateContact(request.getEmail(),request.getContact());
            if (isupdated){
                response.put("status", "success");
            }else {
                response.put("status", "failed");
            }
        }catch (Exception e){
            response.put("status", "error");
            System.out.println(e);
        }
        return ResponseEntity.ok(response);
    }

}
