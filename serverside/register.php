<?php
// 1. Include your existing database connection
require_once 'config.php';

// 2. Set headers for JSON (Mobile App communication)
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    
    // Get data sent from Android
    $email = $_POST['email'] ?? '';
    $password = $_POST['password'] ?? '';
    $fullName = $_POST['full_name'] ?? 'User'; // Default name if not sent

    if (!empty($email) && !empty($password)) {
        try {
            // A. Check if email already exists
            $checkStmt = $pdo->prepare("SELECT id FROM users WHERE email = ?");
            $checkStmt->execute([$email]);
            
            if ($checkStmt->rowCount() > 0) {
                echo json_encode(["status" => "error", "message" => "Email already registered"]);
            } else {
                // B. Insert new user
                // (Note: In production, use password_hash($password, PASSWORD_DEFAULT))
                $insertStmt = $pdo->prepare("INSERT INTO users (full_name, email, password) VALUES (?, ?, ?)");
                
                if ($insertStmt->execute([$fullName, $email, $password])) {
                    echo json_encode(["status" => "success", "message" => "Account created!"]);
                } else {
                    echo json_encode(["status" => "error", "message" => "Failed to create account"]);
                }
            }
        } catch (PDOException $e) {
            echo json_encode(["status" => "error", "message" => "Database error: " . $e->getMessage()]);
        }
    } else {
        echo json_encode(["status" => "error", "message" => "Missing email or password"]);
    }
}
?>