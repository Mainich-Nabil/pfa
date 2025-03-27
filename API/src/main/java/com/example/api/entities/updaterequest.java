package com.example.api.entities;

public class updaterequest {
    private String email;
    private Contact contact;
    public updaterequest(String email, Contact contact) {
        this.email = email;
        this.contact = contact;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Contact getContact() {
        return contact;
    }
    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
