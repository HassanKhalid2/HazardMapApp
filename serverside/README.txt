PROJECT: Simple Hazard Tracker (PHP + MySQL)
============================================

DESCRIPTION
-----------
This is a demonstration web application that allows users to reporting hazards 
(floods, landslides, etc.) and exposes a JSON API to retrieve recent data. 

It demonstrates the "Separation of Concerns" principle:
1. The Frontend (index.php): Handles user interaction and HTML display.
2. The Backend API (api.php): Handles raw data output (JSON) for other applications.
3. The Configuration (config.php): Handles database connectivity centrally.

PREREQUISITES
-------------
1. A local web server environment (XAMPP, WAMP, MAMP, or LAMP stack).
2. PHP 7.4 or higher.
3. MySQL or MariaDB database.

INSTALLATION STEPS
------------------

STEP 1: PLACE FILES
   - Copy the project folder into your server's root directory.
     (e.g., 'C:\xampp\htdocs\hazard_tracker' or '/var/www/html/hazard_tracker').

STEP 2: DATABASE SETUP
   - Open phpMyAdmin (usually at http://localhost/phpmyadmin).
   - Click "New" to create a database.
   - Name the database: hazard_tracker
   - Click the "Import" tab.
   - Select the file 'hazards.sql' from this project folder and click "Go".
   
   *Note: This will create the 'hazards' table automatically.*

STEP 3: CONFIGURE CONNECTION
   - Open 'config.php' in a text editor (Notepad, VS Code, etc.).
   - Check the $username and $password variables.
   - If using default XAMPP:
     $username = 'root';
     $password = ''; 
   - If using MAMP (Mac):
     $username = 'root';
     $password = 'root';

USAGE
-----

1. Access the Web Interface:
   Open your browser and go to:
   http://localhost/hazard_tracker/index.php
   
   - Use the form to submit a few hazard reports.
   - Verify that they appear in the HTML table below the form.

2. Access the JSON API:
   Open your browser and go to:
   http://localhost/hazard_tracker/api.php

   - This will display raw data in JSON format.
   - Note: It only displays hazards reported in the last 36 hours.

FILE STRUCTURE EXPLAINED
------------------------
- hazards.sql  : The blueprint for the database structure.
- config.php   : Contains the database credentials. Included by other files.
- index.php    : The "View". Contains HTML and Form logic for humans.
- api.php      : The "Endpoint". Contains logic to output machine-readable data.