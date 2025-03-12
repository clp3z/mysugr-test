package com.clp3z.mysugrtest.framework.persistence.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.clp3z.mysugrtest.framework.persistence.model.LocalGlucoseMeasurement
import kotlinx.coroutines.flow.Flow

interface GlucoseMeasurementDAO {

    @Query("SELECT * FROM LocalGlucoseMeasurement")
    fun getGlucoseMeasurements(): Flow<List<LocalGlucoseMeasurement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGlucoseMeasurement(value: LocalGlucoseMeasurement)
}
