package com.example.api.controlers;

import com.example.api.DTO.ContactDto;
import com.example.api.entities.Categorie;
import com.example.api.services.ServiceCategorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/Categorie")
public class CategorieController {

    private final ServiceCategorie serviceCategorie;

    @Autowired
    public CategorieController(ServiceCategorie serviceCategorie) {
        this.serviceCategorie = serviceCategorie;
    }

    @PostMapping("/add-categorie")
    public ResponseEntity<Map<String,String>> addCategorie(@RequestBody Categorie categorie) {
        Map<String,String> response = new HashMap<>();
        try{
            boolean isadded = serviceCategorie.createCategorie(categorie);
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
    public ResponseEntity<Map<String,String>> updateCategorie(@PathVariable String oldName, @RequestBody Categorie categorie) {
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

    @PostMapping("/delete/{name}")
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

    @PostMapping("/add-contacts/{catname}")
    public ResponseEntity<Map<String,String>> addContacts(@PathVariable String catname, @RequestBody Set<ContactDto> contact) {
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

    


}
