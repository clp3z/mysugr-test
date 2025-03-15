package com.clp3z.mysugrtest.features

import android.content.Context
import com.clp3z.mysugrtest.R
import com.clp3z.mysugrtest.entity.GlucoseUnit

fun String.toGlucoseUnit(context: Context): GlucoseUnit = when (this) {
    context.getString(R.string.mmol_l) -> GlucoseUnit.MMOL_L
    context.getString(R.string.mg_dl) -> GlucoseUnit.MG_DL
    else -> GlucoseUnit.UNKNOWN
}

fun GlucoseUnit.toString(context: Context): String = when (this) {
    GlucoseUnit.MG_DL -> context.getString(R.string.mg_dl)
    GlucoseUnit.MMOL_L -> context.getString(R.string.mmol_l)
    else -> context.getString(R.string.unknown)
}
