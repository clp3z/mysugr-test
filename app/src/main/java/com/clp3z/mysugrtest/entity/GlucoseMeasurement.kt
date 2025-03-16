package com.clp3z.mysugrtest.entity

data class GlucoseMeasurement(
    val id: Long = 0L,
    val unit: GlucoseUnit,
    val value: Float
)
