package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.HealthMetric
import com.example.data.HealthRepository
import com.example.data.MetricType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HealthViewModel(application: Application) : AndroidViewModel(application) {
    private val database = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "health_database"
    ).build()

    private val repository = HealthRepository(database.healthMetricDao())

    val allMetrics: StateFlow<List<HealthMetric>> = repository.allMetrics
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val todayMetrics: StateFlow<List<HealthMetric>> = repository.getTodayMetrics()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addMetric(type: MetricType, value1: Float, value2: Float? = null, notes: String = "") {
        viewModelScope.launch {
            repository.insert(
                HealthMetric(
                    type = type,
                    timestamp = System.currentTimeMillis(),
                    value1 = value1,
                    value2 = value2,
                    notes = notes
                )
            )
        }
    }

    fun deleteMetric(id: Int) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }
}
