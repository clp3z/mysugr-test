package com.clp3z.mysugrtest.framework.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.clp3z.mysugrtest.entity.GlucoseUnit

@Entity
data class LocalGlucoseMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @TypeConverters(GlucoseUnitConverter::class) val unit: GlucoseUnit,
    val value: Float
)
