<?php
session_start();

// Security Check: If not logged in, redirect to login page
if (!isset($_SESSION['is_logged_in']) || $_SESSION['is_logged_in'] !== true) {
    header("Location: admin_login.php");
    exit;
}

require_once 'config.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['submit_hazard'])) {
    
    $type = $_POST['hazard_type'];
    $otherDetails = ($type === 'other') ? $_POST['other_details'] : null;
    $userAgent = $_SERVER['HTTP_USER_AGENT'];

    try {
        $stmt = $pdo->prepare("INSERT INTO hazards (location_name, latitude, longitude, hazard_type, other_details, reporter_name, user_agent, report_date) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())");

        $stmt->execute([
            $_POST['location_name'],
            $_POST['latitude'],
            $_POST['longitude'],
            $type,
            $otherDetails,
            $_POST['reporter_name'],
            $userAgent
        ]);

        header("Location: list.php");
        exit;
    } catch (PDOException $e) {
        die("Error: " . $e->getMessage());
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Report Hazard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
    
    <style>
        #map { height: 400px; width: 100%; border-radius: 8px; }
        /* Add a small loading spinner for the address */
        .loading-spinner { display: none; margin-left: 10px; color: #666; font-size: 0.9em; }
    </style>
</head>
<body class="bg-light">

    <?php include 'navbar.php'; ?>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow">
                    <div class="card-header bg-danger text-white">
                        <h4 class="mb-0">Report Hazard</h4>
                    </div>
                    <div class="card-body">
                        <form method="POST">
                            
                            <div class="mb-3">
                                <label class="form-label">Location Name</label>
                                <span id="addressLoader" class="loading-spinner"><span class="spinner-border spinner-border-sm"></span> Detecting address...</span>
                                <input type="text" class="form-control" name="location_name" id="locationName" placeholder="e.g. Gelong, 06000, Kedah..." required>
                                <div class="form-text">You can edit this name if the auto-detected one is not accurate.</div>
                            </div>

                            <div class="row g-3 mb-3">
                                <div class="col-md-5">
                                    <label class="form-label">Latitude</label>
                                    <input type="number" step="any" id="lat" name="latitude" class="form-control" placeholder="0.0000" required>
                                </div>
                                <div class="col-md-5">
                                    <label class="form-label">Longitude</label>
                                    <input type="number" step="any" id="lng" name="longitude" class="form-control" placeholder="0.0000" required>
                                </div>
                                <div class="col-md-2 d-flex align-items-end">
                                    <button type="button" class="btn btn-primary w-100" data-bs-toggle="modal" data-bs-target="#mapModal">
                                        <i class="bi bi-geo-alt-fill"></i> Pick Map
                                    </button>
                                </div>
                            </div>

                            <div class="row g-3 mb-3">
                                <div class="col-md-5">
                                    <label class="form-label">Hazard Type</label>
                                    <select class="form-select" name="hazard_type" id="typeSelect" onchange="toggleOther()">
                                        <option value="flood">🌊 Flood</option>
                                        <option value="landslide">⛰️ Landslide</option>
                                        <option value="road_closure">🚧 Road Closure</option>
                                        <option value="construction">🏗️ Construction</option>
                                        <option value="accident">💥 Accident</option>
                                        <option value="fire">🔥 Fire</option>
                                        <option value="other">⚠️ Other</option>
                                    </select>
                                </div>
                                
                                <div class="col-md-7">
                                    <label class="form-label">Reporter Name</label>
                                    <input type="text" class="form-control" name="reporter_name" placeholder="Your Name" required>
                                </div>
                            </div>

                            <div class="mb-3" id="otherDiv" style="display:none;">
                                <label class="form-label text-danger">Please specify "Other" hazard:</label>
                                <input type="text" class="form-control border-danger" name="other_details" id="otherInput" placeholder="e.g. Fallen Tree, Oil Spill...">
                            </div>

                            <div class="d-grid gap-2 mt-4">
                                <button type="submit" name="submit_hazard" class="btn btn-danger btn-lg">Submit Report</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="mapModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title"><i class="bi bi-map"></i> Pick Location</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p class="small text-muted mb-2">Drag the blue pin to the exact location.</p>
                    <div id="map"></div>
                    <div class="d-grid gap-2 mt-2">
                        <button type="button" class="btn btn-outline-primary" onclick="locateUser()">
                            <i class="bi bi-crosshair"></i> Use My Current Location
                        </button>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-success" onclick="confirmLocation()" data-bs-dismiss="modal">Confirm Location</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>

    <script>
        let map;
        let marker;
        let currentLat = 6.44; 
        let currentLng = 100.20;

        // --- MAP INITIALIZATION ---
        const mapModal = document.getElementById('mapModal');
        mapModal.addEventListener('shown.bs.modal', function () {
            if (!map) {
                map = L.map('map').setView([currentLat, currentLng], 13);
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    attribution: '&copy; OpenStreetMap'
                }).addTo(map);

                marker = L.marker([currentLat, currentLng], {draggable: true}).addTo(map);

                marker.on('dragend', function(e) {
                    const position = marker.getLatLng();
                    currentLat = position.lat;
                    currentLng = position.lng;
                });
            } else {
                map.invalidateSize();
            }
        });

        // --- LOCATE USER ---
        function locateUser() {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(position => {
                    const lat = position.coords.latitude;
                    const lng = position.coords.longitude;
                    map.setView([lat, lng], 16);
                    marker.setLatLng([lat, lng]);
                    currentLat = lat;
                    currentLng = lng;
                }, () => {
                    alert("Unable to retrieve location.");
                });
            } else {
                alert("Geolocation not supported.");
            }
        }

        // --- CONFIRM LOCATION & FETCH ADDRESS ---
        function confirmLocation() {
            // 1. Fill Lat/Lng
            document.getElementById("lat").value = currentLat.toFixed(6);
            document.getElementById("lng").value = currentLng.toFixed(6);

            // 2. Start Address Detection
            const addressInput = document.getElementById("locationName");
            const loader = document.getElementById("addressLoader");

            // Show loading state
            addressInput.value = "";
            addressInput.placeholder = "Detecting location name...";
            loader.style.display = "inline-block";

            // 3. Call OpenStreetMap API (Reverse Geocoding)
            // Point to local PHP file instead(get_address.php) to avoid lag 
            fetch(`get_address.php?lat=${currentLat}&lng=${currentLng}`)
                .then(response => response.json())
                .then(data => {
                    loader.style.display = "none";
                    if (data && data.display_name) {
                        // Success: Fill the box
                        addressInput.value = data.display_name;
                    } else {
                        // Failed to find name
                        addressInput.value = "Unknown Location(Type manually)";
                    }
                })
                .catch(error => {
                    loader.style.display = "none";
                    console.error('Error:', error);
                    addressInput.value = ""; // Leave blank for user to type
                    addressInput.placeholder = "Could not detect name. Please type manually.";
                });
        }

        function toggleOther() {
            const select = document.getElementById('typeSelect');
            const div = document.getElementById('otherDiv');
            const input = document.getElementById('otherInput');
            if (select.value === 'other') {
                div.style.display = 'block';
                input.required = true;
            } else {
                div.style.display = 'none';
                input.required = false;
                input.value = ''; 
            }
        }
    </script>
</body>
</html>