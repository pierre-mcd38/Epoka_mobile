<?php
// Connexion à la base de données
$serveur = "127.0.0.1"; // Adresse du serveur MySQL
$utilisateur = "root"; // Nom d'utilisateur MySQL
$motdepasse = "root"; // Mot de passe MySQL
$base_de_donnees = "epoka"; // Nom de la base de données MySQL
$charset = "utf8"; // Encodage des caractères

try {
    // Connexion à la base de données avec PDO et définition de l'encodage des caractères
    $pdo = new PDO("mysql:host=$serveur;dbname=$base_de_donnees;charset=$charset", $utilisateur, $motdepasse);
    
    // Paramètres d'erreur et d'exception
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Requête SQL pour récupérer les villes avec com_categorie égal à 1
    $sql = "SELECT * FROM commune ORDER BY com_categorie, com_nom" ;
    $stmt = $pdo->prepare($sql);
    $stmt->execute();

    // Initialisation d'un tableau pour stocker les objets JSON
    $results = array();

    // Boucle pour récupérer chaque ligne et la stocker dans le tableau
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        // Ajout de chaque ligne au tableau
        $results[] = $row;
    }

    // Fermer la connexion
    $pdo = null;

    // Convertir le tableau en JSON
    $json_result = json_encode($results, JSON_UNESCAPED_UNICODE);

    // Afficher le JSON résultant
    echo $json_result;

} catch (PDOException $e) {
    // Gestion des erreurs PDO
    echo "Erreur de connexion à la base de données: " . $e->getMessage();
}
?>
