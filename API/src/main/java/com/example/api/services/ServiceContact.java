package com.example.api.services;

import com.example.api.Repositories.ContactRepository;
import com.example.api.entities.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceContact {
    private final ContactRepository contactRepository;

    @Autowired
    public ServiceContact(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void AddContact(Contact contact) {
        contactRepository.save(contact);
    }
    public Contact getContactEmail(String email) {
        return contactRepository.findByEmail(email);
    }
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
}
