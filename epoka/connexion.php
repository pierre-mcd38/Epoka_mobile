<?php
// Connexion à la base de données
$serveur = "127.0.0.1"; // Adresse du serveur MySQL
$utilisateur = "root"; // Nom d'utilisateur MySQL
$motdepasse = "root"; // Mot de passe MySQL
$base_de_donnees = "epoka"; // Nom de la base de données MySQL

try {
    // Connexion à la base de données avec PDO
    $pdo = new PDO("mysql:host=$serveur;dbname=$base_de_donnees", $utilisateur, $motdepasse);
    
    // Paramètres d'erreur et d'exception
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Récupération des données d'identification envoyées via la requête GET
    $util_id = $_GET['util_id'];
    $util_mdp = $_GET['util_mdp'];

    // Requête SQL pour vérifier l'authentification avec PDO et des requêtes préparées pour éviter les injections SQL
    $sql = "SELECT * FROM utilisateurs WHERE util_id = :util_id AND util_mdp = :util_mdp";
    $stmt = $pdo->prepare($sql);
    $stmt->bindParam(':util_id', $util_id);
    $stmt->bindParam(':util_mdp', $util_mdp);
    $stmt->execute();

    // Vérification du résultat de la requête
    if ($stmt->rowCount() > 0) {
        // Authentification réussie
        echo "authentification réussie";
    } else {
        // Authentification échouée
        echo "authentification échouée";
    }

    // Fermer la connexion
    $pdo = null;
} catch (PDOException $e) {
    // Gestion des erreurs PDO
    echo "Erreur de connexion à la base de données: " . $e->getMessage();
}
?>
