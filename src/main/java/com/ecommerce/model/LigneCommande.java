package com.ecommerce.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "lignes_commande")
public class LigneCommande implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;
    
    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;
    
    @Column(nullable = false)
    private Integer quantite;
    
    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;
    
    // Constructeurs
    public LigneCommande() {
    }
    
    public LigneCommande(Commande commande, Produit produit, Integer quantite, Double prixUnitaire) {
        this.commande = commande;
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }
    
    // Méthodes utilitaires
    public Double getTotal() {
        return quantite * prixUnitaire;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
}