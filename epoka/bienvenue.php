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

    // Requête SQL pour vérifier l'authentification avec PDO et des requêtes préparées pour éviter les injections SQL
    $sql = "SELECT util_nom FROM utilisateurs WHERE util_id = :util_id";
    $stmt = $pdo->prepare($sql);
    $stmt->bindParam(':util_id', $util_id);
    $stmt->execute();

    // Vérification du résultat de la requête
    if ($stmt->rowCount() > 0) {
        // Authentification réussie
        $resultat = $stmt->fetch(PDO::FETCH_ASSOC);
        $util_nom = $resultat['util_nom'];
        echo "$util_nom !";
    } else {
        // Authentification échouée
        echo "Erreur lors de la récupération";
    }

    // Fermer la connexion
    $pdo = null;
} catch (PDOException $e) {
    // Gestion des erreurs PDO
    echo "Erreur de connexion à la base de données: " . $e->getMessage();
}
?>
