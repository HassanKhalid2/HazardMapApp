<?php
session_start();

// Security Check: If not logged in, redirect to login page
if (!isset($_SESSION['is_logged_in']) || $_SESSION['is_logged_in'] !== true) {
    header("Location: admin_login.php");
    exit;
}

require_once 'config.php';


if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['delete_id'])) {
    
    $id = $_POST['delete_id'];

    try {
        // Prepare statement to prevent SQL injection
        $stmt = $pdo->prepare("DELETE FROM hazards WHERE id = ?");
        $stmt->execute([$id]);
        
        // Success: Redirect back to list
        header("Location: list.php?msg=deleted");
        exit;

    } catch (PDOException $e) {
        die("Error deleting record: " . $e->getMessage());
    }
} else {
    // If someone tries to open this file directly without POST data
    header("Location: list.php");
    exit;
}
?>