<?php
// Get the current page name (e.g., "list.php" or "report.php")
$current_page = basename($_SERVER['PHP_SELF']);
?>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
  <div class="container">
    <a class="navbar-brand d-flex align-items-center" href="list.php">
      <img src="images/logo.png" alt="Logo" width="30" height="30" class="d-inline-block align-text-top me-2">
      HazardMap Tracker
    </a>
    
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
      <span class="navbar-toggler-icon"></span>
    </button>
    
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav ms-auto">
        
        <li class="nav-item">
          <a class="nav-link <?= ($current_page == 'report.php') ? 'active text-warning' : '' ?>" 
             href="report.php">Report Hazard</a>
        </li>
        
        <li class="nav-item">
          <a class="nav-link <?= ($current_page == 'list.php') ? 'active text-warning' : '' ?>" 
             href="list.php">View Hazards</a>
        </li>
        
        <li class="nav-item">
          <a class="nav-link" target="_blank" href="api.php">API (JSON)</a>
        </li>
        
        <li class="nav-item">
          <a class="nav-link btn text-danger" href="logout.php">Logout</a>
        </li>
        
      </ul>
    </div>
  </div>
</nav>