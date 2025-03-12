package com.clp3z.mysugrtest.framework.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.clp3z.mysugrtest.framework.persistence.dao.GlucoseMeasurementDAO
import com.clp3z.mysugrtest.framework.persistence.model.LocalGlucoseMeasurement

@Database(
    entities = [LocalGlucoseMeasurement::class],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract fun glucoseMeasurementDao(): GlucoseMeasurementDAO
}
