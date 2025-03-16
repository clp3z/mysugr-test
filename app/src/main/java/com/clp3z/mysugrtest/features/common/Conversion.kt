package com.clp3z.mysugrtest.features.common

import com.clp3z.mysugrtest.entity.GlucoseUnit
import kotlin.math.roundToInt

private const val CONVERSION_FACTOR = 18.0182f

fun Float.toMgDl(): Float {
    val converted = this * CONVERSION_FACTOR
    return converted.roundToInt().toFloat()
}

fun Float.toMmolL(): Float {
    val converted = this / CONVERSION_FACTOR
    return (converted * 10).roundToInt() / 10f
}

fun  Float.toUnitValue(unit: GlucoseUnit): Float = when (unit) {
    GlucoseUnit.MG_DL -> this.toMgDl()
    GlucoseUnit.MMOL_L -> this.toMmolL()
    else -> 0f
}

fun Float.toPresentationValue(unit: GlucoseUnit): String = when (unit) {
    GlucoseUnit.MG_DL -> this.toInt().toString()
    GlucoseUnit.MMOL_L -> "%.1f".format(this)
    else -> ""
}
