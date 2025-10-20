package com.ecommerce.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "commandes")
public class Commande implements Serializable {
    
    public enum Statut {
        EN_ATTENTE, VALIDEE, EXPEDIEE, LIVREE, ANNULEE
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_commande")
    private Date dateCommande;
    
    @Enumerated(EnumType.STRING)
    private Statut statut;
    
    @Column(name = "montant_total")
    private Double montantTotal;
    
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
    
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneCommande> lignesCommande = new ArrayList<>();
    
    // Constructeurs
    public Commande() {
        this.dateCommande = new Date();
        this.statut = Statut.EN_ATTENTE;
    }
    
    public Commande(Utilisateur utilisateur) {
        this();
        this.utilisateur = utilisateur;
    }
    
    // Méthodes utilitaires
    public void ajouterLigne(Produit produit, int quantite, double prixUnitaire) {
        LigneCommande ligne = new LigneCommande(this, produit, quantite, prixUnitaire);
        lignesCommande.add(ligne);
    }
    
    public void calculerMontantTotal() {
        this.montantTotal = lignesCommande.stream()
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

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<LigneCommande> getLignesCommande() {
        return lignesCommande;
    }

    public void setLignesCommande(List<LigneCommande> lignesCommande) {
        this.lignesCommande = lignesCommande;
    }
    
    @Override
    public String toString() {
        return "Commande{" + "id=" + id + ", dateCommande=" + dateCommande + 
               ", statut=" + statut + ", montantTotal=" + montantTotal + '}';
    }
}