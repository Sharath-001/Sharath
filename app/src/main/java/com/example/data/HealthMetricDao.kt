package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthMetricDao {
    @Query("SELECT * FROM health_metrics ORDER BY timestamp DESC")
    fun getAllMetrics(): Flow<List<HealthMetric>>

    @Query("SELECT * FROM health_metrics WHERE type = :type ORDER BY timestamp DESC")
    fun getMetricsByType(type: MetricType): Flow<List<HealthMetric>>

    @Query("SELECT * FROM health_metrics WHERE timestamp >= :startOfDay AND timestamp <= :endOfDay ORDER BY timestamp DESC")
    fun getMetricsForDay(startOfDay: Long, endOfDay: Long): Flow<List<HealthMetric>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetric(metric: HealthMetric)

    @Query("DELETE FROM health_metrics WHERE id = :id")
    suspend fun deleteMetricById(id: Int)
}
