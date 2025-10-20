package com.ecommerce.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "paniers")
public class Panier implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_creation")
    private Date dateCreation;
    
    @OneToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
    
    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LignePanier> lignesPanier = new ArrayList<>();
    
    // Constructeurs
    public Panier() {
        this.dateCreation = new Date();
    }
    
    public Panier(Utilisateur utilisateur) {
        this();
        this.utilisateur = utilisateur;
    }
    
    // Méthodes utilitaires
    public void ajouterProduit(Produit produit, int quantite) {
        // Vérifier si le produit est déjà dans le panier
        for (LignePanier ligne : lignesPanier) {
            if (ligne.getProduit().getId().equals(produit.getId())) {
                ligne.setQuantite(ligne.getQuantite() + quantite);
                return;
            }
        }
        
        // Si le produit n'est pas dans le panier, ajouter une nouvelle ligne
        LignePanier nouvelleLigne = new LignePanier(this, produit, quantite, produit.getPrix());
        lignesPanier.add(nouvelleLigne);
    }
    
    public void retirerProduit(Produit produit) {
        lignesPanier.removeIf(ligne -> ligne.getProduit().getId().equals(produit.getId()));
    }
    
    public double calculerTotal() {
        return lignesPanier.stream()
                .mapToDouble(ligne -> ligne.getPrixUnitaire() * ligne.getQuantite())
                .sum();
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<LignePanier> getLignesPanier() {
        return lignesPanier;
    }

    public void setLignesPanier(List<LignePanier> lignesPanier) {
        this.lignesPanier = lignesPanier;
    }
}