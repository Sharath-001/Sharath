package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.HealthMetric
import com.example.data.MetricType
import com.example.ui.HealthViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: HealthViewModel,
    modifier: Modifier = Modifier
) {
    val allMetrics by viewModel.allMetrics.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "All History",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (allMetrics.isEmpty()) {
            item {
                Text(
                    "No data yet.",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(allMetrics) { metric ->
                MetricItemRow(metric = metric, onDelete = { viewModel.deleteMetric(it) })
            }
        }
    }
}

@Composable
fun MetricItemRow(metric: HealthMetric, onDelete: (Int) -> Unit) {
    val icon = when (metric.type) {
        MetricType.WATER -> Icons.Default.WaterDrop
        MetricType.STEPS -> Icons.AutoMirrored.Filled.DirectionsWalk
        MetricType.WEIGHT -> Icons.Default.MonitorWeight
        MetricType.SLEEP -> Icons.Default.Bedtime
        MetricType.HEART_RATE -> Icons.Default.Favorite
        MetricType.BLOOD_PRESSURE -> Icons.Default.FavoriteBorder
    }
    
    val title = metric.type.name.replace("_", " ")

    val valueText = when (metric.type) {
        MetricType.WATER -> "${metric.value1.toInt()} ml"
        MetricType.STEPS -> "${metric.value1.toInt()} steps"
        MetricType.WEIGHT -> "${metric.value1} kg"
        MetricType.SLEEP -> "${metric.value1} hrs"
        MetricType.HEART_RATE -> "${metric.value1.toInt()} bpm"
        MetricType.BLOOD_PRESSURE -> "${metric.value1.toInt()}/${metric.value2?.toInt() ?: 0} mmHg"
    }

    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    val dateString = dateFormat.format(Date(metric.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Text(text = dateString, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (metric.notes.isNotEmpty()) {
                    Text(text = metric.notes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text(
                text = valueText,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = { onDelete(metric.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
