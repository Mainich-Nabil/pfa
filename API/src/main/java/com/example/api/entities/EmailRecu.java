package com.example.api.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "email_recu")
public class EmailRecu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Email_R", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Id_Utilisateur_FK")
    private com.example.api.entities.Utilisateur idUtilisateurFk;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Id_Contact_FK")
    private com.example.api.entities.Contact idContactFk;

    @Lob
    @Column(name = "Contenu")
    private String contenu;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public com.example.api.entities.Utilisateur getIdUtilisateurFk() {
        return idUtilisateurFk;
    }

    public void setIdUtilisateurFk(com.example.api.entities.Utilisateur idUtilisateurFk) {
        this.idUtilisateurFk = idUtilisateurFk;
    }

    public com.example.api.entities.Contact getIdContactFk() {
        return idContactFk;
    }

    public void setIdContactFk(com.example.api.entities.Contact idContactFk) {
        this.idContactFk = idContactFk;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

}