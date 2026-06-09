package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

enum class MetricType {
    STEPS,
    WATER,
    WEIGHT,
    SLEEP,
    BLOOD_PRESSURE,
    HEART_RATE
}

@Entity(tableName = "health_metrics")
data class HealthMetric(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: MetricType,
    val timestamp: Long,
    val value1: Float, // Primary value (e.g., steps, ml, weight, systolic)
    val value2: Float? = null, // Secondary value (e.g., diastolic)
    val notes: String = ""
)
