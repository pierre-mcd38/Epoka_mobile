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
    $mis_idUtilisateur = $_GET['mis_idUtil'];
    $mis_idCommune = $_GET['mis_idCom'];
    $mis_dateDepart = $_GET['mis_dDepart'];
    $mis_dateRetour = $_GET['mis_dRetour'];

    // Requête SQL pour insérer une nouvelle mission
    $sql = "INSERT INTO mission (mis_idUtilisateur, mis_idCommune, mis_dateDepart, mis_dateRetour) 
        VALUES (:mis_idUtilisateur, :mis_idCommune, :mis_dateDepart, :mis_dateRetour)";

    // Préparation de la requête
    $stmt = $pdo->prepare($sql);

    // Liaison des paramètres
    $stmt->bindParam(':mis_idUtilisateur', $mis_idUtilisateur);
    $stmt->bindParam(':mis_idCommune', $mis_idCommune);
    $stmt->bindParam(':mis_dateDepart', $mis_dateDepart);
    $stmt->bindParam(':mis_dateRetour', $mis_dateRetour);

    // Exécution de la requête
    if(!$stmt->execute()){
        throw new Exception("Erreur insert");
    }

    // Fermer la connexion
    $pdo = null;
} catch (PDOException $e) {
    // Gestion des erreurs PDO
    echo "Erreur SGBD : " . $e->getMessage();
}
