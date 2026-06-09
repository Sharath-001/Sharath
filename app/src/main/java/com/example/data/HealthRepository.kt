package com.example.data

import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class HealthRepository(private val dao: HealthMetricDao) {
    val allMetrics: Flow<List<HealthMetric>> = dao.getAllMetrics()

    fun getMetricsByType(type: MetricType): Flow<List<HealthMetric>> {
        return dao.getMetricsByType(type)
    }

    fun getTodayMetrics(): Flow<List<HealthMetric>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfDay = calendar.timeInMillis

        return dao.getMetricsForDay(startOfDay, endOfDay)
    }

    suspend fun insert(metric: HealthMetric) {
        dao.insertMetric(metric)
    }

    suspend fun delete(id: Int) {
        dao.deleteMetricById(id)
    }
}
