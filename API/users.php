<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

// filepath: c:\UniServerZ\www\API\users.php

// Connexion à la BDD
$host    = 'localhost';
$db      = 'gamearras';
$user    = "root";
$pass    = "root";
$charset = 'utf8mb4';

$dsn     = "mysql:host=$host;dbname=$db;charset=$charset";
$options = [
    PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    PDO::ATTR_EMULATE_PREPARES   => false,
];

try {
    $pdo = new PDO($dsn, $user, $pass, $options);
} catch (\PDOException $e) {
    http_response_code(500);
    echo json_encode(['message' => 'Erreur de connexion à la base de données']);
    exit;
}

//trouve l'utilisateur avec son email
function getUserByEmail($pdo, $email) {
    $stmt = $pdo->prepare('SELECT * FROM user WHERE email = ?');
    $stmt->execute([$email]);
    $result = $stmt->fetch();
    error_log('Requête getUserByEmail pour email ' . $email . ' : ' . print_r($result, true));
    return $result;
}


if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    http_response_code(400);
    echo json_encode(['message' => 'Requête invalide : méthode GET requise']);
    exit;
}

// Récupération des paramètres depuis $_GET
if (isset($_GET['email'], $_GET['password']) && !empty($_GET['email']) && !empty($_GET['password'])) {
    $email    = trim($_GET['email']);
    $password = $_GET['password'];
} else {
    http_response_code(400);
    echo json_encode(['message' => 'Paramètres manquants']);
    exit;
}

// Vérification que ca envoi bien email
if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    http_response_code(400);
    echo json_encode(['message' => 'Email invalide']);
    exit;
}

error_log("Email reçu : " . $email);
error_log("Mot de passe reçu : " . $password);


$user = getUserByEmail($pdo, $email);

//vérifie le mot de passe et récupère la liste de ces réservations
if ($user && password_verify($password, $user['password'])) {

    $sql = "SELECT r.id,
                   r.user_id,
                   r.forfait_id,
                   r.access_code,
                   r.created_at,
                   r.temps_restant,
                   f.name AS forfait_nom, 
                   f.duration
            FROM reservation r 
            LEFT JOIN forfait f ON r.forfait_id = f.id 
            WHERE r.user_id = ?";
    $stmt = $pdo->prepare($sql);
    $stmt->execute([$user['id']]);
    $reservationDetails = $stmt->fetchAll();

    header('Content-Type: application/json');
    echo json_encode([
        'message'     => 'Authentification réussie',
        'user_id'     => $user['id'],
        'email'       => $user['email'],
        'pseudo'      => $user['pseudo'],
        'reservations'=> $reservationDetails
    ]);
    exit;
} else {
    http_response_code(401);
    echo json_encode(['message' => 'Identifiants incorrects', 'user_id' => -1]);
    exit;
}
?>