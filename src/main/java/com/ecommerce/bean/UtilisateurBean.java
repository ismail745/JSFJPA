package com.ecommerce.bean;

import com.ecommerce.model.Panier;
import com.ecommerce.model.Utilisateur;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class UtilisateurBean implements Serializable {
    
    @PersistenceContext(unitName = "EcommercePU")
    private EntityManager em;
    
    private Utilisateur utilisateur = new Utilisateur();
    private Utilisateur utilisateurConnecte;
    private String email;
    private String motDePasse;
    
    @PostConstruct
    public void init() {
        // Initialisation si nécessaire
    }
    
    @Transactional
    public String inscrire() {
        try {
            // Vérifier si l'email existe déjà
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(u) FROM Utilisateur u WHERE u.email = :email", Long.class);
            query.setParameter("email", utilisateur.getEmail());
            
            if (query.getSingleResult() > 0) {
                // Email déjà utilisé
                return "inscription?faces-redirect=true&error=email";
            }
            
            // Créer un panier pour le nouvel utilisateur
            Panier panier = new Panier(utilisateur);
            utilisateur.setPanier(panier);
            
            // Persister l'utilisateur et son panier
            em.persist(utilisateur);
            
            // Connecter l'utilisateur
            utilisateurConnecte = utilisateur;
            
            // Réinitialiser pour la prochaine inscription
            utilisateur = new Utilisateur();
            
            return "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "inscription?faces-redirect=true&error=general";
        }
    }
    
    @Transactional
    public String connexion() {
        try {
            TypedQuery<Utilisateur> query = em.createQuery(
                "SELECT u FROM Utilisateur u WHERE u.email = :email AND u.motDePasse = :motDePasse", 
                Utilisateur.class);
            query.setParameter("email", email);
            query.setParameter("motDePasse", motDePasse);
            
            List<Utilisateur> results = query.getResultList();
            
            if (results.isEmpty()) {
                // Identifiants incorrects
                return "connexion?faces-redirect=true&error=auth";
            }
            
            utilisateurConnecte = results.get(0);
            return "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "connexion?faces-redirect=true&error=general";
        }
    }
    
    public String deconnexion() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        utilisateurConnecte = null;
        return "index?faces-redirect=true";
    }
    
    public boolean isConnecte() {
        return utilisateurConnecte != null;
    }
    
    // Getters et Setters
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public void setUtilisateurConnecte(Utilisateur utilisateurConnecte) {
        this.utilisateurConnecte = utilisateurConnecte;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}