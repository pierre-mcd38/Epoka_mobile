<?php
// Informations de connexion à la base de données
$serveur = "localhost"; // Adresse du serveur MySQL 
$utilisateur = "root"; // Nom d'utilisateur MySQL
$motdepasse = "root"; // Mot de passe MySQL
$base_de_donnees = "epoka"; // Nom de la base de données MySQL

try {
    // Connexion à la base de données avec PDO
    $pdo = new PDO("mysql:host=$serveur;dbname=$base_de_donnees", $utilisateur, $motdepasse);
    
    // Configuration de PDO pour afficher les erreurs
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Exécutez votre requête SQL ici et traitez les résultats
    // Par exemple, pour sélectionner toutes les données d'une table nommée "utilisateurs":
    $query = "SELECT * FROM utilisateurs";
    $resultat = $pdo->query($query);

    // Traitement des résultats
    foreach ($resultat as $row) {
        // Faites quelque chose avec chaque ligne de résultat, par exemple:
        echo "user: " . $row["util_id"]. " - mdp: " . $row["util_mdp"]. " - prenom: " . $row["util_prenom"]. "<br>";
    }
} catch(PDOException $e) {
    // En cas d'erreur, affichez un message d'erreur
    echo "Erreur de connexion: " . $e->getMessage();
}

// Fermez la connexion à la base de données après avoir terminé
$pdo = null;

