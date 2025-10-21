# Application E-commerce - JSF & JPA

## Description du Projet

Cette application est une plateforme de commerce électronique développée avec les technologies Java EE modernes, utilisant JSF (JavaServer Faces) pour l'interface utilisateur et JPA (Java Persistence API) pour la gestion des données. L'application offre une expérience complète de shopping en ligne avec gestion des utilisateurs, catalogue de produits, panier d'achat et système de commandes.

## Architecture et Technologies

### Technologies Utilisées

- **Framework Web** : JSF 4.0 (Jakarta Server Faces)
- **Persistance** : JPA 3.0 avec EclipseLink
- **Base de Données** : MySQL 8.0
- **Interface Utilisateur** : PrimeFaces 15.0 (composants JSF avancés)
- **Injection de Dépendances** : CDI 3.0 (Contexts and Dependency Injection)
- **Serveur d'Application** : Compatible Jakarta EE 9.1
- **Build Tool** : Maven

### Structure du Projet

```
src/
├── main/
│   ├── java/com/ecommerce/
│   │   ├── model/          # Entités JPA
│   │   │   ├── Produit.java
│   │   │   ├── Categorie.java
│   │   │   ├── Utilisateur.java
│   │   │   ├── Panier.java
│   │   │   ├── Commande.java
│   │   │   ├── LignePanier.java
│   │   │   └── LigneCommande.java
│   │   ├── bean/           # Managed Beans CDI
│   │   │   ├── VitrineBean.java
│   │   │   ├── PanierBean.java
│   │   │   └── UtilisateurBean.java
│   │   └── resources/
│   │       └── META-INF/persistence.xml
│   └── webapp/             # Pages JSF
│       ├── index.xhtml     # Page d'accueil
│       ├── vitrine.xhtml   # Catalogue produits
│       ├── panier.xhtml     # Panier d'achat
│       ├── connexion.xhtml # Authentification
│       └── inscription.xhtml # Inscription utilisateur
└── target/                 # Répertoire de build
```

## Modèle de Données

### Entités JPA

#### 1. Utilisateur
- **Champs** : id, nom, prenom, email, motDePasse, adresse, telephone, dateInscription
- **Relations** : Un utilisateur peut avoir plusieurs commandes et possède un panier

#### 2. Produit
- **Champs** : id, nom, description, prix, stock, image, dateAjout
- **Relations** : Appartient à une catégorie, présent dans plusieurs lignes de panier/commande

#### 3. Categorie
- **Champs** : id, nom, description
- **Relations** : Contient plusieurs produits

#### 4. Panier
- **Champs** : id, dateCreation
- **Relations** : Appartient à un utilisateur, contient plusieurs lignes de panier

#### 5. Commande
- **Champs** : id, dateCommande, statut, montantTotal
- **Relations** : Appartient à un utilisateur, contient plusieurs lignes de commande
- **Statuts** : EN_ATTENTE, VALIDEE, EXPEDIEE, LIVREE, ANNULEE

#### 6. LignePanier & LigneCommande
- **Champs** : id, quantite, prixUnitaire
- **Relations** : Référence un produit et appartient à un panier/commande

## Fonctionnalités

### Fonctionnalités Principales

#### 1. Gestion du Catalogue
- **Affichage des produits** avec système de pagination
- **Filtrage par catégorie** pour une navigation facilitée
- **Recherche par mots-clés** dans les noms et descriptions
- **Tri par date d'ajout** (produits les plus récents en premier)

#### 2. Gestion du Panier
- **Ajout/Suppression de produits** avec gestion des quantités
- **Calcul automatique du total** en temps réel
- **Persistance du panier** par utilisateur connecté

#### 3. Système d'Authentification
- **Inscription de nouveaux utilisateurs**
- **Connexion/Déconnexion sécurisée**
- **Gestion des sessions utilisateur**

#### 4. Gestion des Commandes
- **Validation du panier** en commande
- **Suivi des statuts** de commande
- **Historique des commandes** par utilisateur

### Interfaces Utilisateur

#### Pages JSF

1. **index.xhtml** - Page d'accueil avec présentation générale
2. **vitrine.xhtml** - Catalogue des produits avec filtres et recherche
3. **panier.xhtml** - Gestion du panier d'achat
4. **connexion.xhtml** - Formulaire de connexion
5. **inscription.xhtml** - Formulaire d'inscription

## Configuration

### Base de Données

L'application utilise MySQL avec la configuration suivante dans `persistence.xml` :

