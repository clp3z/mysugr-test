package com.clp3z.mysugrtest.framework.persistence.model

import androidx.room.TypeConverter
import com.clp3z.mysugrtest.entity.GlucoseUnit

class GlucoseUnitConverter {

    @TypeConverter
    fun fromGlucoseUnitType(unit: GlucoseUnit): String = unit.name

    @TypeConverter
    fun toGlucoseUnitType(value: String): GlucoseUnit = GlucoseUnit.valueOf(value)
}
