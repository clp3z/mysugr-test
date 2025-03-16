package com.clp3z.mysugrtest.data

import arrow.core.Either
import com.clp3z.mysugrtest.entity.Error

fun <T> T.rightWithError(): Either<Error, T> = Either.Right(this)

fun <T> Error.leftWithError(): Either<Error, T> = Either.Left(this)

fun Throwable.toDatabaseError(): Error.Database =
    Error.Database(this.localizedMessage ?: this.stackTraceToString())

fun Throwable.toUnknownError(): Error.Unknown =
    Error.Unknown(this.localizedMessage ?: this.stackTraceToString())