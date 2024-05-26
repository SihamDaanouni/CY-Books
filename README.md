![logocy](https://github.com/SihamDaanouni/CY-Books/assets/82617886/0b329506-60f4-44bf-94e3-8a3213ee0ec9)

# CY-Books version JavaFX

Cy-Books est une application JavaFX de gestion de livres pour les bibliothécaires. Elle permet de gérer une bibliothèque et ses clients avec des fonctionnalités d'ajout, de suppression et de modification de client ainsi que la possibilité de rechercher des livres via l'API de la Bibliothèque nationale française. L'application utilise SQLite pour la gestion de la base de données.

## Les Fonctionnalités

- Connexion en tant que bibliothécaire uniquement.
- **Connexion et création de client afin de voir :**
  - Leurs emprunts et leurs dates de rendu.
  - Leurs informations personnelles.
- La liste des livres disponibles avec une fonctionnalité de recherche connectée à la base de données de la BNF.
- La liste de tous les emprunts avec la fonctionnalité de voir les retards.
- Le top 10 des livres les plus empruntés depuis 1 mois.

## Les Prérequis

- Java JDK 20 ou supérieur.
- MAVEN 3.6 ou supérieur.
- IntelliJ IDEA 2023.1.
- Une connexion à internet pour la connexion à l'API BNF.

### Base de Données
Cy-Books utilise SQLite comme base de données. Le fichier de base de données est 'database' et il est situé dans le répertoire des ressources. Si il n'est pas présent, le code est conçu pour le générer naturellement avec comme bibliothécaire Eva@gmail.com.

## Installation

1) Veuillez ouvrir le dossier Cy-Book_javafx avec IntelliJ IDEA.
2) Allez sur la page Main.java et exécutez le programme.
3) L'identifiant de la bibliothécaire est Eva@gmail.com et son mot de passe est 0000.

## Structure du projet
Le projet est sensé suivre cette arborescence

![image](https://github.com/SihamDaanouni/CY-Books/assets/82617886/a22db1a2-742b-43dd-9a5d-c9dd8e3bc448)
![image](https://github.com/SihamDaanouni/CY-Books/assets/82617886/73e60b0a-83bf-4872-95e4-26f2248ca10d)

## Utilisation

- Page **de lancement** : Connexion de la bibliothécaire.
- Page **Accueil** : Liste des fonctionnalités.
- Page **Connecter un client** :
    - Création d'un compte utilisateur avec : mail, prénom, nom, adresse et numéro de téléphone (pas de vérification sur les données entrées).
    - Connexion à un mail d'un utilisateur déjà existant, possibilité de voir et de rendre un livre emprunté ainsi que modifier ses informations (excepté le mail).
- Page **Rechercher un livre** :
    - "*Barre de recherche BNF*": Permet de chercher un livre dans la base de données de la BNF.
    - Bouton "*Mettre à jour la table*": Permet d'actualiser la table et de retirer les livres de la précédente recherche.
    - "*Barre de recherche filtre*": Permet de rechercher filtré dans la base de données parmi tous les critères disponibles.
    - Bouton "*Reset*": Permet de réinitialiser la table et d'afficher à nouveau tous les livres de la recherche BNF.
    - Bouton "*Filtre*": Permet une recherche filtrée spécifique au filtre choisi.
- Page **Afficher la table des emprunts** :
    - "*Barre de recherche*": Fait une recherche filtrée des utilisateurs présents dans le tableau.
    - Bouton "*Reset*": Réinitialise le tableau des emprunts.
    - Bouton "*Voir les retards*": Montre l'ensemble des emprunts en retard.
- Page **Top 10 des livres empruntés** : Montre les 10 livres les plus empruntés entre aujourd'hui et le mois dernier.

## Problèmes connus

- Si une erreur survient avec la base de données, relancez le programme.
- Si le problème persiste, veuillez, via IntelliJ, appuyer sur CTRL+MAJ+ALT+S et accéder à Bibliothèques (ou Librairies) afin d'ajouter un lien vers le "sqlite-jdbc" présent dans le fichier ressources.

![image](https://github.com/SihamDaanouni/CY-Books/assets/82617886/db939b84-4d12-44e7-8210-b8a857629be0)

Sinon, essayez de connecter l'IDE à la base de données à gauche de votre écran en sélectionnant le fichier de la base de données déjà existant grâce à **Data Sources from Path**.

![image](https://github.com/SihamDaanouni/CY-Books/assets/82617886/e03819d7-48ba-4723-ad42-eee80e30f4a0)

Ensuite, entrez dans la console SQL une commande invalide pour actualiser la base de données.

Si vous trouvez d'autres problèmes, vérifiez bien le contenu de votre pom.xml et actualisez bien les pop-up de Maven. Vérifiez aussi le contenu de votre module-info-java présent dans main/java.

# CY-Books version console

## Les Prérequis

- Java JDK 20 ou supérieur.
- Une connexion à internet pour la connexion à l'API BNF.

## Exécution

```bash
# Compilation des fichiers source Java
javac -cp "sqlite-jdbc-3.42.0.1.jar" *.java

# Compilation du main
java -cp ".:sqlite-jdbc-3.42.0.1.jar" Main
```

## Crédit :

### Pascal LE ING1 GI1
### Siham DAANOUNI ING1 GI1
### Thomas HAUTION ING1 GI1
### Amine AIT MOUSSA ING1 GI3
### Amaury PROVENT ING1 GI1

![image](https://github.com/SihamDaanouni/CY-Books/assets/82617886/024b5fe2-29a4-4771-ade6-37623fd728b2)
