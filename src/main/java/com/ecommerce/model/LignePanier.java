package com.ecommerce.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "lignes_panier")
public class LignePanier implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "panier_id")
    private Panier panier;
    
    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;
    
    @Column(nullable = false)
    private Integer quantite;
    
    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;
    
    // Constructeurs
    public LignePanier() {
    }
    
    public LignePanier(Panier panier, Produit produit, Integer quantite, Double prixUnitaire) {
        this.panier = panier;
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

    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
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