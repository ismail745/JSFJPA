package com.ecommerce.bean;

import com.ecommerce.model.Categorie;
import com.ecommerce.model.Produit;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class VitrineBean implements Serializable {

    @PersistenceContext(unitName = "EcommercePU")
    private EntityManager em;
    
    @Inject
    private PanierBean panierBean;
    
    private List<Produit> produits;
    private List<Categorie> categories;
    private Long categorieSelectionneeId;
    private String motCleRecherche;
    
    @PostConstruct
    public void init() {
        try {
            chargerCategories();
            chargerProduits();
            // Créer des données de test si nécessaire
            if (categories.isEmpty()) {
                creerDonneesDeTest();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void creerDonneesDeTest() {
        try {
            // Créer une catégorie de test
            Categorie cat1 = new Categorie("Électronique", "Produits électroniques");
            em.persist(cat1);
            
            Categorie cat2 = new Categorie("Livres", "Livres et manuels");
            em.persist(cat2);
            
            // Créer des produits de test
            Produit p1 = new Produit("Ordinateur Portable", "Un ordinateur portable performant", 999.99, 10);
            p1.setCategorie(cat1);
            em.persist(p1);
            
            Produit p2 = new Produit("Livre Java", "Manuel d'apprentissage Java", 29.99, 50);
            p2.setCategorie(cat2);
            em.persist(p2);
            
            em.flush();
            // Recharger les données
            chargerCategories();
            chargerProduits();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void chargerProduits() {
        if (categorieSelectionneeId != null) {
            TypedQuery<Produit> query = em.createQuery(
                "SELECT p FROM Produit p WHERE p.categorie.id = :categorieId ORDER BY p.dateAjout DESC", 
                Produit.class);
            query.setParameter("categorieId", categorieSelectionneeId);
            produits = query.getResultList();
        } else if (motCleRecherche != null && !motCleRecherche.trim().isEmpty()) {
            TypedQuery<Produit> query = em.createQuery(
                "SELECT p FROM Produit p WHERE LOWER(p.nom) LIKE :motCle OR LOWER(p.description) LIKE :motCle ORDER BY p.dateAjout DESC", 
                Produit.class);
            query.setParameter("motCle", "%" + motCleRecherche.toLowerCase() + "%");
            produits = query.getResultList();
        } else {
            TypedQuery<Produit> query = em.createQuery(
                "SELECT p FROM Produit p ORDER BY p.dateAjout DESC", 
                Produit.class);
            produits = query.getResultList();
        }
    }
    
    public void chargerCategories() {
        TypedQuery<Categorie> query = em.createQuery(
            "SELECT c FROM Categorie c ORDER BY c.nom", 
            Categorie.class);
        categories = query.getResultList();
    }
    
    public void filtrerParCategorie(Long categorieId) {
        this.categorieSelectionneeId = categorieId;
        chargerProduits();
    }
    
    public void rechercher() {
        this.categorieSelectionneeId = null;
        chargerProduits();
    }
    
    public void ajouterAuPanier(Produit produit) {
        panierBean.ajouterProduit(produit);
    }
    
    public Produit getProduitDetails(Long produitId) {
        return em.find(Produit.class, produitId);
    }
    
    // Getters et Setters
    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }

    public List<Categorie> getCategories() {
        return categories;
    }

    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
    }

    public Long getCategorieSelectionneeId() {
        return categorieSelectionneeId;
    }

    public void setCategorieSelectionneeId(Long categorieSelectionneeId) {
        this.categorieSelectionneeId = categorieSelectionneeId;
    }

    public String getMotCleRecherche() {
        return motCleRecherche;
    }

    public void setMotCleRecherche(String motCleRecherche) {
        this.motCleRecherche = motCleRecherche;
    }
}