```xml
<persistence-unit name="MyEcommerceDS" transaction-type="JTA">
    <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
    <jta-data-source>java:jboss/datasources/MyEcommerceDS</jta-data-source>
    <class>com.ecommerce.model.Produit</class>
    <class>com.ecommerce.model.Categorie</class>
    <class>com.ecommerce.model.Utilisateur</class>
    <class>com.ecommerce.model.Panier</class>
    <class>com.ecommerce.model.Commande</class>
    <class>com.ecommerce.model.LignePanier</class>
    <class>com.ecommerce.model.LigneCommande</class>

    <properties>
        <property name="jakarta.persistence.schema-generation.database.action" value="create"/>
        <property name="eclipselink.logging.level" value="FINE"/>
        <property name="eclipselink.logging.parameters" value="true"/>
    </properties>
</persistence-unit>
```

### Dépendances Maven

Les principales dépendances définies dans `pom.xml` :

- **JSF 4.0** : Interface utilisateur Jakarta
- **JPA 3.0** : API de persistance
- **EclipseLink 4.0** : Implémentation JPA
- **MySQL Connector 8.0** : Driver base de données
- **PrimeFaces 15.0** : Composants UI avancés
- **CDI 3.0** : Injection de dépendances

## Guide d'Installation et Déploiement

### Prérequis

1. **JDK 11** ou supérieur
2. **Serveur Jakarta EE compatible** (WildFly, Payara, GlassFish)
3. **MySQL 8.0** ou supérieur
4. **Maven 3.6** ou supérieur

### Étapes d'Installation

1. **Cloner le projet**
   ```bash
   git clone <url-du-projet>
   cd ecommerce-jsf-jpa
   ```

2. **Configurer la base de données**
   - Créer une base de données MySQL nommée `ecommerce`
   - Configurer le datasource dans le serveur d'application

3. **Compiler le projet**
   ```bash
   mvn clean compile
   ```

4. **Packager l'application**
   ```bash
   mvn package
   ```

5. **Déployer sur le serveur**
   - Déployer le fichier `target/ecommerce-jsf-jpa.war`
   - Configurer le datasource JNDI

## Guide d'Utilisation

### Pour un Administrateur

1. **Accéder à l'application** via l'URL du serveur
2. **S'inscrire** comme nouvel utilisateur si nécessaire
3. **Naviguer dans le catalogue** pour voir les produits
4. **Ajouter des produits au panier**
5. **Valider la commande** pour finaliser l'achat

### Pour un Développeur

1. **Structure des Beans CDI** :
   - `VitrineBean` : Gestion du catalogue et recherche
   - `PanierBean` : Gestion du panier d'achat
   - `UtilisateurBean` : Gestion de l'authentification

2. **Ajout de nouvelles entités** :
   - Créer la classe dans `com.ecommerce.model`
   - Ajouter l'entité dans `persistence.xml`
   - Créer le bean CDI correspondant

## Architecture Technique

### Patterns Utilisés

- **MVC (Model-View-Controller)** : Séparation claire entre données, présentation et logique
- **DAO Pattern** : Encapsulation de l'accès aux données via EntityManager
- **Session Management** : Gestion des sessions utilisateur avec CDI
- **Transaction Management** : Gestion des transactions JPA

### Sécurité

- **Authentification par session** CDI
- **Gestion des rôles utilisateur** (client/admin potentiel)
- **Protection CSRF** via JSF intégré

## Tests et Débogage

### Logging

L'application utilise EclipseLink avec niveau de logging `FINE` pour le débogage JPA :

```xml
<property name="eclipselink.logging.level" value="FINE"/>
<property name="eclipselink.logging.parameters" value="true"/>
```

### Gestion d'Erreurs

- **Gestion d'exceptions globale** dans les managed beans
- **Messages d'erreur utilisateur** via FacesMessage
- **Redirection automatique** en cas d'erreur

## Évolution et Maintenance

### Points d'Amélioration Potentiels

1. **Sécurité avancée** : Authentification JWT, rôles utilisateurs
2. **Performance** : Cache, lazy loading optimisé
3. **Tests unitaires** : JUnit pour la logique métier
4. **Interface admin** : Gestion du catalogue par administrateur
5. **API REST** : Exposition de services RESTful
6. **Paiement intégré** : Stripe, PayPal SDK

### Structure Évolutive

L'architecture modulaire permet l'ajout facile de :
- Nouveaux types de produits
- Méthodes de paiement
- Systèmes de notification
- Intégrations tierces

## Auteur et Contact

- **Développé par** : Ismail (Étudiant/Autodidacte)
- **Date de création** : Octobre 2025
- **Version** : 1.0-SNAPSHOT
- **License** : Projet éducatif

## Support

Pour toute question ou problème :
1. Consulter les logs du serveur d'application
2. Vérifier la configuration de la base de données
3. Examiner les messages d'erreur dans les beans CDI

---

*Ce projet représente une implémentation complète d'une application e-commerce moderne utilisant les meilleures pratiques Java EE avec JSF et JPA.*
