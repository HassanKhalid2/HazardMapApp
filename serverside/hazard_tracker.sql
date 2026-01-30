-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 30, 2026 at 04:10 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hazard_tracker`
--

-- --------------------------------------------------------

--
-- Table structure for table `hazards`
--

CREATE TABLE `hazards` (
  `id` int(11) NOT NULL,
  `location_name` varchar(255) NOT NULL,
  `latitude` decimal(10,8) NOT NULL,
  `longitude` decimal(11,8) NOT NULL,
  `hazard_type` varchar(50) DEFAULT NULL,
  `reporter_name` varchar(100) NOT NULL,
  `report_date` datetime NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `user_agent` varchar(255) DEFAULT NULL,
  `other_details` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hazards`
--

INSERT INTO `hazards` (`id`, `location_name`, `latitude`, `longitude`, `hazard_type`, `reporter_name`, `report_date`, `created_at`, `user_agent`, `other_details`, `user_id`) VALUES
(13, 'Cameron Highlands (Tanah Rata)', 4.47000000, 101.38000000, 'landslide', 'Farmer Joe', '2026-01-21 19:17:28', '2026-01-26 11:17:28', NULL, NULL, NULL),
(14, 'NKVE Damansara Toll', 3.13300000, 101.59000000, 'road_closure', 'Polis Trafik', '2026-01-19 19:17:28', '2026-01-26 11:17:28', NULL, NULL, NULL),
(15, 'Gua Musang Railway', 4.88300000, 101.96600000, 'flood', 'KTM Berhad', '2026-01-12 19:17:28', '2026-01-26 11:17:28', NULL, NULL, NULL),
(31, 'Unimap Main Gate', 6.46024900, 100.35183100, 'flood', 'Jojo Sasuke', '2026-01-27 23:19:28', '2026-01-27 15:19:28', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', NULL, NULL),
(32, 'Pauh', 6.43780800, 100.29847100, 'road_closure', 'Tudia', '2026-01-27 23:27:34', '2026-01-27 15:27:34', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', NULL, NULL),
(37, 'Kampung Kodiang Lama', 6.38280000, 100.32050000, 'landslide', 'Khalil', '2026-01-28 18:20:35', '2026-01-28 10:20:35', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', NULL, NULL),
(39, 'Kurong Anai', 6.42413600, 100.31289600, 'accident', 'Pak Samad', '2026-01-28 19:09:22', '2026-01-28 11:09:22', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', NULL, NULL),
(40, 'Paya, Kangar Perlis', 6.50362200, 100.23187100, 'other', 'Azri Arshad', '2026-01-28 19:10:48', '2026-01-28 11:10:48', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 'Fallen Tree', NULL),
(42, 'Gajah', 6.26650000, 100.42000000, 'other', 'Takahashi', '2026-01-28 20:21:05', '2026-01-28 12:21:05', 'Android App', 'Kameen Rider fighting', NULL),
(45, 'Anakkkkk', 6.54032238, 100.42703927, 'flood', 'DAMMMM', '2026-01-29 02:10:45', '2026-01-28 18:10:45', 'Android App', '', NULL),
(47, 'Kangar', 6.41805065, 100.19364148, 'road_closure', 'Amran', '2026-01-29 03:07:52', '2026-01-28 19:07:52', 'Android App', '', NULL),
(48, 'Alor Setar', 6.15816050, 100.31456351, 'fire', 'Hassan', '2026-01-29 03:47:54', '2026-01-28 19:47:54', 'Android App', '', NULL),
(57, '47, K509 Jalan Kampung Paya Rawa, 06700 Pendang, Kedah, Malaysia', 6.03858522, 100.53762373, 'fire', 'Rahman', '2026-01-29 18:28:18', '2026-01-29 10:28:18', 'Android App', '', NULL),
(58, 'Kampung Telaga Batu, Kubang Pasu, Kedah, 06150, Malaysia', 6.23543200, 100.33194700, 'construction', 'Naim', '2026-01-29 18:32:10', '2026-01-29 10:32:10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', NULL, NULL),
(60, 'FELDA Guar Napai, Kubang Pasu, Kedah, Malaysia', 6.37825300, 100.38036300, 'road_closure', 'Rahim', '2026-01-29 22:36:42', '2026-01-29 14:36:42', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', NULL, NULL),
(62, 'No.51, Kampung Kuala Lanjut, 06350 Pokok Sena, Kedah, Malaysia', 6.08654795, 100.52060813, 'other', 'Aisyah', '2026-01-30 08:46:57', '2026-01-30 00:46:57', 'Android App', 'Boar', 4),
(63, 'No 1A, Kampung Alor China, 06400 Pokok Sena, Kedah, Malaysia', 6.09656178, 100.47609217, 'landslide', 'Karim Jabar', '2026-01-30 08:47:34', '2026-01-30 00:47:34', 'Android App', '', 4),
(65, '109, Jalan Derga Perdana 7, Kampung Pengapi Batu, 05300 Alor Setar, Kedah, Malaysia', 6.12474131, 100.40141914, 'accident', 'Naruto', '2026-01-30 08:49:02', '2026-01-30 00:49:02', 'Android App', '', 4);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `full_name`, `email`, `password`, `created_at`) VALUES
(1, 'Ramadan', 'ramdan@gmail.com', '123', '2026-01-28 19:32:05'),
(4, 'Hassan Khalid', 'hasan@gmail.com', '123', '2026-01-28 19:40:25'),
(5, 'Zafran Zauri', 'zafran@gmail.com', '123', '2026-01-29 05:35:15'),
(6, 'Jamil Rahman', 'jamil@gmail.com', '123', '2026-01-29 11:54:48');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `hazards`
--
ALTER TABLE `hazards`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `hazards`
--
ALTER TABLE `hazards`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=66;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
