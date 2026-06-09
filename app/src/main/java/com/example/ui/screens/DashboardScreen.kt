package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.HealthMetric
import com.example.data.MetricType
import com.example.ui.HealthViewModel

@Composable
fun DashboardScreen(
    viewModel: HealthViewModel,
    onAddEntry: (MetricType) -> Unit,
    modifier: Modifier = Modifier
) {
    val todayMetrics by viewModel.todayMetrics.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Today's Overview",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        val grouped = todayMetrics.groupBy { it.type }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickAddCard(
                    modifier = Modifier.weight(1f),
                    title = "Water",
                    icon = Icons.Default.WaterDrop,
                    value = grouped[MetricType.WATER]?.sumOf { it.value1.toDouble() }?.toFloat()?.toString() + " ml",
                    onClick = { onAddEntry(MetricType.WATER) }
                )
                QuickAddCard(
                    modifier = Modifier.weight(1f),
                    title = "Steps",
                    icon = Icons.AutoMirrored.Filled.DirectionsWalk,
                    value = grouped[MetricType.STEPS]?.sumOf { it.value1.toDouble() }?.toFloat()?.toString() ?: "0",
                    onClick = { onAddEntry(MetricType.STEPS) }
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickAddCard(
                    modifier = Modifier.weight(1f),
                    title = "Sleep",
                    icon = Icons.Default.Bedtime,
                    value = grouped[MetricType.SLEEP]?.map { it.value1 }?.maxOrNull()?.toString()?.plus(" hrs") ?: "--",
                    onClick = { onAddEntry(MetricType.SLEEP) }
                )
                QuickAddCard(
                    modifier = Modifier.weight(1f),
                    title = "Weight",
                    icon = Icons.Default.MonitorWeight,
                    value = grouped[MetricType.WEIGHT]?.lastOrNull()?.value1?.toString()?.plus(" kg") ?: "--",
                    onClick = { onAddEntry(MetricType.WEIGHT) }
                )
            }
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickAddCard(
                    modifier = Modifier.weight(1f),
                    title = "Heart Rate",
                    icon = Icons.Default.Favorite,
                    value = grouped[MetricType.HEART_RATE]?.lastOrNull()?.value1?.toInt()?.toString()?.plus(" bpm") ?: "--",
                    onClick = { onAddEntry(MetricType.HEART_RATE) }
                )
                QuickAddCard(
                    modifier = Modifier.weight(1f),
                    title = "Blood Pressure",
                    icon = Icons.Default.FavoriteBorder,
                    value = grouped[MetricType.BLOOD_PRESSURE]?.lastOrNull()?.let { "${it.value1.toInt()}/${it.value2?.toInt()}" } ?: "--",
                    onClick = { onAddEntry(MetricType.BLOOD_PRESSURE) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Recent Logs",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        items(todayMetrics) { metric ->
            MetricItemRow(metric = metric, onDelete = { viewModel.deleteMetric(it) })
        }
        
        if (todayMetrics.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No entries today. Tap a card above to add one!",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun QuickAddCard(
    title: String,
    icon: ImageVector,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .height(120.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            val displayValue = if (value != "null ml") value else "0 ml"
            Text(
                text = displayValue,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
