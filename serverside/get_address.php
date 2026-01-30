<?php
// get_address.php
header('Content-Type: application/json');

// 1. Check coordinates
if (!isset($_GET['lat']) || !isset($_GET['lng'])) {
    echo json_encode(["error" => "Missing coordinates"]);
    exit;
}

$lat = $_GET['lat'];
$lng = $_GET['lng'];

// 2. Prepare the URL
$url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=$lat&lon=$lng&zoom=18&addressdetails=1";

// 3. Initialize cURL (The "Messenger")
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

// --- THE CRITICAL FIX FOR XAMPP ---
// This tells PHP to stop worrying about SSL certificates
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);

// --- THE CRITICAL FIX FOR OPENSTREETMAP ---
// We must identify ourselves to avoid being blocked
curl_setopt($ch, CURLOPT_USERAGENT, "HazardTrackerApp/1.0");

// 4. Execute
$response = curl_exec($ch);
$httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
curl_close($ch);

// 5. Return result
if ($httpCode == 200 && $response) {
    echo $response;
} else {
    echo json_encode(["display_name" => "Type Location Manually"]);
}
?>