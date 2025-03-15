package com.clp3z.mysugrtest.features.bottomsheet

fun String.isFloat() = this.toFloatOrNull() != null

fun Float.isPositive() = this >= 0

fun String.isMeasurementValid(): Boolean =
    this.isNotBlank() && this.isFloat() && this.toFloat().isPositive()
