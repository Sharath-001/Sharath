package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.MetricType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryScreen(
    metricType: MetricType,
    onSave: (Float, Float?, String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var value1 by remember { mutableStateOf("") }
    var value2 by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val title = metricType.name.replace("_", " ")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add $title") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Enter your details below",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Dynamic Input Fields
            when (metricType) {
                MetricType.BLOOD_PRESSURE -> {
                    OutlinedTextField(
                        value = value1,
                        onValueChange = { value1 = it },
                        label = { Text("Systolic (e.g. 120)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = value2,
                        onValueChange = { value2 = it },
                        label = { Text("Diastolic (e.g. 80)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                MetricType.WATER -> {
                    OutlinedTextField(
                        value = value1,
                        onValueChange = { value1 = it },
                        label = { Text("Amount (ml)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                MetricType.STEPS -> {
                    OutlinedTextField(
                        value = value1,
                        onValueChange = { value1 = it },
                        label = { Text("Steps Count") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                MetricType.SLEEP -> {
                    OutlinedTextField(
                        value = value1,
                        onValueChange = { value1 = it },
                        label = { Text("Hours of Sleep") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                MetricType.WEIGHT -> {
                    OutlinedTextField(
                        value = value1,
                        onValueChange = { value1 = it },
                        label = { Text("Weight (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                MetricType.HEART_RATE -> {
                    OutlinedTextField(
                        value = value1,
                        onValueChange = { value1 = it },
                        label = { Text("Heart Rate (bpm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val v1 = value1.toFloatOrNull() ?: 0f
                    val v2 = if (metricType == MetricType.BLOOD_PRESSURE) {
                        value2.toFloatOrNull() ?: 0f
                    } else null

                    onSave(v1, v2, notes)
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = value1.isNotBlank()
            ) {
                Text("Save Entry", fontSize = MaterialTheme.typography.titleMedium.fontSize, fontWeight = FontWeight.Bold)
            }
        }
    }
}
