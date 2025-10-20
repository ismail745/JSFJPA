package com.ecommerce.bean;

import com.ecommerce.model.Commande;
import com.ecommerce.model.LignePanier;
import com.ecommerce.model.Panier;
import com.ecommerce.model.Produit;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.io.Serializable;

@Named
@SessionScoped
public class PanierBean implements Serializable {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private UtilisateurBean utilisateurBean;
    
    private Panier panier;
    
    public void initialiserPanier() {
        if (utilisateurBean.isConnecte()) {
            panier = utilisateurBean.getUtilisateurConnecte().getPanier();
        }
    }
    
    @Transactional
    public String ajouterAuPanier(Long produitId, int quantite) {
        if (!utilisateurBean.isConnecte()) {
            return "connexion?faces-redirect=true";
        }
        
        if (panier == null) {
            initialiserPanier();
        }
        
        try {
            Produit produit = em.find(Produit.class, produitId);
            
            if (produit == null) {
                return "vitrine?faces-redirect=true&error=produit";
            }
            
            // Vérifier si le produit est déjà dans le panier
            boolean produitExiste = false;
            for (LignePanier ligne : panier.getLignesPanier()) {
                if (ligne.getProduit().getId().equals(produitId)) {
                    ligne.setQuantite(ligne.getQuantite() + quantite);
                    produitExiste = true;
                    break;
                }
            }
            
            // Si le produit n'est pas dans le panier, ajouter une nouvelle ligne
            if (!produitExiste) {
                LignePanier nouvelleLigne = new LignePanier(panier, produit, quantite, produit.getPrix());
                panier.getLignesPanier().add(nouvelleLigne);
            }
            
            em.merge(panier);
            
            return "panier?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "vitrine?faces-redirect=true&error=general";
        }
    }
    
    @Transactional
    public String supprimerDuPanier(Long ligneId) {
        try {
            LignePanier ligne = em.find(LignePanier.class, ligneId);
            
            if (ligne != null && ligne.getPanier().getId().equals(panier.getId())) {
                panier.getLignesPanier().remove(ligne);
                em.remove(ligne);
                em.merge(panier);
            }
            
            return "panier?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "panier?faces-redirect=true&error=general";
        }
    }
    
    @Transactional
    public String mettreAJourQuantite(Long ligneId, int quantite) {
        try {
            LignePanier ligne = em.find(LignePanier.class, ligneId);
            
            if (ligne != null && ligne.getPanier().getId().equals(panier.getId())) {
                if (quantite <= 0) {
                    panier.getLignesPanier().remove(ligne);
                    em.remove(ligne);
                } else {
                    ligne.setQuantite(quantite);
                    em.merge(ligne);
                }
                
                em.merge(panier);
            }
            
            return "panier?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "panier?faces-redirect=true&error=general";
        }
    }
    
    @Transactional
    public String validerCommande() {
        if (!utilisateurBean.isConnecte() || panier == null || panier.getLignesPanier().isEmpty()) {
            return "panier?faces-redirect=true&error=empty";
        }
        
        try {
            // Créer une nouvelle commande
            Commande commande = new Commande(utilisateurBean.getUtilisateurConnecte());
            
            // Ajouter les lignes du panier à la commande
            for (LignePanier lignePanier : panier.getLignesPanier()) {
                commande.ajouterLigne(
                    lignePanier.getProduit(), 
                    lignePanier.getQuantite(), 
                    lignePanier.getPrixUnitaire()
                );
            }
            
            // Calculer le montant total
            double montantTotal = panier.getLignesPanier().stream()
                .mapToDouble(ligne -> ligne.getQuantite() * ligne.getPrixUnitaire())
                .sum();
            commande.setMontantTotal(montantTotal);
            
            // Persister la commande
            em.persist(commande);
            
            // Vider le panier
            for (LignePanier ligne : panier.getLignesPanier()) {
                em.remove(ligne);
            }
            panier.getLignesPanier().clear();
            em.merge(panier);
            
            return "confirmation?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "panier?faces-redirect=true&error=general";
        }
    }
    
    public double calculerTotal() {
        if (panier == null || panier.getLignesPanier() == null) {
            return 0;
        }
        
        return panier.getLignesPanier().stream()
            .mapToDouble(ligne -> ligne.getQuantite() * ligne.getPrixUnitaire())
            .sum();
    }
    
    // Getters et Setters
    public Panier getPanier() {
        if (panier == null && utilisateurBean.isConnecte()) {
            initialiserPanier();
        }
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }
    
    @Transactional
    public void ajouterProduit(Produit produit) {
        if (produit == null) {
            return;
        }
        
        if (!utilisateurBean.isConnecte()) {
            // Rediriger vers la page de connexion sera géré par la navigation JSF
            return;
        }
        
        if (panier == null) {
            initialiserPanier();
        }
        
        try {
            // Vérifier si le produit est déjà dans le panier
            boolean produitExiste = false;
            for (LignePanier ligne : panier.getLignesPanier()) {
                if (ligne.getProduit().getId().equals(produit.getId())) {
                    ligne.setQuantite(ligne.getQuantite() + 1); // Ajoute 1 par défaut
                    produitExiste = true;
                    break;
                }
            }
            
            // Si le produit n'est pas dans le panier, ajouter une nouvelle ligne
            if (!produitExiste) {
                LignePanier nouvelleLigne = new LignePanier(panier, produit, 1, produit.getPrix());
                panier.getLignesPanier().add(nouvelleLigne);
            }
            
            em.merge(panier);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}