package com.example.api.controlers;

import com.example.api.DTO.CategorieDto;
import com.example.api.DTO.ContactDto;
import com.example.api.DTO.categorieUpdateDto;
import com.example.api.entities.Categorie;
import com.example.api.services.ServiceCategorie;
import com.example.api.services.ServiceUtilisateure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Categorie")
public class CategorieController {

    private final ServiceCategorie serviceCategorie;
    private final ServiceUtilisateure serviceUtilisateure;

    @Autowired
    public CategorieController(ServiceCategorie serviceCategorie, ServiceUtilisateure serviceUtilisateure) {
        this.serviceCategorie = serviceCategorie;
        this.serviceUtilisateure = serviceUtilisateure;
    }

    @PostMapping("/add-categorie")
    public ResponseEntity<Map<String,String>> addCategorie(@RequestBody Categorie categorie) {
        Map<String,String> response = new HashMap<>();
        try{
            boolean isadded = serviceUtilisateure.addCategorieToUser(categorie);
            if(isadded){
                response.put("status", "success");
                response.put("message", "Categorie added");
            }else {
                response.put("status", "error");
                response.put("message", "Categorie not added");
            }
        }catch (Exception e){
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update/{oldName}")
    public ResponseEntity<Map<String,String>> updateCategorie(@PathVariable String oldName, @RequestBody categorieUpdateDto categorie) {
        Map<String,String> response = new HashMap<>();
        try {
            boolean isupdated = serviceCategorie.updateCategorie(oldName,categorie);
            if(isupdated){
                response.put("status", "success");
                response.put("message", "Categorie updated");
            }else{
                response.put("status", "error");
                response.put("message", "Categorie not updated");
            }
        }catch (Exception e){
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/delete/{name}")
    public ResponseEntity<Map<String,String>> deleteCategorie(@PathVariable String name) {
        Map<String,String> response = new HashMap<>();
        try {
            boolean isdeleted = serviceCategorie.deleteCategorie(name);
            if(isdeleted){
                response.put("status", "success");
                response.put("message", "Categorie deleted");
            }else{
                response.put("status", "error");
                response.put("message", "Categorie not deleted");
            }
        }catch (Exception e){
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete-cat/{catName}")
    public ResponseEntity<Map<String,String>> deleteCategory(@PathVariable String catName,@RequestBody String conEmail){
        Map<String,String> response = new HashMap<>();
        try{
            boolean deleted = serviceCategorie.deleteContactFromCat(catName,conEmail);
            if(deleted){
                response.put("status", "success");
                response.put("message", "Contact deleted from Categorie");
            }else {
                response.put("status", "error");
                response.put("message", "Contact not deleted from Categorie");
            }
        }catch (Exception e){
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-contacts/{catname}")
    public ResponseEntity<Map<String,String>> addContacts(@PathVariable String catname, @RequestBody ContactDto contact) {
        Map<String,String> response = new HashMap<>();
        try {
            boolean added = serviceCategorie.addContactToCategorie(catname,contact);
            if(added){
                response.put("status", "success");
                response.put("message", "Contact added");
            }else{
                response.put("status", "error");
                response.put("message", "Contact not added");
            }

        }catch (Exception e){
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-categories")
    public ResponseEntity<Set<CategorieDto>> getCategories() {
        try {
            Set<Categorie> categories = serviceUtilisateure.getCategories();
            if (categories == null) {
                return ResponseEntity.ok(new HashSet<>()); // Return empty set instead of null
            }

            Set<CategorieDto> categorieDtos = categories.stream()
                    .map(CategorieDto::fromEntity)
                    .collect(Collectors.toSet());

            return ResponseEntity.ok(categorieDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }



    


}
