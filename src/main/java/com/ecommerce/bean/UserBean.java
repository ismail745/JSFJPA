package com.ecommerce.bean;

import com.ecommerce.model.Utilisateur;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.Serializable;

@Named
@RequestScoped
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UtilisateurBean utilisateurBean;

    private String prenom;
    private String nom;
    private String email;
    private String password;

    // Default constructor
    public UserBean() {}

    @Transactional
    public String register() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // Validation basique
        if (prenom == null || prenom.trim().isEmpty()) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Le prénom est obligatoire"));
            return null;
        }

        if (nom == null || nom.trim().isEmpty()) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Le nom est obligatoire"));
            return null;
        }

        if (email == null || email.trim().isEmpty()) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "L'email est obligatoire"));
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Le mot de passe est obligatoire"));
            return null;
        }

        // Créer un utilisateur temporaire et l'inscrire via UtilisateurBean
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPrenom(prenom);
        utilisateur.setNom(nom);
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(password); // Note: En production, hasher le mot de passe

        utilisateurBean.setUtilisateur(utilisateur);
        String result = utilisateurBean.inscrire();

        if (result.contains("error")) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Inscription échouée"));
            return null;
        }

        // Succès
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Inscription réussie!"));
        return "index?faces-redirect=true";
    }

    public String goBack() {
        return "index?faces-redirect=true";
    }

    // Getters et Setters
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
