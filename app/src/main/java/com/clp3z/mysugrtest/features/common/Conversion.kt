package com.clp3z.mysugrtest.features.common

import com.clp3z.mysugrtest.entity.GlucoseUnit

private const val CONVERSION_FACTOR = 18.0182f

fun Float.toMgDl(): Float = this * CONVERSION_FACTOR

fun Float.toMmolL(): Float = this / CONVERSION_FACTOR

fun  Float.toUnitValue(unit: GlucoseUnit): Float = when (unit) {
    GlucoseUnit.MG_DL -> this.toMgDl()
    GlucoseUnit.MMOL_L -> this.toMmolL()
    else -> 0f
}
