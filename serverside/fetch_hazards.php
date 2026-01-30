<?php
// fetch_hazards.php
require_once 'config.php';

// 1. Fetch data
$stmt = $pdo->query("SELECT * FROM hazards ORDER BY report_date DESC");
$hazards = $stmt->fetchAll(PDO::FETCH_ASSOC);

// 2. Generate HTML Rows
if (count($hazards) > 0) {
    foreach ($hazards as $row) {
        // --- Badge Logic (Copied from your original code) ---
        $type = strtolower($row['hazard_type']);
        $bgColor = '#6f42c1'; // Default Purple
        $textColor = 'white';
        $icon = '⚠️';

        if ($type == 'flood') {
            $bgColor = '#0d6efd'; $icon = '🌊';
        } elseif ($type == 'landslide') {
            $bgColor = '#8B4513'; $icon = '⛰️';
        } elseif ($type == 'fire') {
            $bgColor = '#fd7e14'; $icon = '🔥'; $textColor = 'black';
        } elseif ($type == 'accident') {
            $bgColor = '#b80202'; $icon = '💥';
        } elseif ($type == 'construction') {
            $bgColor = '#6c757d'; $icon = '🏗️';
        } elseif ($type == 'road_closure' || strpos($type, 'road') !== false) {
            $bgColor = '#ffc107'; $icon = '🚧'; $textColor = 'black';
        }
        
        // --- Output the Row ---
        ?>
        <tr>
            <td class="ps-3">
                <div class="fw-bold text-nowrap"><?= date('d M Y', strtotime($row['report_date'])) ?></div>
                <small class="text-muted"><?= date('h:i A', strtotime($row['report_date'])) ?></small>
            </td>

            <td>
                <div class="fw-bold"><?= htmlspecialchars($row['location_name']) ?></div>
                <?php if (!empty($row['other_details'])): ?>
                    <div class="text-danger small mt-1">
                        <i class="bi bi-exclamation-circle-fill"></i> Note: <?= htmlspecialchars($row['other_details']) ?>
                    </div>
                <?php endif; ?>
            </td>

            <td>
                <span class="badge rounded-pill" style="background-color: <?= $bgColor ?>; color: <?= $textColor ?>;">
                    <?= $icon ?> <?= ucfirst(str_replace('_', ' ', $type)) ?>
                </span>
            </td>

            <td class="font-monospace text-nowrap">
                <span class="text-primary">Lat:</span> <?= number_format($row['latitude'], 6) ?><br>
                <span class="text-primary">Lng:</span> <?= number_format($row['longitude'], 6) ?>
            </td>

            <td>
                <div class="badge bg-light text-dark border">
                    <i class="bi bi-person"></i> <?= htmlspecialchars($row['reporter_name']) ?>
                </div>
            </td>

            <td>
                <small class="text-muted d-inline-block text-truncate" style="max-width: 150px;" title="<?= htmlspecialchars($row['user_agent'] ?? 'Unknown') ?>">
                    <?= htmlspecialchars($row['user_agent'] ?? 'N/A') ?>
                </small>
            </td>

            <td class="text-end pe-3">
                <form method="POST" action="delete.php" onsubmit="return confirm('Delete this report?');">
                    <input type="hidden" name="delete_id" value="<?= $row['id'] ?>">
                    <button type="submit" class="btn btn-outline-danger btn-sm"><i class="bi bi-trash"></i></button>
                </form>
            </td>
        </tr>
        <?php
    }
} else {
    echo '<tr><td colspan="7" class="text-center py-5 text-muted">No hazards reported yet.</td></tr>';
}
?>