<?php
require_once 'config.php';
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");

// 1. GET REQUEST: Show markers
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    try {
        $sql = "SELECT * FROM hazards 
                WHERE report_date >= NOW() - INTERVAL 36 HOUR 
                ORDER BY report_date DESC";

        $stmt = $pdo->prepare($sql);
        $stmt->execute();
        $hazards = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($hazards, JSON_PRETTY_PRINT);
    } catch (PDOException $e) {
        echo json_encode(['error' => $e->getMessage()]);
    }
}

// 2. POST REQUEST: Report a hazard
elseif ($_SERVER['REQUEST_METHOD'] === 'POST') {
    
    // Receive data
    $lat = $_POST['latitude'] ?? null;
    $lng = $_POST['longitude'] ?? null;
    $type = $_POST['hazard_type'] ?? null;
    
    // THIS IS THE MANUAL NAME (Whatever they typed in the box)
    $reporter = $_POST['reporter_name'] ?? 'Mobile User'; 
    
    $locName = $_POST['location_name'] ?? 'Unknown Location';
    $desc = $_POST['other_details'] ?? '';
    
    // THIS IS THE HIDDEN ID (For your database records only)
    $userId = !empty($_POST['user_id']) ? $_POST['user_id'] : null;

    if ($lat && $lng && $type) {
        try {
            // We insert both the Manual Name AND the Hidden User ID
            $sql = "INSERT INTO hazards (location_name, latitude, longitude, hazard_type, other_details, reporter_name, user_id, report_date, user_agent) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), 'Android App')";

            $stmt = $pdo->prepare($sql);
            $stmt->execute([$locName, $lat, $lng, $type, $desc, $reporter, $userId]);
            
            echo json_encode([
                "status" => "success", 
                "message" => "Hazard reported successfully"
            ]);
        } catch (PDOException $e) {
            echo json_encode([
                "status" => "error", 
                "message" => "Database Error: " . $e->getMessage()
            ]);
        }
    } else {
        echo json_encode([
            "status" => "error", 
            "message" => "Missing required fields"
        ]);
    }
}
?>