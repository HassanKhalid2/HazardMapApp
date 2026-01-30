<?php
session_start();
if (!isset($_SESSION['is_logged_in']) || $_SESSION['is_logged_in'] !== true) {
    header("Location: admin_login.php");
    exit;
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hazard Tracker | Live Feed</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        .table-hover tbody tr:hover { background-color: #f8f9fa; }
        .font-monospace { font-size: 0.85rem; }
    </style>
</head>

<body class="bg-light">

    <?php include 'navbar.php'; ?>

    <div class="container py-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>🚨 Live Hazard Feed</h2>
            <div class="spinner-grow text-danger spinner-grow-sm" role="status"></div>
        </div>

        <?php if (isset($_GET['msg']) && $_GET['msg'] == 'deleted'): ?>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle"></i> Hazard report deleted.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <?php endif; ?>

        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        
                        <thead class="table-dark">
                            <tr class="text-center align-middle"> 
                                <th class="py-2">Timestamp</th>
                                <th class="py-2">Location & Info</th>
                                <th class="py-2">Type</th>
                                <th class="py-2">GPS Coordinates</th>
                                <th class="py-2">Reporter</th>
                                <th class="py-2">User Agent</th>
                                <th class="py-2">Action</th>
                            </tr>
                        </thead>
                        <tbody id="liveDataBody">
                            <tr>
                                <td colspan="7" class="text-center py-5">
                                    <div class="spinner-border text-primary" role="status"></div>
                                    <p class="mt-2 text-muted">Connecting to Mobile Feed...</p>
                                </td>
                            </tr>
                        </tbody>
                        
                    </table>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        function refreshTable() {
            fetch('fetch_hazards.php') 
                .then(response => response.text())
                .then(data => {
                    document.getElementById('liveDataBody').innerHTML = data;
                })
                .catch(error => console.error('Error fetching data:', error));
        }

        // 1. Load immediately
        refreshTable();

        // 2. Refresh every 2 seconds
        setInterval(refreshTable, 2000);
    </script>

</body>
</html>