package com.clp3z.mysugrtest.features.common

import com.clp3z.mysugrtest.entity.GlucoseUnit

fun Float.toMgDl(): Float = this * 18.0182f

fun Float.toMmolL(): Float = this / 18.0182f

fun  Float.toUnitValue(unit: GlucoseUnit): Float = when (unit) {
    GlucoseUnit.MG_DL -> this.toMgDl()
    GlucoseUnit.MMOL_L -> this.toMmolL()
    else -> 0f
}
