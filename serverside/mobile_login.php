<?php
require_once 'config.php';
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    
    $email = $_POST['email'] ?? '';
    $password = $_POST['password'] ?? '';

    if (!empty($email) && !empty($password)) {
        try {
            // Find user with matching email AND password
            $stmt = $pdo->prepare("SELECT * FROM users WHERE email = ? AND password = ?");
            $stmt->execute([$email, $password]);
            
            $user = $stmt->fetch(PDO::FETCH_ASSOC);

            if ($user) {
                // Success! Send back the user info (excluding password)
                echo json_encode([
                    "status" => "success",
                    "message" => "Login Successful",
                    "user_id" => $user['id'],
                    "full_name" => $user['full_name'],
                    "email" => $user['email']
                ]);
            } else {
                echo json_encode(["status" => "error", "message" => "Invalid email or password"]);
            }
        } catch (PDOException $e) {
            echo json_encode(["status" => "error", "message" => "Database error"]);
        }
    } else {
        echo json_encode(["status" => "error", "message" => "Missing credentials"]);
    }
}
?>